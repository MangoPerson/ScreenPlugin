package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.Colors;
import com.github.mangoperson.screenplugin.util.SCommand;
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
import java.util.ArrayList;
import java.util.List;

public class ScreenCommand extends SCommand {

    public ScreenCommand() {
        super("screen");
    }

    @Override
    protected boolean checkArgs() {
        if (args.length != 2) return false;
        if (!isNumber(args[0])) return false;
        if (!isNumber(args[1])) return false;
        return true;
    }

    @Override
    protected boolean run() {
        //set executing world to the server default world
        final World[] world = {sender.getServer().getWorlds().get(0)};

        if (sender instanceof Player) {
            world[0] = ((Player) sender).getWorld();
        } else if (sender instanceof BlockCommandSender) {
            world[0] = ((BlockCommandSender) sender).getBlock().getWorld();
        }
        boolean w = useNSOption("w", "The world you specified does not exist. Using default world instead", str -> getServer().getWorld(str), ops -> world[0] = ops.get(0));
        if (!w) return true;

        //retrieve image name from the config file
        String imgName = (String) cfg("image-name");

        //read the image
        File file = new File("./plugins/ScreenPlugin/" + imgName);
        BufferedImage img;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //retrieve location from the config file
        List<Integer> location = (List<Integer>) cfg("screen-location");

        boolean l = useNSOption("l", "xyz must be valid coordinates", str -> Integer.valueOf(str), ops -> {
            location.set(0, ops.get(0));
            location.set(1, ops.get(1));
            location.set(2, ops.get(2));
        });
        if (!l) return true;

        int x = location.get(0);
        int y = location.get(1);
        int z = location.get(2);

        //set the width and height to user-specified values
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);

        final double[] satModifier = {1};

        boolean s = useNSOption("s", "Saturation level must be a valid number", str -> Double.valueOf(str), ops -> satModifier[0] = ops.get(0));
        if (!s) return true;

        //generate the image within the game
        //iterate through columns of the image
        for (float i = 0; i < img.getWidth(); i++) {
            //iterate through rows within the image
            for (float j = 0; j < img.getHeight(); j++) {
                //get the closest material to the color value of the pixel at (i, j) in the image
                Color col = new Color(img.getRGB((int)i, (int)j));
                float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                hsb[1] = (float) Math.pow(hsb[1], satModifier[0]);
                Material closest = Colors.closestBlock(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
                //set the block corresponding to the image pixel to the closest material, making sure it's within the user-defined width and height
                new Location(world[0], i*width/img.getWidth() + x, height - j*height/img.getHeight() + y, z).getBlock().setType(closest);
            }
        }

        reply(width + "x" + height + " image generated at " + x + ", " + y + ", " + z + " in " + world[0].getName());
        return true;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        System.out.println(arg);
        if (arg < 1) return new ArrayList<>();
        if (args[arg-1].equalsIgnoreCase("--w")) {
            //get list of world names
            List<String> names = new ArrayList<>();
            for (World world : getServer().getWorlds()) {
                names.add(world.getName());
            }
            return names;
        }
        return new ArrayList<>();
    }
}
