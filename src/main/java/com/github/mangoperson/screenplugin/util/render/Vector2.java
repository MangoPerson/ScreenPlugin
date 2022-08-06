package com.github.mangoperson.screenplugin.util.render;

import java.util.function.BinaryOperator;

public class Vector2 {
    public double x;
    public double y;

    public Vector2() {
        new Vector2(0, 0);
    }

    public Vector2(double x, double t) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 multiply(Vector3 other) {
        return new Vector2(x * other.x, y * other.y);
    }

    public Vector2 operation(Vector2 other, BinaryOperator<Double> operator) {
        return new Vector2(operator.apply(x, other.x), operator.apply(y, other.y));
    }

    public Vector2 scale(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector2 normalized() {
        return scale(1 / length());
    }

    public Vector2 rotate(double angle) {
        return new Vector2(
                x * Math.cos(angle) - y * Math.sin(angle),
                x * Math.sin(angle) + y * Math.cos(angle)
        );
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 v = (Vector2) o;
        double low = 1E-15d;
        return Math.abs(x - v.x) < low && Math.abs(y - v.y) < low;
    }
}
