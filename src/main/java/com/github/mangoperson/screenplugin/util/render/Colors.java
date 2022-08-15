package com.github.mangoperson.screenplugin.util.render;

import com.github.mangoperson.screenplugin.ScreenPlugin;
import com.github.mangoperson.screenplugin.util.MMap;
import com.github.mangoperson.screenplugin.util.Pair;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.bukkit.Material;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Colors {
    public static MMap<Material, Color> getColorMap() {
        CSVReader reader = new CSVReader(new InputStreamReader(Colors.class.getClassLoader().getResourceAsStream("colormap.csv")));
        try {
            return reader.readAll().stream()
                    .map(a -> new Pair<>(Material.valueOf(a[0]), c(
                                Integer.parseInt(a[1].replace(" ", "")),
                                Integer.parseInt(a[2].replace(" ", "")),
                                Integer.parseInt(a[3].replace(" ", ""))))
                    )
                    .collect(MMap.toMMap());
        } catch(IOException | CsvException e) {
            e.printStackTrace();
        }

        return new MMap<>();
    }

    //find closest average color to a given color
    public static Material closestBlock(Color col) {
        return ScreenPlugin.getColorMap()
                .map((m, c) -> new Pair<>(m, Math.abs(col.getRed() - c.getRed())
                        + Math.abs(col.getGreen() - c.getGreen())
                        + Math.abs(col.getBlue() - c.getBlue())))
                .min((Comparator.comparingInt(t -> t.b)))
                .get().a;
    }

    //convenience function for easier initialization of colors in the hashmap
    private static Color c(int red, int green, int blue) {
        return new Color(red, green, blue);
    }
}
