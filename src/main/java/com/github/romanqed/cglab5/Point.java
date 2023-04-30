package com.github.romanqed.cglab5;

import java.util.Objects;

public final class Point {
    final double x;
    final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point move(double dx, double dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    public Point scale(Point center, double factor) {
        return new Point((x - center.x) * factor + center.x, (y - center.y) * factor + center.y);
    }

    public Point rotate(Point center, double angle) {
        double r_cos = Math.cos(Math.toRadians(angle));
        double r_sin = Math.sin(Math.toRadians(angle));
        double x = (this.x - center.x) * r_cos + (this.y - center.y) * r_sin + center.x;
        double y = -(this.x - center.x) * r_sin + (this.y - center.y) * r_cos + center.y;
        return new Point(x, y);
    }

    public Point subtract(Point p) {
        return new Point(x - p.x, y - p.y);
    }

    public double distance(Point p) {
        return Math.hypot(x - p.x, y - p.y);
    }

    // Signed area / determinant thing
    public double cross(Point p) {
        return x * p.y - y * p.x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return String.format("Point(%g, %g)", x, y);
    }
}
