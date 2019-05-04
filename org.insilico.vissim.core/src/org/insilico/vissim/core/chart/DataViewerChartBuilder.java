package org.insilico.vissim.core.chart;

import java.util.LinkedList;

import org.apache.commons.lang.ArrayUtils;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.LineTrace;
import org.charts.dataviewer.api.trace.TraceConfiguration;
import org.charts.dataviewer.javafx.JavaFxDataViewer;
import org.charts.dataviewer.utils.TraceVisibility;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 * Provides {@code DataViewerJavaFx} for values distribution of some {@code SimulationResult} 
 * */
public class DataViewerChartBuilder implements ChartBuilder {

	@Override
	public Pane buildChart(SimulationResult result) {
		double[] timePoints = result.getTimePoints();
		JavaFxDataViewer dataviewer = new JavaFxDataViewer();
		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle(result.getLayers().get(0).getLayerName());
		if (result.getTimeEntity() != null) {
			config.setxAxisTitle("Time points (" + result.getTimeEntity() + ")");
		} else {
			config.setxAxisTitle("Time points (entity undefined)");
		}
		config.setyAxisTitle("Values");
		config.showLegend(true);
		config.setLegendInsidePlot(false);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		
		for (Layer layer : result.getLayers()) {
			LinkedList<Quantity> quantities = layer.getQuantities();
			for (Quantity quantity : quantities) {
				LineTrace<Double> lineTrace = new LineTrace<>();
				lineTrace.setxArray(ArrayUtils.toObject(timePoints));
				lineTrace.setyArray(ArrayUtils.toObject(quantity.getResults()));
				TraceConfiguration lineConfig = new TraceConfiguration();
				lineConfig.setTraceName(quantity.getQuantityName());
				lineConfig.setTraceVisibility(TraceVisibility.TRUE);
				lineTrace.setConfiguration(lineConfig);
				plotData.addTrace(lineTrace);
			}
		}
		dataviewer.updatePlot(plotData);
		BorderPane pane = new BorderPane(dataviewer);
		return pane;
	}
}
