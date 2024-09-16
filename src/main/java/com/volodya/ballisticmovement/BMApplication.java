package com.volodya.ballisticmovement;

import com.volodya.ballisticmovement.types.FrameInfo;
import com.volodya.ballisticmovement.view.BallisticGraph;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class BMApplication extends Application {
    final static double WINDOW_WIDTH = 666.666;
    final static double WINDOW_HEIGHT = 500;

    @Override
    public void start(Stage stage) {
        double angle = 60;
        double height = 100;
        double velocity = 1000;
        int fps = 10;

        BallisticModel model = new BallisticModel(angle, height, velocity, fps);
        var graph = new BallisticGraph(model.calculateTheoreticalDistance(), model.calculateTheoreticalHeight());
        StackPane.setAlignment(graph, Pos.TOP_CENTER);
        StackPane.setMargin(graph, new Insets(10, 0, 0, 0));

        StackPane root = new StackPane(graph);
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.setBackground(Background.fill(Color.LIGHTGRAY));
        graph.adjustSize(root);
        graph.setFrame(model.frames, model.calculateMaxFramesAmount());

        AnimationTimer animation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (model.isFlying()) {
                    model.applyVelocity();
                    if (model.peak()) {
                        graph.addMark(model.getPrevFrame());
                    }
                }
                graph.adjustSize(root);
                graph.setFrame(model.frames, model.calculateMaxFramesAmount());
            }
        };

        animation.start();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}