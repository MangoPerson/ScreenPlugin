package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

        reply("Deleted " + w.getName());
        return true;
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
