package org.insilico.vissim.sbscl.factory;

import java.util.LinkedList;

import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;
import org.simulator.math.odes.MultiTable;
import org.simulator.math.odes.MultiTable.Block;
import org.simulator.math.odes.MultiTable.Block.Column;

public class ResultAdapter {
	public SimulationResult getResult() {
		return result;
	}

	public ResultAdapter(MultiTable mTable) {
		super();
		this.mTable = mTable;
		this.result = init(mTable);
	}

	MultiTable mTable;
	SimulationResult result;
	
	/**
	 * Translate MultiTable into SimulationResult
	 * TODO: change extremely inefficient ~n^3 algorithm
	 * 
	 * */
	private SimulationResult init(MultiTable table) {
		LinkedList<Layer> layers = new LinkedList<Layer>();
		for (int i = 0; i < table.getBlockCount(); i++) {
			int columnCount = table.getColumnCount();
			Block block = table.getBlock(i);
			Layer layer = new Layer();
			for (int j = 0; j < columnCount; j++) {
				Column column = block.getColumn(j);
				double[] values = new double[columnCount];
				for (int k = 0; k < column.getRowCount(); k++) {
					values[k] = column.getValue(k);
				}
				layer.addQuantity(new Quantity(column.getColumnName(), values));
			}
			layers.add(layer);
		}
		return new SimulationResult(table.getTimePoints(), layers);
	}
	
}
