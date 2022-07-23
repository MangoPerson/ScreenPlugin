package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MWUnloadCommand extends SCommand {
    public MWUnloadCommand() {
        super("mwunload");
    }

    @Override
    protected boolean run() {
        World w = getServer().getWorld(args[0]);
        if (w == null) {
            reply("The world you specified does not exist");
            return true;
        }
        if (w == getServer().getWorlds().get(0)) {
            reply("Cannot unload the default world");
            return true;
        }

        //teleport all players in the specified world to the default world
        w.getPlayers().forEach(p -> {
            p.teleport(new Location(getServer().getWorlds().get(0), 0, 0, 0));
            p.sendMessage("The world you were in, " + w.getName() + ", was unloaded, so you have been returned to the default world");
        });
        //unload the world from the server
        Bukkit.unloadWorld(w, true);
        FileConfiguration config = ScreenPlugin.getInstance().getConfig();
        List<String> worldNames = config.getStringList("loaded-worlds");
        worldNames.remove(w.getName());
        config.set("loaded-worlds", worldNames);
        ScreenPlugin.getInstance().saveConfig();
        ScreenPlugin.getInstance().saveDefaultConfig();

        reply("Unloaded " + w.getName());
        return true;
    }

    @Override
    protected boolean checkArgs() {
        return args.length == 1;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        switch (arg) {
            case 0:
                //get list of world names
                List<String> names = new ArrayList<>();
                for (World world : getServer().getWorlds()) {
                    names.add(world.getName());
                }
                return names;
            default:
                return new ArrayList<>();
        }
    }
}
