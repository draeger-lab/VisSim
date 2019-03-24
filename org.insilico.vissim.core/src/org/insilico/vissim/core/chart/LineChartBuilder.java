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


/**
 * Provides {@code LineChart} for values distribution of some {@code SimulationResult} 
 * */
public class LineChartBuilder implements ChartBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public LineChart<?, ?> buildChart(SimulationResult result) {
		final NumberAxis timePointsAxis = new NumberAxis(0, result.getTimePoints().length - 1, 1);
		final NumberAxis valuesAxis = new NumberAxis();
		final LineChart<Number, Number> chart = new LineChart<>(timePointsAxis, valuesAxis);
		for (Layer layer : result.getLayers()) {
			chart.setTitle(layer.getLayerName());
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
		return chart;
	}

}
