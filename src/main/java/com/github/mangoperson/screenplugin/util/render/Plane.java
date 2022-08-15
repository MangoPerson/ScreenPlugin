package com.github.mangoperson.screenplugin.util.render;

public class Plane {
    public double xs;
    public double ys;
    public double zInt;

    public Plane(double xs, double ys, double zInt) {
        this.xs = xs;
        this.ys = ys;
        this.zInt = zInt;
    }

    public Plane(Vector3 p, double slopeX, double slopeY) {
        xs = slopeX;
        ys = slopeY;
        zInt = p.z - xs * p.x - ys * p.y;
    }

    public Vector3 alignToZero(Vector3 v) {
        v.z -= zInt;
        return v.rotate(Math.atan(-xs), Math.atan(-ys), 0);
    }

    public Vector3 alignFromZero(Vector3 v) {
        v = v.rotate(Math.atan(xs), Math.atan(ys), 0);
        v.z += zInt;
        return v;
    }

    public double xy(double x, double y) {
        return xs * x + ys * y + zInt;
    }

    public double xyz(double x, double y, double z) {
        return xs * x + ys * y + zInt - z;
    }
}
