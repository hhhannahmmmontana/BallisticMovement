package com.volodya.ballisticmovement.view;

import com.volodya.ballisticmovement.types.FrameInfo;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public final class BallisticGraph extends StackPane {
    private static final double MEASUREMENT_LENGTH = 8;
    private static final double GRAPH_AXIS_WIDTH = 2;
    private static final double DEFAULT_POSITIVE_AXIS_MAXVALUE = 1;
    private static final double DEFAULT_NEGATIVE_AXIS_MAXVALUE = -1;
    private static final double GRAPH_EXPANSION = 1.05;
    private static final double X_TEXT_MARGIN = 3;
    private static final double Y_TEXT_MARGIN = 5;
    private static final double X_MAX_TEXT_MARGIN = 5;
    private static final double Y_MAX_TEXT_MARGIN = 3;
    private static final double MARGIN = 5;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color DATA_COLOR = Color.BLACK;
    private static final double OBJ_RADIUS = 5;

    private final double xStart;
    private final double xEnd;
    private final double yStart = 0;
    private final double yEnd;

    private final ArrayList<FrameInfo> marks = new ArrayList<>();

    public BallisticGraph(double maxDistance, double maxHeight) {
        this.setBackground(Background.fill(Color.TRANSPARENT));
        xStart = 0;
        if (maxDistance >= 0) {
            xEnd = Math.max(maxDistance * GRAPH_EXPANSION, DEFAULT_POSITIVE_AXIS_MAXVALUE);
        } else {
            xEnd = Math.min(maxDistance * GRAPH_EXPANSION, DEFAULT_NEGATIVE_AXIS_MAXVALUE);
        }
        yEnd = Math.max(maxHeight * GRAPH_EXPANSION, DEFAULT_POSITIVE_AXIS_MAXVALUE);
    }

    private Text getText(double value) {
        final var text = new Text(String.format("%.1f", value));
        text.setFont(new Font(10));
        text.setFill(DATA_COLOR);
        return text;
    }

    private Pane makeGraph() {
        final var graph = new Pane();
        graph.setPrefSize(getWidth(), getHeight());
        graph.setBackground(Background.fill(BACKGROUND_COLOR));
        final var xAxis = new Pane();
        final var yAxis = new Pane();

        xAxis.relocate(0, getHeight());
        yAxis.relocate(0, 0);

        final var xAxisLine = new Line(0, 0, getWidth() - GRAPH_AXIS_WIDTH, 0);
        xAxisLine.setStroke(DATA_COLOR);
        xAxisLine.setStrokeWidth(GRAPH_AXIS_WIDTH);
        StackPane.setAlignment(xAxisLine, Pos.BOTTOM_LEFT);

        final var xText = getText(xEnd);
        final Bounds xTextBounds = xText.getBoundsInParent();
        if (xEnd >= 0) {
            xText.relocate(
                    xAxisLine.getEndX() + X_MAX_TEXT_MARGIN,
                    xAxisLine.getStartY() - xTextBounds.getHeight() / 2
            );
        } else {
            xText.relocate(
                    0 - xTextBounds.getWidth() - X_MAX_TEXT_MARGIN,
                    xAxisLine.getStartY() - xTextBounds.getHeight() / 2
            );
        }
        xAxis.getChildren().add(xAxisLine);
        xAxis.getChildren().add(xText);

        final var yAxisLine = new Line(0, 0, 0, getHeight() - GRAPH_AXIS_WIDTH);;
        yAxisLine.setStroke(DATA_COLOR);
        yAxisLine.setStrokeWidth(GRAPH_AXIS_WIDTH);
        final var yText = getText(yEnd);
        final Bounds yBounds = yText.getBoundsInParent();
        yText.relocate(-yBounds.getWidth() / 2, -yBounds.getHeight() - Y_MAX_TEXT_MARGIN);
        if (xEnd < 0) {
            yText.setX(getWidth());
            yAxisLine.relocate(getWidth(), 0);
        }
        yAxis.getChildren().add(yAxisLine);
        yAxis.getChildren().add(yText);

        graph.getChildren().add(xAxis);
        graph.getChildren().add(yAxis);
        return graph;
    }

    public void adjustSize(Region root) {
        final double x = root.getWidth();
        final double y = root.getHeight();
        setMinSize(x - 2 * x / MARGIN, y - 2 * y / MARGIN);
        setMaxSize(x - 2 * x / MARGIN, y - 2 * y / MARGIN);
    }

    private double xMetersToPixels() {
        return getWidth() / Math.abs(xEnd - xStart);
    }
    private double yMetersToPixels() {
        return getHeight() / Math.abs(yEnd - yStart);
    }

    public void addMark(FrameInfo frame) {
        marks.add(frame);
    }
    private void applyMark(FrameInfo frame) {
        final double x = frame.getX();
        final double y = frame.getY();

        final double canvasX;
        if (x >= 0) {
            canvasX = x * xMetersToPixels();
        } else {
            canvasX = getWidth() + x * xMetersToPixels();
        }

        final double canvasY = getHeight() - y * yMetersToPixels();
        final var mark = new Pane();

        final var xMark = new Line(
                canvasX,
                getHeight() - MEASUREMENT_LENGTH,
                canvasX,
                getHeight() - 1
        );
        xMark.setStroke(DATA_COLOR);
        xMark.setStrokeWidth(GRAPH_AXIS_WIDTH);

        final var xText = getText(x);
        final Bounds xBounds = xText.getBoundsInParent();
        xText.relocate(xMark.getStartX() - xBounds.getWidth() / 2, xMark.getEndY() + X_TEXT_MARGIN);

        mark.getChildren().add(xMark);
        mark.getChildren().add(xText);

        final Line yMark;
        if (x >= 0) {
             yMark = new Line(
                    1,
                    canvasY,
                    MEASUREMENT_LENGTH,
                    canvasY
            );
        } else {
            yMark = new Line(
                    getWidth() - MEASUREMENT_LENGTH,
                    canvasY,
                    getWidth() - 1,
                    canvasY
            );
        }
        yMark.setStroke(DATA_COLOR);
        yMark.setStrokeWidth(GRAPH_AXIS_WIDTH);

        final var yText = getText(y);
        Bounds yBounds = yText.getBoundsInParent();
        if (x >= 0) {
            yText.relocate(yMark.getStartX() - yBounds.getWidth() - Y_TEXT_MARGIN, yMark.getStartY() - yBounds.getHeight() / 2);
        } else {
            yText.relocate(yMark.getEndX() + GRAPH_AXIS_WIDTH + Y_TEXT_MARGIN, yMark.getStartY() - yBounds.getHeight() / 2);
        }

        mark.getChildren().add(yMark);
        mark.getChildren().add(yText);

        this.getChildren().add(mark);
    }

    public void setFrame(ArrayList<FrameInfo> frames, int totalFramesAmount) {
        getChildren().clear();
        final var graph = makeGraph();
        getChildren().add(graph);

        final int trajectoryQuantity = 150;
        final int trajectorySkip = Math.max(totalFramesAmount / trajectoryQuantity, 1);
        final double xMetersToPixels = xMetersToPixels();
        final double yMetersToPixels = yMetersToPixels();

        final var simulation = new Pane();

        final var obj = new Circle(OBJ_RADIUS, DATA_COLOR);
        BallisticGraph.setAlignment(obj, Pos.TOP_LEFT);
        simulation.getChildren().add(obj);

        final double negative_k;
        if (frames.getLast().getX() >= 0) {
            negative_k = 0;
        } else {
            negative_k = getWidth();
        }

        for (int i = trajectorySkip; i < frames.size(); i += 2 * trajectorySkip) {
            if (getHeight() - frames.get(i).getY() * yMetersToPixels <= getHeight()) {
                Line line = new Line(
                        frames.get(i - trajectorySkip).getX() * xMetersToPixels + negative_k,
                        getHeight() - frames.get(i - trajectorySkip).getY() * yMetersToPixels,
                        frames.get(i).getX() * xMetersToPixels + negative_k,
                        getHeight()- frames.get(i).getY() * yMetersToPixels
                );
                line.setStroke(DATA_COLOR);
                simulation.getChildren().add(line);
            }
        }

        if (frames.getLast().getY() > 0 || frames.size() <= 1) {
            obj.setCenterX(frames.getLast().getX() * xMetersToPixels + negative_k);
            obj.setCenterY(getHeight() - (frames.getLast().getY() * yMetersToPixels));
            if (frames.size() > 1) {
                applyMark(frames.getLast());
            }
        } else {
            double xPath = frames.get(frames.size() - 2).getX() - frames.getLast().getX();
            double yPath = frames.get(frames.size() - 2).getY() - frames.getLast().getY();
            double k = (frames.get(frames.size() - 2).getY() - (OBJ_RADIUS / yMetersToPixels)) / yPath;
            double new_x = frames.get(frames.size() - 2).getX() + xPath * k;
            applyMark(new FrameInfo(new_x, 0, 0));
            obj.setCenterX(new_x * xMetersToPixels + negative_k);
            obj.setCenterY(getHeight() - OBJ_RADIUS);
        }

        for (var m : marks) {
            applyMark(m);
        }

        BallisticGraph.setAlignment(simulation, Pos.TOP_LEFT);
        getChildren().add(simulation);
    }
}
