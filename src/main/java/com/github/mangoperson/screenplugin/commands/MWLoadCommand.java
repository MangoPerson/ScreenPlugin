package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MWLoadCommand extends SCommand {
    public MWLoadCommand() {
        super("mwload");
    }

    @Override
    protected boolean run() {
        File file = getUnloadedWorlds().stream()
                .filter(f -> (f.getName().equalsIgnoreCase(args[0])))
                .collect(Collectors.toList())
                .get(0);
        if (file == null) {
            reply("The directory you specified does not exist");
            return true;
        }

        new WorldCreator(args[0]).createWorld();
        FileConfiguration config = ScreenPlugin.getInstance().getConfig();
        List<String> worldNames = config.getStringList("loaded-worlds");
        worldNames.add(args[0]);
        config.set("loaded-worlds", worldNames);
        reply("Loaded " + args[0]);
        ScreenPlugin.getInstance().saveConfig();
        ScreenPlugin.getInstance().saveDefaultConfig();
        return true;
    }

    @Override
    protected MList<String> tabComplete(int arg) {
        switch (arg) {
            case 0:
                return getUnloadedWorlds()
                        .map(f -> f.getName())
                        .collect(MList.toMList());
            default:
                return new MList<>();
        }
    }

    @Override
    protected boolean checkArgs() {
        return args.length == 1;
    }
}
