package com.github.mangoperson.screenplugin;

import com.github.mangoperson.screenplugin.commands.*;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public final class ScreenPlugin extends JavaPlugin {

    private static ScreenPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        new ScreenCommand().register();
        new MWorldCommand().register();

        new MWCreateCommand().register();
        new MWRemoveCommand().register();
        new MWTeleportCommand().register();
        new MWListCommand().register();
        new MWHelpCommand().register();
        new MWLoadCommand().register();
        new MWUnloadCommand().register();

        cfgInit();
        loadWorlds();
    }

    public void cfgInit() {
        //add default config values
        FileConfiguration config = getConfig();
        config.addDefault("image-name", "image.png");
        config.addDefault("screen-location", new int[] {0, 0, 0});
        config.addDefault("loaded-worlds", new String[] {"world"});
        config.options().copyDefaults(true);
        saveConfig();
        saveDefaultConfig();
    }

    public void loadWorlds() {
        FileConfiguration config = getConfig();
        List<String> worldNames = config.getStringList("loaded-worlds");
        for (String name : worldNames) {
            File worldFile = SCommand.getFirstMatch(SCommand.getUnloadedWorlds(), f -> f.getName().equalsIgnoreCase(name));
            if (worldFile != null) {
                new WorldCreator(name).createWorld();
            }
        }
    }

    public static ScreenPlugin getInstance() {
        return instance;
    }
}
