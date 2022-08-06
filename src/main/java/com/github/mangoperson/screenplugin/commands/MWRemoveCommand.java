package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.WorldInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MWRemoveCommand extends SCommand {
    public MWRemoveCommand() {
        super("mwremove");
    }

    @Override
    protected boolean checkArgs() {
        return args.length == 1;
    }

    @Override
    protected boolean run() {
        //get the world specified by the user
        World w = getServer().getWorld(args[0]);
        if (w == null) {
            reply("The world you specified does not exist");
            return true;
        }
        if (w == getServer().getWorlds().get(0)) {
            reply("Cannot delete the default world");
            return true;
        }

        //teleport all players in the specified world to the default world
        w.getPlayers().forEach(p -> {
            p.teleport(new Location(getServer().getWorlds().get(0), 0, 0, 0));
            p.sendMessage("The world you were in, " + w.getName() + ", was deleted, so you have been returned to the default world");
        });
        //unload the world from the server
        Bukkit.unloadWorld(w, false);
        //delete the world directory
        File worldDir = new File("./" + w.getName() + "/");
        try {
            FileUtils.deleteDirectory(worldDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileConfiguration config = ScreenPlugin.getInstance().getConfig();
        List<String> worldNames = config.getStringList("loaded-worlds");
        worldNames.remove(w.getName());
        config.set("loaded-worlds", worldNames);
        ScreenPlugin.getInstance().saveConfig();
        ScreenPlugin.getInstance().saveDefaultConfig();

        reply("Deleted " + w.getName());
        return true;
    }

    @Override
    protected MList<String> tabComplete(int arg) {
        if (arg == 0) {
            return getServer().getWorlds().stream()
                    .map(WorldInfo::getName)
                    .collect(MList.toMList());
        }
        return new MList<>();
    }

}
