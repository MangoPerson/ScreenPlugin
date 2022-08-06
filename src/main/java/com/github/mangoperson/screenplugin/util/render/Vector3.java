package com.github.mangoperson.screenplugin.util.render;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Vector3 {
    public double x;
    public double y;
    public double z;

    public Vector3() {
        new Vector3(0, 0, 0);
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return add(other.scale(-1));
    }

    public Vector3 multiply(Vector3 other) {
        return new Vector3(x * other.x, y * other.y, z * other.z);
    }

    public Vector3 operation(Vector3 other, BinaryOperator<Double> operator) {
        return new Vector3(operator.apply(x, other.x), operator.apply(y, other.y), operator.apply(z, other.z));
    }

    public Vector3 scale(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public Vector3 normalized() {
        return scale(1 / length());
    }

    public Vector3 rotate(double x, double y, double z) {
        return rotate(Axis.X, x)
                .rotate(Axis.Y, y)
                .rotate(Axis.Z, z);
    }

    public Vector3 rotate(Axis axis, double angle) {
        switch(axis) {
            case X:
                return new Vector3(
                        x,
                        y * Math.cos(angle) - z * Math.sin(angle),
                        y * Math.sin(angle) + z * Math.cos(angle)
                );
            case Y:
                return new Vector3(
                        x * Math.cos(angle) - z * Math.sin(angle),
                        y,
                        x * Math.sin(angle) + z * Math.cos(angle)
                );
            case Z:
                return new Vector3(
                        x * Math.cos(angle) - y * Math.sin(angle),
                        x * Math.sin(angle) + y * Math.cos(angle),
                        z
                );
        }
        return this;
    }

    public Optional<Vector3> raymarch(Vector3 d, Plane plane, Vector3 original) {
        if (subtract(original).length() > 100) return Optional.empty();

        Vector3 s = add(d);
        double current = plane.xyz(x, y, z);
        double result = plane.xyz(s.x, s.y, s.z);
        double sd = Math.abs(Math.signum(result) - Math.signum(current));

        if (sd == 1) {
            return Optional.of(this);
        }
        else if (sd == 0) {
            return s.raymarch(d, plane, original);
        }
        else {
            return s.raymarch(d.scale(-.5), plane, original);
        }
    }

    enum Axis {
        X,
        Y,
        Z
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 v = (Vector3) o;
        double low = 1E-15d;
        return Math.abs(x - v.x) < low && Math.abs(y - v.y) < low && Math.abs(z - v.z) < low;
    }
}
