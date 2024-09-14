package com.volodya.ballisticmovement.view;

import javafx.scene.Group;
import javafx.stage.Stage;

public final class BallisticGraph {
    private final double MEASUREMENT_WIDTH = 1;
    private final double MEASUREMENT_LENGTH = 4;
    private final double GRAPH_AXIS_WIDTH = 2;
    private final double DEFAULT_AXIS_MAXVALUE = 1;

    private final Stage stageReference;
    private final double minX;
    private final double maxX;
    private final double minY = 0;
    private final double maxY;

    private double xShare;
    private double yShare;

    public BallisticGraph(Stage stage, double maxDistance, double maxHeight) {
        stageReference = stage;
        if (maxDistance > 0) {
            minX = 0;
            maxX = maxDistance;
        } else {
            minX = maxDistance;
            maxX = 0;
        }
        maxY = Math.max(maxHeight * 1.3, DEFAULT_AXIS_MAXVALUE);
    }

    Group getFrame() {
        final double GRAPH_X_SIZE = stageReference.getMaxWidth() - 2 * stageReference.getMaxWidth() / 10;
        final double X_MEASUREMENT_DIST = GRAPH_X_SIZE / 50;

        final double GRAPH_Y_SIZE = stageReference.getMaxHeight() - 2 * stageReference.getMaxHeight() / 10;

        var group = new Group();
        return group;
    }
}
