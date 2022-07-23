package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MWTeleportCommand extends SCommand {

    private boolean useDefault = false;

    public MWTeleportCommand() {
        super("mwtp");
    }

    @Override
    protected boolean checkArgs() {
        if (args.length == 4) {
            useDefault = false;
            if (!isNumber(args[1])) return false;
            if (!isNumber(args[2])) return false;
            if (!isNumber(args[3])) return false;
            return true;
        }
        else if (args.length == 1) {
            useDefault = true;
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected boolean run() {
        World w = getServer().getWorld(args[0]);
        if (w == null) {
            reply("The world you specified does not exist");
            return true;
        }
        int x = 0;
        int y = 0;
        int z = 0;

        if (!useDefault) {
            x = Integer.parseInt(args[1]);
            y = Integer.parseInt(args[2]);
            z = Integer.parseInt(args[3]);
        }

        Player p = (Player) sender;
        p.teleport(new Location(w, x, y, z));

        reply("Teleported to " + x + ", " + y + ", " + z + " in " + w.getName());
        return true;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        switch (arg) {
            case 0:
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
