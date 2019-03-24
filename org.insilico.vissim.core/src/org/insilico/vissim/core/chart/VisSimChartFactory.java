package org.insilico.vissim.core.chart;

import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert.AlertType;

/**
 * Factory class for appropriate {@code LineChart} initialization
 * */
public class VisSimChartFactory {
	
	static public LineChart<?,?> getValuesTable(ChartType type, SimulationResult result) {
			switch (type) {
			case LINE_CHART:
				try {
					return LineChartBuilder.class.newInstance().buildChart(result);
				} catch (Exception e) {
					new DialogUtils().showConfirmationDialog("Initialization error", "Something went wrong.",
							"Corresponding chart can not be initialized", AlertType.ERROR, e);
				}
			default:
				return null;
			}
	}
}
