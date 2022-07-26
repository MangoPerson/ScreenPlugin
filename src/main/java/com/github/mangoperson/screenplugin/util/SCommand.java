package com.github.mangoperson.screenplugin.util;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class SCommand implements TabExecutor {
    protected CommandSender sender;
    protected String[] args;

    protected MList<MList<String>> options;
    protected final String name;
    protected MMap<String, SCommand> subCommands = new MMap<>();

    protected abstract boolean run();

    public SCommand(String name) {
        this.name = name;
    }

    protected MList<String> tabComplete(int arg) {
        return new MList<>();
    }

    protected boolean checkArgs() {
        return true;
    }

    protected Predicate<String> tabFilter = s -> s.startsWith(args[args.length-1]);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] inArgs) {
        //sort given args into command arguments and options
        MList<MList<String>> preOpts = new MList<>();
        List<String> preArgs = new ArrayList<>();

        //after the first arg beginning in --, set all following args to be in the options category
        boolean opsStarted = false;
        if (inArgs.length > 0) {
            for (String s : inArgs) {
                boolean sw = s.startsWith("--");
                opsStarted = opsStarted || sw;

                if (opsStarted) {
                    if (sw) preOpts.add(new MList<>());
                    int i = preOpts.size() - 1;
                    preOpts.get(i).add(s);
                } else preArgs.add(s);
            }
        }

        //set the class values for options and args to the sorted arguments
        options = preOpts;
        args = preArgs.toArray(new String[0]);

        //if the command has subcommands
        if (subCommands.size() > 0) {
            //make sure that a subcommand is being used
            if (args.length < 1) return false;
            return subCommands
                    .filter((n, c) -> args[0].equalsIgnoreCase(n))
                    .map((n, c) -> {
                        c.sender = sender;
                        c.args = ArrayUtils.remove(args, 0);
                        return c.onCommand(sender, command, label, c.args);
                    })
                    .addR(false)
                    .get(0);
        }
        //if the command has no subcommands, make sure the args are correct and run the command
        if (!checkArgs()) return false;
        this.sender = sender;
        return run();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        //everything in here is the same as in the onCommand function, but with tabComplete instead of run and without options
        this.sender = sender;
        this.args = args;
        if (subCommands.size() > 0) {
            if (args.length <= 1) {
                return subCommands.keyList();
            }
            return subCommands
                    .filter((n, c) -> args[0].equalsIgnoreCase(n))
                    .map((n, c) -> {
                        c.args = ArrayUtils.remove(args, 0);
                        return c.onTabComplete(sender, command, label, c.args).stream()
                                .filter(tabFilter)
                                .collect(Collectors.toList());
                    })
                    .get(0);
        }
        MList<String> tabList = new MList<>(tabComplete(args.length - 1));
        if (tabList != null) return tabList.filter(tabFilter);

        return new ArrayList<>();
    }

    //register the command executor to the plugin under this command's name
    public void register() {
        ScreenPlugin.getInstance().getCommand(name).setExecutor(this);
        ScreenPlugin.getInstance().getCommand(name).setTabCompleter(this);
    }

    //add a subcommand to this command
    public void addSubCommand(String name, SCommand subCommand) {
        subCommands.add(name, subCommand);
    }

    //retrieve a value from the config file
    protected Object cfg(String key) {
        return ScreenPlugin.getInstance().getConfig().get(key);
    }

    //send a message to the sender of the command
    protected void reply(String message) {
        sender.sendMessage(message);
    }

    //get arguments for a command option
    protected Optional<MList<String>> oargs(String opt) {
        MList<MList<String>> matching = options.filter(as -> as.get(0).equalsIgnoreCase("--" + opt));
        if (matching.size() > 0) {
            return Optional.of(matching
                    .get(0)
                    .removeR(0)
            );
        }
        return Optional.empty();
    }

    protected boolean hasOption(String name) {
        return options
                .filter(as -> as.addR("").get(0).equalsIgnoreCase("--" + name))
                .size() > 0;
    }
    //convenience functions for option checking
    protected void useOption(String name, Consumer<String[]> function) {
        String[] ops = oargs(name).isPresent() ? oargs(name).get().toArray(new String[0]) : new String[0];
        if (ops.length > 0) {
            function.accept(ops);
        }
    }
    //use a non-string option
    protected <T> boolean useNSOption(String name, String failMessage, Function<String, T> converter, Consumer<List<T>> function) {
        Optional<MList<String>> o = oargs(name);
        if (o.isEmpty()) return true;
        MList<String> ops = o.get();

        if (ops.size() > 0) {
            if (ops.allMatch(str -> canConvert(str, converter))) {
                List<T> t = ops
                        .map(converter)
                        .collect(Collectors.toList());
                function.accept(t);
            }
            else {
                reply(failMessage);
                return false;
            }
        }
        return true;
    }

    //test if a string is parsable as a number
    protected static boolean isNumber(String s) {
        return NumberUtils.isParsable(s);
    }

    public static <I, O> boolean canConvert(I i, Function<I, O> converter) {
        try {
            converter.apply(i);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    protected static Server getServer() {
        return ScreenPlugin.getInstance().getServer();
    }

    public static MList<File> getUnloadedWorlds() {
        File dir = new File("./");
        return Arrays.stream(dir.listFiles())
                .filter(f -> f.isDirectory())
                .filter(f -> getServer().getWorld(f.getName()) == null)
                .filter(f -> new File(f.getAbsolutePath() + "/level.dat").exists())
                .collect(MList.toMList());
    }
}
