package org.insilico.vissim.core.chart;

import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.layout.Pane;

/**
 * Standard interface for any VisSim ChartBuilder
 * */
public interface ChartBuilder {
	Pane buildChart(SimulationResult result);
}
