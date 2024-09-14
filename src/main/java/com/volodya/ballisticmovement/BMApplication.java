package com.volodya.ballisticmovement;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class BMApplication extends Application {
    final static double WINDOW_WIDTH = 666.666;
    final static double WINDOW_HEIGHT = 500;
    static int FPS = 60;

    @Override
    public void start(Stage stage) throws Exception {
        double velocity = 10;
        double height = 0;
        double angle = Math.toRadians(125);
        int extraSpace = 10;
        BallisticModel model = new BallisticModel(angle, height, velocity);

        var xAxis = new NumberAxis();
        xAxis.setLabel("x, м");
        xAxis.setAutoRanging(false);
        if (model.isXVelocityPositive()) {
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(Math.ceil(model.calculateTheoreticalDistance() * 1.3) + extraSpace);
        } else {
            xAxis.setLowerBound(Math.ceil(model.calculateTheoreticalDistance() * 1.3) + extraSpace);
            xAxis.setUpperBound(0);
        }

        var yAxis = new NumberAxis();
        yAxis.setLabel("y, м");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(Math.ceil(model.calculateTheoreticalHeight() * 1.3) + extraSpace);

        var graph = new LineChart<>(xAxis, yAxis);
        graph.setCreateSymbols(false);
        graph.setLegendVisible(false);
        var dots = new XYChart.Series<Number, Number>();
        graph.getData().add(dots);

        dots.getData().add(new XYChart.Data<>(model.getX(), model.getY()));
        AnimationTimer anim = new AnimationTimer() {
            double frame = 0;
            @Override
            public void handle(long l) {
                if (model.isFlying()) {
                    model.applyVelocity();
                    dots.getData().add(new XYChart.Data<>(model.getX(), model.getY()));
                    ++frame;
                } else {
                    System.out.println("[" + model.getExperimentalTimeLowerBound() + ", " + model.getExperimentalTimeUpperBound() + "]");
                }
                System.out.println();
            }
        };
        anim.start();
        final var scene = new Scene(new StackPane(graph), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}