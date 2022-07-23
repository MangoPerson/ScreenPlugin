package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;

public class MWorldCommand extends SCommand {

    public MWorldCommand() {
        super("mworld");
        addSubCommand("create", new MWCreateCommand());
        addSubCommand("remove", new MWRemoveCommand());
        addSubCommand("teleport", new MWTeleportCommand());
        addSubCommand("list", new MWListCommand());
    }

    @Override
    protected boolean run() {
        return true;
    }
}
