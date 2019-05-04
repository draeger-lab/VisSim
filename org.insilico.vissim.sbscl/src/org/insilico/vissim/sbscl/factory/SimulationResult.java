package org.insilico.vissim.sbscl.factory;

import java.util.LinkedList;

import org.insilico.vissim.sbscl.result.Layer;

public class SimulationResult {
	private double[] timePoints;
	private String simulationName;
	private String timeEntity;
	public SimulationResult(double[] timePoints, LinkedList<Layer> layers, String simulationName) {
		super();
		this.timePoints = timePoints;
		this.layers = layers;
		this.simulationName = simulationName;
	}
	
	public SimulationResult(double[] timePoints, LinkedList<Layer> layers, String simulationName, String timeEntity) {
		super();
		this.timePoints = timePoints;
		this.layers = layers;
		this.simulationName = simulationName;
		this.setTimeEntity(timeEntity);
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

	public String getSimulationName() {
		return simulationName;
	}

	public void setSimulationName(String simulationName) {
		this.simulationName = simulationName;
	}

	/**
	 * @return the timeEntity
	 */
	public String getTimeEntity() {
		return timeEntity;
	}

	/**
	 * @param timeEntity the timeEntity to set
	 */
	public void setTimeEntity(String timeEntity) {
		this.timeEntity = timeEntity;
	}
}
