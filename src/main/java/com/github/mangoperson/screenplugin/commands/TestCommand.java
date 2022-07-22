package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.SCommand;

public class TestCommand extends SCommand {

    public TestCommand() {
        super("mwtest");
        def.addSubCommand("ghi", ghi);
        abc.addSubCommand("def", def);
        abc.addSubCommand("ghi", ghi);
        addSubCommand("abc", abc);
        addSubCommand("def", def);
    }

    @Override
    protected boolean checkArgs() {
        if (args.length != 1) return false;
        return true;
    }

    @Override
    protected boolean run() {
        return true;
    }

    SCommand abc = new SCommand("abc") {
        @Override
        protected boolean run() {
            sender.sendMessage("abc");
            return true;
        }
        @Override
        protected boolean checkArgs() {
            if (args.length != 1) return false;
            return true;
        }
    };

    SCommand ghi = new SCommand("ghi") {
        @Override
        protected boolean run() {
            sender.sendMessage("ghi");
            return true;
        }
    };

    SCommand def = new SCommand("def") {
        @Override
        protected boolean run() {
            sender.sendMessage("def");
            return true;
        }
        @Override
        protected boolean checkArgs() {
            if (args.length != 1) return false;
            return true;
        }
    };
}
