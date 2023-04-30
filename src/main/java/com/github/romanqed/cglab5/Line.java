package com.github.romanqed.cglab5;

import java.util.Objects;

public class Line {
    final Point start;
    final Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    public Line move(double dx, double dy) {
        return new Line(start.move(dx, dy), end.move(dx, dy));
    }

    public Line scale(Point center, double factor) {
        return new Line(start.scale(center, factor), end.scale(center, factor));
    }

    public Line rotate(Point center, double angle) {
        return new Line(start.rotate(center, angle), end.rotate(center, angle));
    }

    public boolean isPoint() {
        return start.equals(end);
    }

    public boolean isVertical() {
        return Double.compare(start.x, end.x) == 0;
    }

    public boolean isHorizontal() {
        return Double.compare(start.y, end.y) == 0;
    }

    public int getSide(Point point) {
        if (isVertical()) {
            return Double.compare(point.x, start.x);
        }
        if (isHorizontal()) {
            return Double.compare(start.y, point.y);
        }
        double check = (point.x - start.x) * (end.y - start.y) - (point.y - start.y) * (end.x - start.x);
        return Double.compare(check, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return start.equals(line.start) && end.equals(line.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "Line{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
