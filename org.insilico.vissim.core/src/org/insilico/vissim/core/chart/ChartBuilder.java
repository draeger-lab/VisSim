package org.insilico.vissim.core.chart;

import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.chart.LineChart;

/**
 * Standard interface for any VisSim ChartBuilder
 * */
public interface ChartBuilder {
	LineChart<?,?> buildChart(SimulationResult result);
}
