package org.insilico.vissim.sbscl.factory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;
import org.jlibsedml.execution.IProcessedSedMLSimulationResults;
import org.simulator.math.odes.MultiTable;
import org.simulator.math.odes.MultiTable.Block;
import org.simulator.math.odes.MultiTable.Block.Column;

public class ResultAdapter {
	private final static int ROUND_PLACES = 3;
	
	public SimulationResult getResult() {
		return result;
	}

	public ResultAdapter(MultiTable mTable) {
		super();
		this.mTable = mTable;
		this.result = init(mTable);
	}
	
	public ResultAdapter(MultiTable mTable, String timeEntity) {
		super();
		this.mTable = mTable;
		this.timeEntity = timeEntity;
		this.result = init(mTable);
	}
	
	public ResultAdapter(IProcessedSedMLSimulationResults result, String name) {
		super();
		this.result = init(result, name);
	}

	MultiTable mTable;
	SimulationResult result;
	String timeEntity;
	
	/**
	 * Translate IProcessedSedMLSimulationResults into SimulationResult and assign time entity
	 * 
	 * */
	private SimulationResult init(IProcessedSedMLSimulationResults result, String name) {
		double[][] data = result.getData();
		double[] timePoints = new double[data.length];
		String[] columnHeaders = result.getColumnHeaders();
		LinkedList<Layer> layers = new LinkedList<Layer>();
		Layer layer = new Layer();
		layer.setLayerName(name);
		int timePointsCol = 0;
		for (int i = 0; i < columnHeaders.length; i++) {
			if (columnHeaders[i].toLowerCase().matches(".*time.*")) {
				timePointsCol = i;
				this.timeEntity = columnHeaders[i];
			} else {
				layer.addQuantity(new Quantity(columnHeaders[i], new double[data.length]));
			}
		}
		LinkedList<Quantity> quantities = layer.getQuantities();
		boolean timePointColFound = false;
		for (int i = 0; i < data.length; i++) {
			timePointColFound = false;
			int length = data[0].length;
			for (int j = 0; j < length; j++) {
				if (timePointsCol == j) {
					timePoints[i] = data[i][j];
					timePointColFound = true;
				} else {
					Quantity quantity = null;
					if (timePointColFound) {
						quantity = quantities.get(j - 1);
						
					} else {
						quantity = quantities.get(j);
					}
					double[] results = quantity.getResults();
					results[i] = round(data[i][j], ROUND_PLACES);
					quantity.setResults(results);
				}
			}
		}
		layers.add(layer);
		return new SimulationResult(timePoints, layers, name, timeEntity);
	}
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
		if (timeEntity != null) {
			return new SimulationResult(table.getTimePoints(), layers, table.getName(), timeEntity);
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
