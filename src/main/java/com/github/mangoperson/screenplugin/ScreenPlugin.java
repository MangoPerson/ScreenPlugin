package com.github.mangoperson.screenplugin;

import com.github.mangoperson.screenplugin.commands.ScreenCommand;
import com.github.mangoperson.screenplugin.commands.TestCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScreenPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        new ScreenCommand().register(this);
        new TestCommand().register(this);

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
}
