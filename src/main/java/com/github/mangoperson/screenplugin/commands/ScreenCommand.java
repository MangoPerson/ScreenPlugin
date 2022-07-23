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
        World w = sender.getServer().getWorlds().get(0);;

        //get world option
        String[] opW = oargs("w");

        //if world is specified
        if (opW.length > 0) {
            String wName = opW[0];
            //if the world specified is invalid, use the sender's world instead
            if (getServer().getWorld(wName) == null) {
                reply(wName + " does not exist. Using default world instead");
            } else {
                //set the executing world to the user-specified world
                w = getServer().getWorld(wName);;
            }
        }
        //if the world is not specified
        else {
            //set the executing world to the world that the sender is in
            if (sender instanceof Player) {
                w = ((Player) sender).getWorld();
            } else if (sender instanceof BlockCommandSender) {
                w = ((BlockCommandSender) sender).getBlock().getWorld();
            }
        }

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
        int x = location.get(0);
        int y = location.get(1);
        int z = location.get(2);

        //get location options
        String[] opL = oargs("l");

        //if location is specified
        if (opL.length > 0) {
            //make sure x, y, z are numbers
            if(!isNumber(opL[0]) || !isNumber(opL[1]) || !isNumber(opL[2])) {
                reply("x, y, and z must be valid coordinates");
                return true;
            }
            //rewrite values x, y, and z to the user-specified values
            x = Integer.parseInt(opL[0]);
            y = Integer.parseInt(opL[1]);
            z = Integer.parseInt(opL[2]);
        }

        //set the width and height to user-specified values
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);

        double satModifier = 1;

        //get saturation options
        String[] stL = oargs("s");

        //if saturation is specified
        if (stL.length > 0) {
            //make sure it's a positive number
            if (!isNumber(stL[0]) || stL[0].startsWith("-")) {
                reply("Saturation modifier must be a posistive number");
                return true;
            }
            //set the saturation modifier to the user-defined value
            satModifier = Double.parseDouble(stL[0]);
        }

        System.out.println(satModifier);

        //generate the image within the game
        //iterate through columns of the image
        for (float i = 0; i < img.getWidth(); i++) {
            //iterate through rows within the image
            for (float j = 0; j < img.getHeight(); j++) {
                //get the closest material to the color value of the pixel at (i, j) in the image
                Color col = new Color(img.getRGB((int)i, (int)j));
                float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                hsb[1] = (float) Math.pow(hsb[1], satModifier);
                Material closest = Colors.closestBlock(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
                //set the block corresponding to the image pixel to the closest material, making sure it's within the user-defined width and height
                new Location(w, i*width/img.getWidth() + x, height - j*height/img.getHeight() + y, z).getBlock().setType(closest);
            }
        }

        reply(width + "x" + height + " image generated at " + x + ", " + y + ", " + z + " in " + w.getName());
        return true;
    }

    @Override
    protected List<String> tabComplete(int arg) {
        if (arg < 1) return new ArrayList<>();
        if (args[arg-1] == "--w") {
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
