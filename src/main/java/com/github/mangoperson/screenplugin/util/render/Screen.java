package com.github.mangoperson.screenplugin.util.render;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen {
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private int width;
    private int height;

    public Screen(World world, int x, int y, int z, int width, int height) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
    }

    public void draw(BufferedImage img, double saturation) {
        for (float i = 0; i < img.getWidth(); i++) {
            //iterate through rows within the image
            for (float j = 0; j < img.getHeight(); j++) {
                //get the closest material to the color value of the pixel at (i, j) in the image
                Color col = new Color(img.getRGB((int)i, (int)j));
                float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                hsb[1] = (float) Math.pow(hsb[1], saturation);
                //set the block corresponding to the image pixel to the closest material, making sure it's within the user-defined width and height
                drawPixel(col, x + i * width/img.getWidth(), y + i * height/ img.getHeight(), saturation);
            }
        }
    }

    public void drawPixel(Color color, float x, float y, double saturation) {
        drawPixel(Colors.closestBlock(color), x, y);
    }

    public void drawPixel(Material block, float x, float y) {
        new Location(world, this.x + x, this.y + y, z).getBlock().setType(block);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public World getWorld() {
        return world;
    }

    public int[] getLocation() {
        return new int[] {x, y, z};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
