package com.github.mangoperson.screenplugin;

import com.github.mangoperson.screenplugin.commands.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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

        cfgInit();
    }

    public void cfgInit() {
        FileConfiguration config = getConfig();
        config.addDefault("image-name", "image.png");
        config.addDefault("screen-location", new int[] {0, 0, 0});
        config.options().copyDefaults(true);
        saveConfig();
        saveDefaultConfig();
    }

    public static ScreenPlugin getInstance() {
        return instance;
    }
}
