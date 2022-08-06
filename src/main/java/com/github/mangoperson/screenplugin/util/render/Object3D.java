package com.github.mangoperson.screenplugin.util.render;

import com.github.mangoperson.screenplugin.util.MList;
import com.github.mangoperson.screenplugin.util.Pair;

public class Object3D {
    private MList<Vector3> points;
    private MList<int[]> edges;
    private MList<int[]> faces;
    public Vector3 offset;
    public double[] rotation;

    public void addPoint(Vector3 point) {
        points.add(point);
    }

    public void removePoint(Vector3 point) {
        points.remove(point);
    }

    public void addEdge(int point1, int point2) {
        edges.add(new int[] {point1, point2});
    }

    public void addEdge(Vector3 point1, Vector3 point2) {
        int p1 = points.indexOf(point1);
        int p2 = points.indexOf(point2);

        if (p1 == -1) {
            points.add(point1);
            addEdge(point1, point2);
            return;
        }
        if (p2 == -1) {
            points.add(point2);
            addEdge(point1, point2);
            return;
        }
        addEdge(p1, p2);
    }

    public void addFace(int point1, int point2, int point3) {
        faces.add(new int[] {point1, point2, point3});
    }

    public void addFace(Vector3 point1, Vector3 point2, Vector3 point3) {
        int p1 = points.indexOf(point1);
        int p2 = points.indexOf(point2);
        int p3 = points.indexOf(point3);

        if (p1 == -1) {
            points.add(point1);
            addFace(point1, point2, point3);
            return;
        }
        if (p2 == -1) {
            points.add(point2);
            addFace(point1, point2, point3);
            return;
        }
        if (p3 == -1) {
            points.add(point3);
            addFace(point1, point2, point3);
        }
        addFace(p1, p2, p3);
    }

    public void rotate(double x, double y, double z) {
        rotation[0] += x;
        rotation[1] += y;
        rotation[2] += z;
    }

    public void setRotation(double x, double y, double z) {
        rotation = new double[] {x, y, z};
    }

    public void translate(Vector3 offset) {
        this.offset = offset.add(offset);
    }

    public void setOffset(Vector3 offset) {
        this.offset = offset;
    }

    public MList<Vector3> getPoints() {
        return points;
    }

    public MList<int[]> getEdgePairs() {
        return edges;
    }

    public MList<int[]> getFaceTriplets() {
        return faces;
    }

    public MList<Pair<Vector3, Vector3>> getEdges() {
        return edges.map(e -> new Pair<>(points.get(e[0]), points.get(e[1])));
    }

    public MList<Vector3[]> getFaces() {
        return faces.map(f -> new Vector3[] {points.get(f[0]), points.get(f[1]), points.get(f[2])});
    }

    public MList<Vector3> getAbsolutePoints() {
        return points.map(p -> p.rotate(rotation[0], rotation[1], rotation[2]).add(offset));
    }

    public MList<Pair<Vector3, Vector3>> getAbsoluteEdges() {
        MList<Vector3> points = getAbsolutePoints();
        return edges.map(e -> new Pair<>(points.get(e[0]), points.get(e[1])));
    }

    public MList<Vector3[]> getAbsoluteFaces() {
        MList<Vector3> points = getAbsolutePoints();
        return faces.map(f -> new Vector3[] {points.get(f[0]), points.get(f[1]), points.get(f[2])});
    }
}
