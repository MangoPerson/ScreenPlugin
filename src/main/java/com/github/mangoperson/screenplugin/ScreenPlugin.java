package com.github.mangoperson.screenplugin;

import com.github.mangoperson.screenplugin.commands.*;
import com.github.mangoperson.screenplugin.util.*;
import com.github.mangoperson.screenplugin.util.render.Colors;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class ScreenPlugin extends JavaPlugin {

    private static ScreenPlugin instance;

    private static MMap<Material, Color> colorMap;

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
        colorMap = Colors.getColorMap();
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
        new MList<>(config.getStringList("loaded-worlds"))
                .filter(n -> SCommand.getUnloadedWorlds().filter(f -> f.getName().equalsIgnoreCase(n)).size() > 0)
                .forEach(n -> new WorldCreator(n).createWorld());
    }

    public static ScreenPlugin getInstance() {
        return instance;
    }

    public static MMap<Material, Color> getColorMap() {
        return colorMap;
    }
}
