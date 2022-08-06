package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

public class MWTeleportCommand extends SCommand {

    private boolean useDefault = false;

    public MWTeleportCommand() {
        super("mwtp");
    }

    @Override
    protected boolean checkArgs() {
        if (args.length == 4) {
            //if user specified xyz, make sure they are valid numbers
            useDefault = false;
            if (!isNumber(args[1])) return false;
            if (!isNumber(args[2])) return false;
            if (!isNumber(args[3])) return false;
            return true;
        }
        else if (args.length == 1) {
            //otherwise use the default values
            useDefault = true;
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected boolean run() {
        //make sure a player executes the command
        if (!(sender instanceof Player)) {
            reply("This command must be run by a player");
            return true;
        }
        //get world specififed by the player
        World w = getServer().getWorld(args[0]);
        //make sure the world exists
        if (w == null) {
            reply("The world you specified does not exist");
            return true;
        }

        //initialize xyz
        double x = 0;
        double y = 0;
        double z = 0;

        //if the player set the xyz, use them. If not, keep as default of 0, 0, 0
        if (!useDefault) {
            x = Double.parseDouble(args[1]);
            y = Double.parseDouble(args[2]);
            z = Double.parseDouble(args[3]);
        }

        //teleport the player
        Player p = (Player) sender;
        p.teleport(new Location(w, x, y, z));

        reply("Teleported to " + x + ", " + y + ", " + z + " in " + w.getName());
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
