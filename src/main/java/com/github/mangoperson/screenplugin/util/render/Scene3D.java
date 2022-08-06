package com.github.mangoperson.screenplugin.util.render;

import com.github.mangoperson.screenplugin.util.MList;

import java.awt.image.BufferedImage;

public class Scene3D {
    public final MList<Object3D> objects = new MList<>();
    public final MList<Light> lights = new MList<>();
    private Camera camera;

    public BufferedImage render(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
