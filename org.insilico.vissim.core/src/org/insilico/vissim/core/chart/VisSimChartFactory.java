package org.insilico.vissim.core.chart;

import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.core.Messages;
import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

/**
 * Factory class for appropriate {@code LineChart} initialization
 * */
public class VisSimChartFactory {
	
	static public Pane getLineChart(ChartType type, SimulationResult result) {
			switch (type) {
			case LINE_CHART:
				try {
					return LineChartBuilder.class.newInstance().buildChart(result);
				} catch (Exception e) {
					showStackTraceDialog(e);
				}
			case DATA_VIEWER_CHART:
				try {
					return DataViewerChartBuilder.class.newInstance().buildChart(result);
				} catch (Exception e) {
					showStackTraceDialog(e);
				}
			default:
				return null;
			}
	}

	private static void showStackTraceDialog(Exception e) {
		new DialogUtils().showConfirmationDialog(Messages.init_error, Messages.info_general,
				Messages.init_error_chart, AlertType.ERROR, e);
	}
}
