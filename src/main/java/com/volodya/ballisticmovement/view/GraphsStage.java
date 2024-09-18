package com.volodya.ballisticmovement.view;

import com.volodya.ballisticmovement.BallisticModel;
import com.volodya.ballisticmovement.types.FrameInfo;
import com.volodya.ballisticmovement.types.Pair;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GraphsStage extends Stage {
    private LineChart<Number, Number> xGraph;
    final private XYChart.Series<Number, Number> xSeries = new XYChart.Series<>();
    private LineChart<Number, Number> vGraph;
    final private XYChart.Series<Number, Number> vSeries = new XYChart.Series<>();
    private HBox graphs;
    private Scene scene;

    public GraphsStage(BallisticModel model) {
        super();
        initFrame(model);
    }

    private static NumberAxis makeAxis(double minValue, double maxValue) {
        minValue = Math.floor(minValue);
        maxValue = Math.ceil(maxValue);
        final var axis = new NumberAxis();
        axis.setAutoRanging(false);
        if (maxValue >= minValue) {
            axis.setLowerBound(minValue);
            axis.setUpperBound(maxValue);
        } else {
            axis.setLowerBound(maxValue);
            axis.setUpperBound(minValue);
        }
        return axis;
    }

    private static LineChart<Number, Number> makeGraph(String title, double minX, double minY, double maxX, double maxY, XYChart.Series<Number, Number> series) {
        final var xAxis = makeAxis(minX * 1.1, maxX * 1.1);
        final var yAxis = makeAxis(minY * 1.1, maxY * 1.1);
        final var graph = new LineChart<>(xAxis, yAxis);
        graph.setCreateSymbols(false);
        graph.setLegendVisible(false);
        graph.setTitle(title);
        graph.getData().add(series);
        return graph;
    }

    public void setFrame(FrameInfo frame, int frameId, int fps) {
        xSeries.getData().add(new XYChart.Data<>((double) frameId / fps, frame.getX()));
        vSeries.getData().add(new XYChart.Data<>((double) frameId / fps, frame.getVelocity()));
        graphs.setPrefSize(this.getWidth(), this.getHeight());
    }

    public void initFrame(BallisticModel model) {
        xSeries.getData().clear();
        vSeries.getData().clear();
        xGraph = makeGraph(
                "График зависимости x от t",
                0,
                0,
                model.calculateTheoreticalTime(),
                model.calculateTheoreticalDistance(),
                xSeries
        );
        vGraph = makeGraph(
                "График зависимости v от t",
                0,
                -model.getFirstFrame().getVelocity(),
                model.calculateTheoreticalTime(),
                model.getFirstFrame().getVelocity(),
                vSeries
        );
        graphs = new HBox(xGraph, vGraph);
        scene = new Scene(graphs);
        this.setScene(scene);
    }
}
