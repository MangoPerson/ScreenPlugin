package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;

public class MWHelpCommand extends SCommand {

    public MWHelpCommand() {
        super("mwhelp");
    }

    @Override
    protected boolean run() {
        reply("HELP");
        return true;
    }
}
