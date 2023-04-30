package com.github.romanqed.cglab5;

import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

public class Figure {
    final List<Point> points;

    public Figure(List<Point> points) {
        this.points = Collections.unmodifiableList(points);
    }

    public void draw(DrawBuffer buffer, Color color) {
        draw(buffer, color, () -> {});
    }

    private static double calculateBarrier(List<Point> figure) {
        double curMax = figure.get(0).x;
        double curMin = figure.get(0).x;
        for (int i = 1; i < figure.size(); ++i) {
            Point point = figure.get(i);
            if (point.x > curMax) {
                curMax = point.x;
            }
            if (point.x < curMin) {
                curMin = point.x;
            }
        }
        return (curMax + curMin) / 2;
    }

    private static int sign(double a, double b) {
        if (a > b) {
            return -1;
        }
        return 1;
    }

    public void draw(DrawBuffer buffer, Color color, Runnable runnable) {
        double barrier = calculateBarrier(points);
        double prevDy = 0, firstDy = 0;
        int currSize = points.size();
        for (int i = 0; i < currSize; ++i) {
            double xCur = points.get(i).x, xNext = points.get((i + 1) % currSize).x;
            double yCur = points.get(i).y, yNext = points.get((i + 1) % currSize).y;
            if (yCur == yNext) {
                continue;
            }
            double dy = sign(yCur, yNext);
            if (i == 0) {
                firstDy = dy;
            }
            double dx = (xNext - xCur) / Math.abs(yNext - yCur);
            double x = xCur;
            if (yNext > yCur) {
                ++yNext;
                if (dy == prevDy) {
                    ++yCur;
                }
                if (i == currSize - 1 && dy == firstDy) {
                    --yNext;
                }
            }
            if (yNext < yCur) {
                --yNext;
                if (dy == prevDy) {
                    --yCur;
                }
                if (i == currSize - 1 && dy == firstDy) {
                    ++yNext;
                }
            }
            prevDy = dy;
            for (double j = yCur; (j >= yCur && j < yNext) || (j <= yCur && j > yNext); j += dy, x += dx) {
                double start;
                double end;
                if (x < barrier) {
                    start = Math.floor(x);
                    end = barrier;
                } else {
                    start = barrier;
                    end = Math.ceil(x);
                }
                for (; start < end; ++start) {
                    Point pixel = new Point(start, j);
                    if (buffer.getPixel(pixel) == color) {
                        buffer.setPixel(pixel, Color.WHITE);
                    } else {
                        buffer.setPixel(pixel, color);
                    }
                }
            }
            runnable.run();
        }
    }
}
