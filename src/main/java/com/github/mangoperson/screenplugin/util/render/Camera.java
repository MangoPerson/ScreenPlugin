package com.github.mangoperson.screenplugin.util.render;

public class Camera {
    public Vector3 position;
    public Vector3 rotation;
    public Mode mode;
    public double scale;

    public Camera(Vector3 position, Vector3 rotation, Mode mode, double scale) {
        this.position = position;
        this.rotation = rotation;
        this.mode = mode;
        this.scale = scale;
    }

    enum Mode {
        ORTHOGRAPHIC,
        PERSPECTIVE
    }
}
