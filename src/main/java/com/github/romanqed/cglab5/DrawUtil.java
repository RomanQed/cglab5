package com.github.romanqed.cglab5;

import javafx.scene.paint.Color;

final class DrawUtil {
    static void drawSymmetrical(Point point, Point center, Color color, PointDrawer drawer) {
        drawer.draw(point.x, point.y, color);
        drawer.draw(-point.x + 2 * center.x, point.y, color);
        drawer.draw(point.x, -point.y + 2 * center.y, color);
        drawer.draw(-point.x + 2 * center.x, -point.y + 2 * center.y, color);
    }

    static void drawSymmetricalCircle(Point point, Point center, Color color, PointDrawer drawer) {
        double x = point.y - center.y + center.x;
        double y = point.x - center.x + center.y;
        double x1 = -point.y + center.y + center.x;
        double y1 = -point.x + center.x + center.y;
        drawer.draw(x, y, color);
        drawer.draw(x1, y, color);
        drawer.draw(x, y1, color);
        drawer.draw(x1, y1, color);
        drawSymmetrical(point, center, color, drawer);
    }
}
