package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

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
        if (getServer().getWorld(args[0]) != null) {
            reply(args[0] + " already exists");
            return true;
        }

        WorldCreator wc = new WorldCreator(args[0]);
        String[] opT = oargs("t");

        if (opT.length > 0) {
            String t = opT[0];
            if (!EnumUtils.isValidEnum(WorldType.class, t.toUpperCase())) {
                reply(t + " is not a valid world type");
                return true;
            }
            WorldType wt = WorldType.valueOf(t.toUpperCase());
            wc.type(wt);
        }

        wc.createWorld();
        reply("Created " + wc.type().getName().toLowerCase() + " world \"" + args[0] + "\"");
        return true;
    }
}
