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
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public final class BMApplication extends Application {
    final static double WINDOW_WIDTH = 666.666;
    final static double WINDOW_HEIGHT = 500;
    final private static double DEFAULT_ANGLE = 45;
    final private static double DEFAULT_HEIGHT = 2;
    final private static double DEFAULT_VELOCITY = 10;
    final private static int DEFAULT_FPS = 60;

    private<T> TextField makeInput(TextFormatter<T> formatter) {
        TextField textField = new TextField();
        textField.setTextFormatter(formatter);
        return textField;
    }

    private<T> VBox makeInputBox(String label, TextField textField) {
        final var text = new Text(label);
        return new VBox(text, textField);
    }

    private static void updateGraphs(BallisticModel model, BallisticGraph ballisticGraph, GraphsStage graphsStage) {
        ballisticGraph.setInit(model.calculateTheoreticalDistance(), model.calculateTheoreticalHeight());
        ballisticGraph.setFrame(model.getFrames(), model.calculateMaxFramesAmount());
        graphsStage.initFrame(model);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Баллистическое движение");

        final var model = new BallisticModel(DEFAULT_ANGLE, DEFAULT_HEIGHT, DEFAULT_VELOCITY, DEFAULT_FPS);
        final var graph = new BallisticGraph(model.calculateTheoreticalDistance(), model.calculateTheoreticalHeight());
        StackPane.setAlignment(graph, Pos.TOP_CENTER);
        StackPane.setMargin(graph, new Insets(20, 0, 0, 0));

        final var root = new StackPane(graph);
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.setBackground(Background.fill(Color.LIGHTGRAY));
        graph.adjustSize(root);
        graph.setFrame(model.getFrames(), model.calculateMaxFramesAmount());

        final var graphsStage = new GraphsStage(model);
        graphsStage.setFrame(model.getFirstFrame(), model.getFrameId(), model.getFps());

        StackPane gui = new StackPane();
        boolean[] animationStarted = {false};

        final var angleField = makeInput(NumberPatterns.makeSignedDoubleFormatter(DEFAULT_ANGLE));
        final var angleBox = makeInputBox("Угол, °", angleField);

        final var heightField = makeInput(NumberPatterns.makeUnsignedDoubleFormatter(DEFAULT_HEIGHT));
        final var heightBox = makeInputBox("Высота, м", heightField);

        final var velocityField = makeInput(NumberPatterns.makeUnsignedDoubleFormatter(DEFAULT_VELOCITY));
        final var velocityBox = makeInputBox("Скорость, м/с", velocityField);

        final var fpsField = makeInput(NumberPatterns.makeUnsignedIntFormatter(DEFAULT_FPS));
        final var fpsBox = makeInputBox("Скорость анимации, кадр/с", fpsField);

        final var inputs = new HBox(angleBox, heightBox, velocityBox, fpsBox);
        inputs.setAlignment(Pos.TOP_CENTER);

        final var enterButton = new Button("Ввести значения");
        enterButton.setOnAction(e -> {
            model.updateInitData(
                    Double.parseDouble(angleField.getText()),
                    Double.parseDouble(heightField.getText()),
                    Double.parseDouble(velocityField.getText()),
                    Integer.parseInt(fpsField.getText())
            );
            updateGraphs(model, graph, graphsStage);
        });

        StackPane.setAlignment(enterButton, Pos.CENTER);
        final var inputBox = new StackPane(inputs, enterButton);

        final var startButton = new Button("Запустить модель");
        startButton.setOnAction(e -> {
            if (animationStarted[0]) {
                startButton.setText("Запустить модель");
            } else {
                startButton.setText("Пауза");
            }
            animationStarted[0] = !animationStarted[0];
            enterButton.setDisable(animationStarted[0]);
        });

        final var stopButton = new Button("Остановить модель");
        stopButton.setOnAction(e -> {
            startButton.getOnAction().handle(e);
            enterButton.getOnAction().handle(e);
            stopButton.setDisable(true);
        });
        stopButton.setDisable(true);

        final var showGraphs = new Button("Показать графики");
        showGraphs.setOnAction(e -> {
            graphsStage.show();
        });

        StackPane.setAlignment(startButton, Pos.TOP_LEFT);
        StackPane.setAlignment(stopButton, Pos.TOP_CENTER);
        StackPane.setAlignment(showGraphs, Pos.TOP_RIGHT);
        final var startBox = new StackPane(startButton, stopButton, showGraphs);

        StackPane.setAlignment(inputBox, Pos.TOP_CENTER);
        StackPane.setAlignment(startBox, Pos.BOTTOM_CENTER);
        gui.getChildren().add(inputBox);
        gui.getChildren().add(startBox);

        StackPane.setAlignment(gui, Pos.BOTTOM_CENTER);
        root.getChildren().add(gui);

        final var animation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (animationStarted[0]) {
                    if (stopButton.isDisabled()) {
                        stopButton.setDisable(false);
                    }
                    if (model.isFlying()) {
                        model.applyVelocity();
                        if (model.peak()) {
                            graph.addMark(model.getPrevFrame());
                        }
                        graphsStage.setFrame(model.getLastFrame(), model.getFrameId(), model.getFps());
                    } else {
                        animationStarted[0] = false;
                    }
                } else if (enterButton.isDisabled() || !stopButton.isDisabled()) {
                    startButton.setText("Запустить модель");
                    stopButton.setDisable(true);
                    enterButton.setDisable(false);
                }
                gui.setMinSize(root.getWidth(), root.getHeight() / 3.5);
                gui.setMaxSize(root.getWidth(), root.getHeight() / 3.5);

                inputBox.setMinWidth(gui.getWidth());
                inputBox.setMaxWidth(gui.getWidth());

                startBox.setMaxSize(gui.getWidth(), startButton.getHeight());

                graph.adjustSize(root);
                graph.setFrame(model.getFrames(), model.calculateMaxFramesAmount());
            }
        };

        animation.start();
        final var scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}