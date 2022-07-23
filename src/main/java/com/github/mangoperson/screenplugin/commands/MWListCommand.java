package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.World;

public class MWListCommand extends SCommand {
    public MWListCommand() {
        super("mwlist");
    }

    @Override
    protected boolean run() {
        String result = "Current worlds are: ";
        for (World w : getServer().getWorlds()) {
            result += w.getName() + ", ";
        }

        result = result.substring(0, result.length() - 2);

        reply(result);
        return true;
    }
}
