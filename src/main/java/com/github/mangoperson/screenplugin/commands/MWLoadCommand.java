package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class MWLoadCommand extends SCommand {
    public MWLoadCommand() {
        super("mwload");
    }

    @Override
    protected boolean run() {
        Optional<File> file = getUnloadedWorlds()
                .getFirst(f -> (f.getName().equalsIgnoreCase(args[0])));
        if (file.isEmpty()) {
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
        if (arg == 0) {
            return getUnloadedWorlds()
                    .map(File::getName)
                    .collect(MList.toMList());
        }
        return new MList<>();
    }

    @Override
    protected boolean checkArgs() {
        return args.length == 1;
    }
}
