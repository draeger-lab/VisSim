package org.insilico.vissim.core.table;

import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.control.TableView;

/**
 * Standard interface for any VisSim ChartBuilder
 * */
public interface TableBuilder {
	TableView<?> buildTable(SimulationResult result);
}
