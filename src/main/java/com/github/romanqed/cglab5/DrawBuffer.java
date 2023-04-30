package com.github.romanqed.cglab5;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class DrawBuffer {
    private final Map<Point, Color> pixels;

    public DrawBuffer() {
        this.pixels = new HashMap<>();
    }

    private DrawBuffer(Map<Point, Color> pixels) {
        this.pixels = pixels;
    }

    public Color getPixel(Point point, Color def) {
        return this.pixels.putIfAbsent(point, def);
    }

    public Color getPixel(Point point) {
        return getPixel(point, Color.WHITE);
    }

    public void setPixel(Point point, Color color) {
        this.pixels.put(point, color);
    }

    private static int round(double n) {
        if (n - (int) n < 0.5) {
            return (int) n;
        }
        return (int) (n + 1);
    }

    public DrawBuffer copy() {
        return new DrawBuffer(pixels);
    }

    public void drawLine(Point start, Point end, Color color) {
        // Calculate dx and dy
        double dx = end.x - start.x;
        double dy = end.y - start.y;

        // If dx > dy we will take step as dx
        // else we will take step as dy to draw the complete
        // line
        double step = Math.max(Math.abs(dx), Math.abs(dy));

        // Calculate x-increment and y-increment for each step
        double x_incr = (float) dx / step;
        double y_incr = (float) dy / step;

        // Take the initial points as x and y
        double x = start.x;
        double y = start.y;

        for (int i = 0; i < step; ++i) {
            setPixel(new Point(round(x), round(y)), color);
            x += x_incr;
            y += y_incr;
        }
    }

    public void delayedPush(GraphicsContext context) {
        Timeline timeline = new Timeline();
        Duration timepoint = Duration.ZERO;
        Duration pause = Duration.millis(1);
        for (Map.Entry<Point, Color> pixel : pixels.entrySet()) {
            timepoint = timepoint.add(pause);
            KeyFrame keyFrame = new KeyFrame(timepoint, e -> {
                context.setStroke(pixel.getValue());
                Point point = pixel.getKey();
                context.strokeLine(point.x, point.y, point.x, point.y);
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }

    public void push(GraphicsContext context) {
        for (Map.Entry<Point, Color> pixel : pixels.entrySet()) {
            context.setStroke(pixel.getValue());
            Point point = pixel.getKey();
            context.strokeLine(point.x, point.y, point.x, point.y);
        }
    }

    public void flush(GraphicsContext context) {
        push(context);
        pixels.clear();
    }

    public void clear() {
        pixels.clear();
    }
}
