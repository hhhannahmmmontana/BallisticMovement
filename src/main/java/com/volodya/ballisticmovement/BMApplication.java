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
        BallisticModel model = new BallisticModel(45, 2, 10);
        Text textField = new Text(Double.toString(model.getLastFrame().getVelocity()));
        AnimationTimer anim = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (model.isFlying()) {
                    model.applyVelocity();
                    textField.setText(Double.toString(model.getLastFrame().getVelocity()));
                }
            }
        };
        anim.start();
        final var scene = new Scene(new StackPane(textField), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}