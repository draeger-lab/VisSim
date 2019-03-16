package org.insilico.vissim.sbscl.factory;

import java.util.LinkedList;

import org.insilico.vissim.sbscl.result.Layer;

public class SimulationResult {
	private double[] timePoints;
	public SimulationResult(double[] timePoints, LinkedList<Layer> layers) {
		super();
		this.timePoints = timePoints;
		this.layers = layers;
	}

	private LinkedList<Layer> layers;

	public double[] getTimePoints() {
		return timePoints;
	}

	public void setTimePoints(double[] timePoints) {
		this.timePoints = timePoints;
	}

	public LinkedList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(LinkedList<Layer> layers) {
		this.layers = layers;
	}
}
