package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.SCommand;
import com.github.mangoperson.screenplugin.util.render.Screen;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScreenCommand extends SCommand {

    private static final MList<Screen> screens = new MList<>();

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

        boolean l = useNSOption("l", "xyz must be valid coordinates", Integer::valueOf, ops -> {
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

        boolean s = useNSOption("s", "Saturation level must be a valid number", Double::valueOf, ops -> satModifier[0] = ops.get(0));
        if (!s) return true;

        if (screens.filter(screen -> (screen.getX() == x && screen.getY() == y && screen.getZ() == z)).size() > 0) {
            screens.map(screen -> {
                if (screen.getX() == x && screen.getY() == y && screen.getZ() == z) {
                    screen.setSize(width, height);
                    screen.draw(img, satModifier[0]);
                }
                return screen;
            });
        }
        else {
            Screen screen = new Screen(world[0], x, y, z, width, height);
            screen.draw(img, satModifier[0]);
            screens.add(screen);
        }

        reply(width + "x" + height + " image generated at " + x + ", " + y + ", " + z + " in " + world[0].getName());
        return true;
    }

    @Override
    protected MList<String> tabComplete(int arg) {
        if (arg < 1) return new MList<>();
        if (args[arg-1].equalsIgnoreCase("--w")) {
            //get list of world names
            return getServer().getWorlds().stream()
                    .map(WorldInfo::getName)
                    .collect(MList.toMList());
        }
        return new MList<>();
    }
}
