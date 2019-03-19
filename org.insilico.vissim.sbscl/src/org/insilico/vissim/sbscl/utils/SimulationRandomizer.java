package org.insilico.vissim.sbscl.utils;

import java.util.LinkedList;
import java.util.Random;

import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;


/**
 * Generates random SimulationResult. 
 * Tend to be used for isolated view dev.
 * 
 * */
public class SimulationRandomizer {
	
	private static final String SIMULATION_NAME = "Randomized simulation 42";
	private static final int TIME_POINTS_COUNT = 25; 
	private static final int LAYERS_COUNT = 1;
	private static final int QUANTITIES_COUNT = 4;
	private String simulationName;
	private int timePoints;
	private int layersCount;
	private int quantitiesCount;
	
    /**
     * Initializes a newly created {@code SimulationRandomizer} object so that it represents
     * an random Simulation result. Predefined values are used. To specify boundries use 
     * {@code SimulationResult(String simulationName, int timePoints, int layersCount, int quantitiesCount)}
     */
	public SimulationRandomizer() {
		this(SIMULATION_NAME, TIME_POINTS_COUNT, LAYERS_COUNT, QUANTITIES_COUNT);
	}
	
    /**
     * Initializes a newly created {@code SimulationRandomizer} object so that it represents
     * an random Simulation result.
     */
	public SimulationRandomizer(String simulationName, int timePoints, int layersCount, int quantitiesCount) {
		super();
		this.simulationName = simulationName;
		this.timePoints = timePoints;
		this.layersCount = layersCount;
		this.quantitiesCount = quantitiesCount;
	}

	/**
	 * Produces a random SimulationResult
	 * 
	 * @return random generated simulation result
	 * */
	public SimulationResult initRandomSimulationResult() {
		double[] timePoints = getRandomTimePoints(this.timePoints);
		LinkedList<Layer> layers = new LinkedList<Layer>();
		for (int i = 0; i < layersCount; i++) {
			Layer layer = new Layer();
			layer.setLayerName("Layer: " + i);
			for (int j = 0; j < quantitiesCount; j++) {
				double[] random = getRandomNumbers(this.timePoints);
				layer.addQuantity(new Quantity("Quantity: " + j, random));
			}
			layers.add(layer);
		}
		return new SimulationResult(timePoints, layers, simulationName);
	}

	/**
	 * Generates ascending time points sequence with specified length
	 * */
	private double[] getRandomTimePoints(int length) {
		double[] timePoints = new double[length];
		for (int i = 0; i < timePoints.length; i++) {
			timePoints[i] = i;
		}
		return timePoints;
	}

	/**
	 * Generates random numbers sequence with specified length
	 * */
	private double[] getRandomNumbers(int length) {
		double[] numbers = new double[length];
		Random rand = new Random();
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = rand.nextInt(timePoints);
		}
		return numbers;
	}
}
