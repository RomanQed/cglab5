package com.github.romanqed.cglab5;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


public class MainController implements Initializable {
    private final ActionPool pool = new ActionPool();

    @FXML
    protected Canvas canvas;
    @FXML
    protected ColorPicker fillPicker;

    @FXML
    protected TextField x;
    @FXML
    protected TextField y;

    private GraphicsContext context;
    private Set<Figure> figures;
    private List<Point> points;

    private void commit(Figure figure) {
        this.pool.add(new ActionImpl(figure));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillPicker.setValue(Color.RED);
        this.context = canvas.getGraphicsContext2D();
        this.figures = new HashSet<>();
        this.points = new ArrayList<>();
        this.refresh();
    }

    private void refresh() {
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        context.setLineWidth(1);
        drawFigures();
    }

    private <T> T parse(TextField textField, Function<String, T> parser) {
        try {
            return parser.apply(textField.getText());
        } catch (Throwable e) {
            return null;
        }
    }

    private Point getPoint(TextField xField, TextField yField) {
        Double x = parse(xField, Double::parseDouble);
        Double y = parse(yField, Double::parseDouble);
        if (x == null || y == null) {
            return null;
        }
        return new Point(x, y);
    }

    private void drawLine(Point start, Point end) {
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.strokeLine(start.x, start.y, end.x, end.y);
    }

    private void drawFigures() {
        for (Figure figure : figures) {
            List<Point> points = figure.points;
            int size = points.size();
            for (int i = 0; i < size - 1; ++i) {
                drawLine(points.get(i), points.get(i + 1));
            }
            drawLine(points.get(size - 1), points.get(0));
        }
    }

    @FXML
    protected void onFigureAdd() {
        int size = points.size();
        if (size < 3) {
            Util.showError("Ошибка", "Для замыкания фигуры необходимо не менее 3 точек");
            return;
        }
        commit(new Figure(points));
        this.points = new ArrayList<>();
    }

    @FXML
    protected void onDelayedFill() {
        this.refresh();
        context.setLineWidth(1);
        DrawBuffer buffer = new DrawBuffer();
        for (Figure figure : figures) {
            figure.draw(buffer, fillPicker.getValue(), () -> {
                buffer.delayedPush(context);
            });
        }
    }

    @FXML
    protected void onFill() {
        this.refresh();
        context.setLineWidth(1);
        DrawBuffer buffer = new DrawBuffer();
        long time = Util.calculateTime(() -> {
            for (Figure figure : figures) {
                figure.draw(buffer, fillPicker.getValue());
            }
        });
        buffer.flush(context);
        Util.showInfo("Время работы", "Время работы алгоритма: " + time + " (ns)");
    }

    @FXML
    protected void onExitAction() {
        System.exit(0);
    }

    @FXML
    protected void onAboutAction() throws IOException {
        Util.showInfo("О программе", Util.readResourceFile("about.txt"));
    }

    @FXML
    protected void onAuthorAction() {
        Util.showInfo("Автор", "Бакалдин Роман ИУ7-45Б");
    }

    @FXML
    protected void onCancelAction() {
        this.pool.undo();
    }

    @FXML
    protected void onRedoAction() {
        this.pool.redo();
    }

    @FXML
    protected void onResetAction() {
        this.pool.clear();
        this.points.clear();
        this.figures.clear();
        this.refresh();
    }

    private void drawPoint(Point point) {
        context.setStroke(Color.BLACK);
        context.strokeLine(point.x, point.y, point.x, point.y);
    }

    private void drawPoints(List<Point> points) {
        refresh();
        context.setLineWidth(4);
        points.forEach(this::drawPoint);
    }

    @FXML
    protected void onAdd() {
        Point toAdd = getPoint(x, y);
        if (toAdd == null) {
            Util.showError("Ошибка", "Введенные координаты точки не являются числом");
            return;
        }
        points.add(toAdd);
        drawPoints(points);
    }

    @FXML
    protected void onRemove() {
        int size = points.size();
        if (size == 0) {
            return;
        }
        points.remove(size - 1);
        drawPoints(points);
    }

    @FXML
    protected void onCanvasMouseClicked(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if (event.getButton() == MouseButton.PRIMARY) {
            points.add(new Point(x, y));
            drawPoints(points);
        } else {
            this.x.setText(Double.toString(x));
            this.y.setText(Double.toString(y));
        }
    }

    private final class ActionImpl implements Action {
        private final Figure backup;

        public ActionImpl(Figure backup) {
            this.backup = backup;
        }

        @Override
        public void perform() {
            figures.add(backup);
            refresh();
        }

        @Override
        public void undo() {
            figures.remove(backup);
            refresh();
        }
    }
}
