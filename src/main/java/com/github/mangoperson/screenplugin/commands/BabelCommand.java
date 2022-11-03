package com.github.mangoperson.screenplugin.commands;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.BabelDecode;
import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.SCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BabelCommand extends SCommand {
    public BabelCommand() {
        super("babel");
        addSubCommand("load", new SCommand("load") {
            @Override
            protected boolean run() {
                World w;
                int lx;
                int ly;
                int lz;

                if (sender instanceof Player p) {
                    Location l = p.getLocation();
                    lx = (int) l.getX() + 2;
                    ly = (int) l.getY();
                    lz = (int) l.getZ();
                    w = l.getWorld();
                } else if (sender instanceof BlockCommandSender b) {
                    Location l = b.getBlock().getLocation();
                    lx = (int) l.getX() + 2;
                    ly = (int) l.getY();
                    lz = (int) l.getZ();
                    w = l.getWorld();
                } else {
                    reply("Command must be run by a player or command block");
                    return true;
                }

                int[] ids = BabelDecode.idsFromFile(args[0]);

                for (int i = 0; i < ids.length; i++) {
                    String block = ScreenPlugin.getIds().get(ids[i]);
                    int x = lx + i % 16;
                    int y = ly + Math.floorDiv(i, 256);
                    int z = lz + Math.floorDiv(i, 16) % 16;

                    new Location(w, x, y, z).getBlock().setType(Material.valueOf(block));
                }
                return true;
            }

            @Override
            protected boolean checkArgs() {
                return args.length == 1;
            }
        });

        addSubCommand("save", new SCommand("save") {
            @Override
            protected boolean run() {
                World w;
                Location l;

                if (sender instanceof Player p) {
                    l = p.getLocation();
                    w = l.getWorld();
                } else if (sender instanceof BlockCommandSender b) {
                    l = b.getBlock().getLocation();
                    w = l.getWorld();
                } else {
                    reply("Command must be run by a player or command block");
                    return true;
                }

                Path p = Paths.get("./plugins/ScreenPlugin/" + args[0]);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(p.toFile()));

                    List<Integer> ids = getBlocksIn(w, l, l.add(16, 16, 16))
                            .map(b -> ScreenPlugin.getIds().indexOf(b.getType().name()));


                    ids.forEach(i -> {
                        try {
                            writer.append(String.valueOf(i)).append("\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return true;
            }

            @Override
            protected boolean checkArgs() {
                return args.length == 1;
            }
        });
    }

    @Override
    protected boolean run() {
        return true;
    }

    public static MList<Block> getBlocksIn(World world, Location p1, Location p2) {
        MList<Block> blocks = new MList<>();
        double x1 = p1.getX();
        double y1 = p1.getY();
        double z1 = p1.getZ();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double z2 = p2.getZ();

        for (int y = 0; y < y2 - y1; y++) {
            for (int z = 0; z < z2 - z1; z++) {
                for (int x = 0; x < x2 - x1; x++) {
                    Block b = new Location(world, x+x1, y+y1, z+z1).getBlock();
                    blocks.add(b);
                }
            }
        }

        return blocks;
    }
}
