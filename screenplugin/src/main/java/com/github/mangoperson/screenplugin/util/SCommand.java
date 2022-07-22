package com.github.mangoperson.screenplugin.util;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class SCommand implements TabExecutor {
    protected CommandSender sender;
    protected String[] args;
    protected final String name;
    private JavaPlugin plugin;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.args = args;
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
        String argString = "";
        for (String s : args) {
            argString += s + ", ";
        }
        String cmdString = "";
        for(SCommand s : subCommands.values()) {
            cmdString += s.name + ", ";
        }
        if (subCommands.size() > 0) {
            if (args.length <= 1) {
                System.out.println("Command: " + name + "\nArgs: " + argString + "\nSubcommands: " + cmdString);
                return filter(new ArrayList<>(subCommands.keySet()));
            }
            for (Map.Entry<String, SCommand> subCommand : subCommands.entrySet()) {
                System.out.println("Command in loop: " + subCommand.getValue().name);
                if (args[0].equalsIgnoreCase(subCommand.getKey())) {
                    SCommand cmd = subCommand.getValue();
                    System.out.println("Command: " + name + "\nArgs: " + argString + "\nSubcommands: " + cmdString +  "\nCurrent Subcommand: " + cmd.name);
                    cmd.args = ArrayUtils.remove(args, 0);
                    return filter(cmd.onTabComplete(sender, command, label, cmd.args));
                }
            }
            System.out.println("Command: " + name + "\nArgs: " + argString + "\nSubcommands: " + cmdString);
            return new ArrayList<>();
        }
        System.out.println("Command: " + name + "\nArgs: " + argString + "\nSubcommands: " + cmdString);
        List<String> tabList = tabComplete(args.length - 1);
        if (tabList != null) return filter(tabList);

        return new ArrayList<>();
    }

    public void register(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand(name).setExecutor(this);
        plugin.getCommand(name).setTabCompleter(this);
    }

    public void addSubCommand(String name, SCommand subCommand) {
        subCommands.put(name, subCommand);
    }

    protected Object cfg(String key) {
        return plugin.getConfig().get(key);
    }

    private List<String> filter(List<String> tabOptions) {
        List<String> result = new ArrayList<>();
        for (String s : tabOptions) {
            if (s.startsWith(args[args.length - 1])) {
                result.add(s);
            }
        }

        return result;
    }
}
