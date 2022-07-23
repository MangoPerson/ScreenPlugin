package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;
import org.apache.commons.lang3.EnumUtils;
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
        //make sure the world doesn't already exist
        if (getServer().getWorld(args[0]) != null) {
            reply(args[0] + " already exists");
            return true;
        }

        //make a world creator with the name specified
        WorldCreator wc = new WorldCreator(args[0]);
        //get type options specified
        String[] opT = oargs("t");

        //if user has specified type options
        if (opT.length > 0) {
            //type is the first argument
            String t = opT[0];
            //check if the world type exists
            if (!EnumUtils.isValidEnum(WorldType.class, t.toUpperCase())) {
                reply(t + " is not a valid world type");
                return true;
            }
            //convert the type name into a WorldType object
            WorldType wt = WorldType.valueOf(t.toUpperCase());
            //set the world creator to use that type of world
            wc.type(wt);
        }

        //create the world
        wc.createWorld();
        reply("Created " + wc.type().getName().toLowerCase() + " world \"" + args[0] + "\"");
        return true;
    }
}
