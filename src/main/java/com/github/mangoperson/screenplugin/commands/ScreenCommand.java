package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.Colors;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScreenCommand extends SCommand {

    public ScreenCommand() {
        super("screen");
    }

    @Override
    protected boolean run() {
        if (args.length != 2) return false;
        if (!NumberUtils.isParsable(args[0])) return false;
        if (!NumberUtils.isParsable(args[1])) return false;

        World w;
        if (sender instanceof Player) {
            w = ((Player) sender).getWorld();
        }
        else if (sender instanceof BlockCommandSender) {
            w = ((BlockCommandSender) sender).getBlock().getWorld();
        }
        else {
            w = sender.getServer().getWorlds().get(0);
        }

        String imgName = (String) cfg("image-name");

        File file = new File("./plugins/ScreenPlugin/" + imgName);
        BufferedImage img;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);

        for (float i = 0; i < img.getWidth(); i++) {
            for (float j = 0; j < img.getHeight(); j++) {
                Material closest = Colors.closestBlock(new Color(img.getRGB((int)i, (int)j)));
                List<Integer> location = (List) cfg("screen-location");
                int x = location.get(0);
                int y = location.get(1);
                int z = location.get(2);
                new Location(w, i*width/img.getWidth() + x, height - j*height/img.getHeight() + y, z).getBlock().setType(closest);
            }
        }
        return true;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        return null;
    }
}
