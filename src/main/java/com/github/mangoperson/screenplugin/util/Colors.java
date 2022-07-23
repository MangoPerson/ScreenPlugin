package com.github.mangoperson.screenplugin.util;

import org.bukkit.Material;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Colors {
    public static HashMap<Material, Color> getColorMap() {
        HashMap<Material, Color> colorMap = new HashMap<>();

        //define average RGB for all concrete and terracotta blocks
        colorMap.put(Material.WHITE_CONCRETE, c(208, 214 ,214));
        colorMap.put(Material.RED_CONCRETE, c(143, 33, 33));
        colorMap.put(Material.PURPLE_CONCRETE, c(100, 32, 157));
        colorMap.put(Material.PINK_CONCRETE, c(214, 101, 143));
        colorMap.put(Material.ORANGE_CONCRETE, c(225, 98, 0));
        colorMap.put(Material.MAGENTA_CONCRETE, c(169, 48, 159));
        colorMap.put(Material.LIME_CONCRETE, c(93, 168, 24));
        colorMap.put(Material.LIGHT_GRAY_CONCRETE, c(125, 125, 115));
        colorMap.put(Material.LIGHT_BLUE_CONCRETE, c(35, 135, 200));
        colorMap.put(Material.GREEN_CONCRETE, c(74, 92, 37));
        colorMap.put(Material.GRAY_CONCRETE, c(55, 58, 62));
        colorMap.put(Material.CYAN_CONCRETE, c(22, 120, 127));
        colorMap.put(Material.BROWN_CONCRETE, c(96, 59, 31));
        colorMap.put(Material.BLUE_CONCRETE, c(45, 47, 144));
        colorMap.put(Material.BLACK_CONCRETE, c(7, 9, 10));

        colorMap.put(Material.TERRACOTTA, c(152, 95, 69));
        colorMap.put(Material.WHITE_TERRACOTTA, c(184,131, 34));
        colorMap.put(Material.RED_TERRACOTTA, c(141, 59, 46));
        colorMap.put(Material.PURPLE_TERRACOTTA, c(118, 70, 86));
        colorMap.put(Material.PINK_TERRACOTTA, c(160, 75, 74));
        colorMap.put(Material.ORANGE_TERRACOTTA, c(163, 85, 39));
        colorMap.put(Material.MAGENTA_TERRACOTTA, c(149, 86, 107));
        colorMap.put(Material.LIME_TERRACOTTA, c(107, 121, 55));
        colorMap.put(Material.LIGHT_GRAY_TERRACOTTA, c(138, 109, 98));
        colorMap.put(Material.LIGHT_BLUE_TERRACOTTA, c(116, 110, 139));
        colorMap.put(Material.GREEN_TERRACOTTA, c(75, 82, 41));
        colorMap.put(Material.GRAY_TERRACOTTA, c(58, 42, 36));
        colorMap.put(Material.CYAN_TERRACOTTA, c(86, 90, 91));
        colorMap.put(Material.BROWN_TERRACOTTA, c(76, 50, 35));
        colorMap.put(Material.BLUE_TERRACOTTA, c(73, 58, 90));
        colorMap.put(Material.BLACK_TERRACOTTA, c(39, 24, 18));

        return colorMap;
    }

    //find closest average color to a given color
    public static Material closestBlock(Color col) {
        //set record low difference to the maximum (255*3)
        int lowest = 765;
        Material lowestMat = null;

        //iterate through the material-color map
        for (Map.Entry<Material, Color> pair : getColorMap().entrySet()) {
            //find the difference between the color of the block and the given color
            Color mCol = pair.getValue();
            int diff = Math.abs(col.getRed() - mCol.getRed())
                    + Math.abs(col.getGreen() - mCol.getGreen())
                    + Math.abs(col.getBlue() - mCol.getBlue());
            //if the difference is lower than the previous lowest difference, set the new lowest and material to this one
            if (diff < lowest) {
                lowest = diff;
                lowestMat = pair.getKey();
            }
        }

        //return the material with the lowest color difference from the given color
        return lowestMat;
    }

    //convenience function for easier initialization of colors in the hashmap
    private static Color c(int red, int green, int blue) {
        return new Color(red, green, blue);
    }
}
