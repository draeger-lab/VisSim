package org.insilico.vissim.sbscl.factory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;
import org.simulator.math.odes.MultiTable;
import org.simulator.math.odes.MultiTable.Block;
import org.simulator.math.odes.MultiTable.Block.Column;

public class ResultAdapter {
	private final static int ROUND_PLACES = 2;
	
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
			Block block = table.getBlock(i);
			int columnCount = block.getColumnCount();
			Layer layer = new Layer();
			layer.setLayerName(block.getName());
			for (int j = 0; j < columnCount - 1; j++) {
				Column column = block.getColumn(j);
				double[] values = new double[column.getRowCount()];
				for (int k = 0; k < column.getRowCount(); k++) {
					values[k] = round(column.getValue(k), ROUND_PLACES);
				}
				layer.addQuantity(new Quantity(column.getColumnName(), values));
			}
			layers.add(layer);
		}
		return new SimulationResult(table.getTimePoints(), layers, table.getName());
	}
	
	/**
	 * Helper function to round doubles.
	 * */
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}
