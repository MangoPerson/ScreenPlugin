package com.github.mangoperson.screenplugin.util;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class SCommand implements TabExecutor {
    protected CommandSender sender;
    protected Command command;
    protected String[] args;
    protected String name;

    private JavaPlugin plugin;

    protected abstract boolean run();

    protected abstract List<String> tabComplete(int arg);

    public SCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(name)) {
            this.sender = sender;
            this.command = command;
            this.args = args;
            return run();
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase(name)) {
            this.sender = sender;
            this.command = command;
            this.args = args;
            List<String> tabList = tabComplete(args.length - 1);
            if (tabList != null) list = tabList;
        }

        return list;
    }

    public void register(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand(name).setExecutor(this);
        plugin.getCommand(name).setTabCompleter(this);
    }

    protected Object cfg(String key) {
        return plugin.getConfig().get(key);
    }
}
