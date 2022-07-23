package com.github.mangoperson.screenplugin.util;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.lang.model.type.ArrayType;
import java.util.*;

public abstract class SCommand implements TabExecutor {
    protected CommandSender sender;
    protected String[] args;

    protected Set<List<String>> options;
    protected final String name;
    protected HashMap<String, SCommand> subCommands = new HashMap<>();

    protected abstract boolean run();

    public SCommand(String name) {
        this.name = name;
    }

    protected List<String> tabComplete(int arg) {
        return new ArrayList<>();
    }

    protected boolean checkArgs() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] inArgs) {
        List<List<String>> preOpts = new ArrayList<>();
        List<String> preArgs = new ArrayList<>();

        boolean opsStarted = false;
        if (inArgs.length > 0) {
            for (String s : inArgs) {
                boolean sw = s.startsWith("--");
                opsStarted = opsStarted || sw;

                if (opsStarted) {
                    if (sw) preOpts.add(new ArrayList<>());
                    int i = preOpts.size() - 1;
                    preOpts.get(i).add(s);
                } else preArgs.add(s);
            }
        }

        options = new HashSet<>(preOpts);
        args = preArgs.toArray(new String[0]);

        if (subCommands.size() > 0) {
            if (args.length < 1) return false;
            for (Map.Entry<String, SCommand> subCommand : subCommands.entrySet()) {
                if (args[0].equalsIgnoreCase(subCommand.getKey())) {
                    SCommand cmd = subCommand.getValue();
                    cmd.sender = sender;
                    cmd.args = ArrayUtils.remove(args, 0);
                    return cmd.onCommand(sender, command, label, cmd.args);
                }
            }
            return false;
        }
        if (!checkArgs()) return false;
        this.sender = sender;
        return run();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;
        if (subCommands.size() > 0) {
            if (args.length <= 1) {
                return filter(new ArrayList<>(subCommands.keySet()));
            }
            for (Map.Entry<String, SCommand> subCommand : subCommands.entrySet()) {
                if (args[0].equalsIgnoreCase(subCommand.getKey())) {
                    SCommand cmd = subCommand.getValue();
                    cmd.args = ArrayUtils.remove(args, 0);
                    return filter(cmd.onTabComplete(sender, command, label, cmd.args));
                }
            }
            return new ArrayList<>();
        }
        List<String> tabList = tabComplete(args.length - 1);
        if (tabList != null) return filter(tabList);

        return new ArrayList<>();
    }

    public void register() {
        ScreenPlugin.getInstance().getCommand(name).setExecutor(this);
        ScreenPlugin.getInstance().getCommand(name).setTabCompleter(this);
    }

    public void addSubCommand(String name, SCommand subCommand) {
        subCommands.put(name, subCommand);
    }

    protected Object cfg(String key) {
        return ScreenPlugin.getInstance().getConfig().get(key);
    }

    protected Server getServer() {
        return ScreenPlugin.getInstance().getServer();
    }

    protected void reply(String message) {
        sender.sendMessage(message);
    }

    private List<String> filter(List<String> tabOptions) {
        List<String> result = new ArrayList<>();
        tabOptions.forEach(s -> {
            if (s.startsWith(args[args.length - 1])) {
                result.add(s);
            }
        });

        return result;
    }

    protected String[] oargs(String opt) {
        for (List<String> as : options) {
            if (as.get(0).equalsIgnoreCase("--" + opt)) {
                as.remove(0);
                return as.toArray(new String[0]);
            }
        }
        return new String[0];
    }

    protected static boolean isNumber(String s) {
        return NumberUtils.isParsable(s);
    }
}
