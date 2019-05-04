package org.insilico.vissim.core.chart;

import java.util.LinkedList;

import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.BorderPane;


/**
 * Provides {@code LineChart} for values distribution of some {@code SimulationResult} 
 * */
public class LineChartBuilder implements ChartBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public BorderPane buildChart(SimulationResult result) {
		double[] timePoints = result.getTimePoints();
		final NumberAxis timePointsAxis = new NumberAxis(0, timePoints[timePoints.length - 1], timePoints[1]);
		timePointsAxis.setLabel("Time points (" + result.getTimeEntity() + ")");
		final NumberAxis valuesAxis = new NumberAxis();
		valuesAxis.setLabel("Values");
		final LineChart<Number, Number> chart = new LineChart<>(timePointsAxis, valuesAxis);
		chart.setTitle(result.getLayers().get(0).getLayerName());
		for (Layer layer : result.getLayers()) {
			LinkedList<Quantity> quantities = layer.getQuantities();
			for (Quantity quantity : quantities) {
				Series<Number, Number> lineChart = new XYChart.Series<Number, Number>();
				lineChart.setName(quantity.getQuantityName());
				ObservableList<Data<Number, Number>> chartData = lineChart.getData();
				for (int i = 0; i < result.getTimePoints().length; i++) {
					chartData
							.add(new XYChart.Data<Number, Number>(result.getTimePoints()[i], quantity.getResults()[i]));
				}
				chart.getData().addAll(lineChart);
			}
		}
		return new BorderPane(chart);
	}
}
