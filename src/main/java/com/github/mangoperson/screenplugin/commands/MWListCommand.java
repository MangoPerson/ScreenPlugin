package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.World;

import java.util.stream.Collectors;

public class MWListCommand extends SCommand {
    public MWListCommand() {
        super("mwlist");
    }

    @Override
    protected boolean run() {
        reply("Current worlds are: " +
                getServer().getWorlds().stream()
                        .map(World::getName)
                        .collect(Collectors.joining(", "))
        );
        return true;
    }
}
