package com.volodya.ballisticmovement;

import com.volodya.ballisticmovement.view.BallisticGraph;
import com.volodya.ballisticmovement.view.GraphsStage;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public final class BMApplication extends Application {
    final static double WINDOW_WIDTH = 666.666;
    final static double WINDOW_HEIGHT = 500;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Баллистическое движение");

        final var model = new BallisticModel(45, 2, 30, 60);
        final var graph = new BallisticGraph(model.calculateTheoreticalDistance(), model.calculateTheoreticalHeight());
        StackPane.setAlignment(graph, Pos.TOP_CENTER);
        StackPane.setMargin(graph, new Insets(20, 0, 0, 0));

        final var root = new StackPane(graph);
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.setBackground(Background.fill(Color.LIGHTGRAY));
        graph.adjustSize(root);
        graph.setFrame(model.frames, model.calculateMaxFramesAmount());

        final var graphsStage = new GraphsStage(model);
        graphsStage.setFrame(model.getFirstFrame(), model.getFrameId(), model.getFps());

        StackPane gui = new StackPane();
        boolean[] animationStarted = {false};

        final var angleText = new Text("Угол, °");
        TextField angle = new TextField("45.0");
        angle.setTextFormatter(NumberPatterns.SIGNED_DOUBLE_FORMATTER);
        angle.setOnAction(e -> {
            model.updateInitAngle(Double.parseDouble(angle.getText()));
        });
        final var angleBox = new VBox(angleText, angle);

        final var heightText = new Text("Высота, м");
        TextField height = new TextField("2.0");
        height.setTextFormatter(NumberPatterns.UNSIGNED_DOUBLE_FORMATTER);
        height.setOnAction(e -> {
            model.updateInitHeight(Double.parseDouble(height.getText()));
        });
        final var heightBox = new VBox(heightText, height);

        final var vText = new Text("Скорость, м/с");
        final var v = new TextField("30.0");
        v.setTextFormatter(NumberPatterns.UNSIGNED_DOUBLE_FORMATTER);
        v.setOnAction(e -> {
            model.updateInitVelocity(Double.parseDouble(v.getText()));
        });
        final var vBox = new VBox(vText, v);

        final var fpsText = new Text("Скорость анимации, кадр/с (п.у. 60)");
        TextField fps = new TextField("60");
        fps.setTextFormatter(NumberPatterns.UNSIGNED_INT_FORMATTER);
        fps.setOnAction(e -> {
            model.updateInitFps(Integer.parseInt(fps.getText()));
        });
        final var fpsBox = new VBox(fpsText, fps);

        final var startButton = new Button("Запустить модель");
        startButton.setOnAction(e -> {
            animationStarted[0] = true;
        });

        HBox inputs = new HBox(v, height, angle, fps);
        StackPane.setAlignment(inputs, Pos.CENTER);
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER);

        gui.getChildren().add(inputs);
        gui.getChildren().add(startButton);

        root.getChildren().add(gui);

        final var animation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (animationStarted[0]) {
                    if (model.isFlying()) {
                        model.applyVelocity();
                        if (model.peak()) {
                            graph.addMark(model.getPrevFrame());
                        }
                        graphsStage.setFrame(model.getLastFrame(), model.getFrameId(), model.getFps());
                    } else {
                        animationStarted[0] = false;
                    }
                }
                graph.adjustSize(root);
                graph.setFrame(model.frames, model.calculateMaxFramesAmount());
            }
        };

        animation.start();
        final var scene = new Scene(root);
        stage.setScene(scene);
        graphsStage.show();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}