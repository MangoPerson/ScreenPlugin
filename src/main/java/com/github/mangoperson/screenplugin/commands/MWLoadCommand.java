package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MWLoadCommand extends SCommand {
    public MWLoadCommand() {
        super("mwload");
    }

    @Override
    protected boolean run() {
        File file = getFirstMatch(getUnloadedWorlds(), f -> (f.getName().equalsIgnoreCase(args[0])));
        if (file == null) {
            reply("The directory you specified does not exist");
            return true;
        }

        new WorldCreator(args[0]).createWorld();
        FileConfiguration config = ScreenPlugin.getInstance().getConfig();
        List<String> worldNames = config.getStringList("loaded-worlds");
        worldNames.add(args[0]);
        config.set("loaded-worlds", worldNames);
        reply("Loaded " + args[0]);
        ScreenPlugin.getInstance().saveConfig();
        ScreenPlugin.getInstance().saveDefaultConfig();
        return true;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        switch (arg) {
            case 0:
                return convertAll(getUnloadedWorlds(), f -> f.getName());
            default:
                return new ArrayList<>();
        }
    }

    @Override
    protected boolean checkArgs() {
        return args.length == 1;
    }
}
