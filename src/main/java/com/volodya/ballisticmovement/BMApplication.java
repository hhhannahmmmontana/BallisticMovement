package com.volodya.ballisticmovement;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public final class BMApplication extends Application {
    final static double WINDOW_WIDTH = 666.666;
    final static double WINDOW_HEIGHT = 500;
    final static int APPROX_FPS = 60;

    @Override
    public void start(Stage stage) throws Exception {
        double velocity = 10;
        double height = 0;
        double angle = Math.toRadians(45);

        var xAxis = new NumberAxis();
        xAxis.setLabel("x, м");
        xAxis.setAutoRanging(false);
        double w = (Math.pow(velocity, 2) * Math.sin(2 * angle)) / Physics.G;
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(w + w / 3);

        var yAxis = new NumberAxis();
        yAxis.setLabel("y, м");
        yAxis.setAutoRanging(false);
        double h = (Math.pow(velocity, 2) * Math.pow(Math.sin(angle), 2)) / (2 * Physics.G);
        yAxis.setUpperBound(Math.ceil(h + h / 3));
        yAxis.setLowerBound(0);

        var graph = new LineChart<>(xAxis, yAxis);
        graph.setCreateSymbols(false);
        graph.setLegendVisible(false);
        graph.getXAxis().setAnimated(false);
        var dots = new XYChart.Series<Number, Number>();
        graph.getData().add(dots);

        BallisticModel model = new BallisticModel(angle, height, velocity);
        dots.getData().add(new XYChart.Data<>(model.getLastFrame().getX(), model.getLastFrame().getY()));
        AnimationTimer anim = new AnimationTimer() {
            double frame = 0;
            @Override
            public void handle(long l) {
                if (model.isFlying()) {
                    model.applyVelocity();
                    dots.getData().add(new XYChart.Data<>(model.getLastFrame().getX(), model.getLastFrame().getY()));
                    ++frame;
                }
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