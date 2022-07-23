package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MWCreateCommand extends SCommand {

    public MWCreateCommand() {
        super("mwcreate");
    }

    @Override
    protected boolean checkArgs() {
        return args.length == 1;
    }

    @Override
    protected boolean run() {
        //make sure the world doesn't already exist
        if (getServer().getWorld(args[0]) != null) {
            reply(args[0] + " already exists");
            return true;
        }
        if (getUnloadedWorlds().stream().anyMatch(f -> f.getName().equalsIgnoreCase(args[0]))) {
            reply(args[0] + " is an unloaded world. Please use /mwload to load it");
            return true;
        }

        //make a world creator with the name specified
        WorldCreator wc = new WorldCreator(args[0]);

        boolean t = useNSOption("t", "Must use a valid world type (normal, flat, amplified, large_biomes)",
                str -> WorldType.valueOf(str.toUpperCase()), ops -> wc.type(ops.get(0)));
        boolean s = useNSOption("s", "Seed must be a number",
                str -> Long.valueOf(str), ops -> wc.seed(ops.get(0)));
        if (!t || !s) return true;

        //create the world
        wc.createWorld();
        FileConfiguration config = ScreenPlugin.getInstance().getConfig();
        List<String> worldNames = config.getStringList("loaded-worlds");
        worldNames.add(wc.name());
        config.set("loaded-worlds", worldNames);
        ScreenPlugin.getInstance().saveConfig();
        ScreenPlugin.getInstance().saveDefaultConfig();
        reply("Created " + wc.type().getName().toLowerCase() + " world \"" + wc.name() + "\"" + " with seed " + wc.seed());
        return true;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        if (arg < 1) return new ArrayList<>();
        if (args[arg-1].equalsIgnoreCase("--t")) {
            //get list of world names
            List<String> names = new ArrayList<>();
            for (WorldType type : WorldType.values()) {
                names.add(type.name().toLowerCase());
            }
            return names;
        }
        return new ArrayList<>();
    }
}
