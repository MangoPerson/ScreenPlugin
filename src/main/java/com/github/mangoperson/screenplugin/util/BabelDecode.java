package com.github.mangoperson.screenplugin.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BabelDecode {
    public static int[] idsFromFile(String name) {
        Path p = Paths.get("./plugins/ScreenPlugin/" + name);

        byte[] bytes;
        try {
            bytes = Files.readAllBytes(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String s = bytes2bin(bytes);
        int[] ids = new int[s.length()/10];

        for (int i = 0; i < ids.length; i++) {
            ids[i] = Integer.parseUnsignedInt(s.substring(10 * i, 10 * (i + 1)), 2);
        }

        return ids;
    }

    static String bytes2bin(byte[] bytes) {
        String binary = "";

        for (byte b : bytes) {
            String s = fillfront(Integer.toBinaryString(Byte.toUnsignedInt(b)), '0', 8);
            binary += s;
        }

        return binary;
    }

    static String fillfront(String s, char fill, int length) {
        while (true) {
            if (s.length() >= length) {
                return s;
            } else {
                s = fill + s;
            }
        }
    }
}
