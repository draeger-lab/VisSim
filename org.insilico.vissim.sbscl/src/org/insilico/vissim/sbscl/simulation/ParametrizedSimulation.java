/**
 * 
 */
package org.insilico.vissim.sbscl.simulation;

import org.insilico.vissim.sbscl.factory.SolverFactory;
import org.simulator.math.odes.AbstractDESSolver;

/**
 * Interface for any transitive simulation parameter providers
 * */
public interface ParametrizedSimulation {

	/**
	 * Provides corresponding {@link AbstractDESSolver} depending on the simulation name
	 * <p>
	 * Solver name should match any {@link SolverType} Enum. 
	 * </p>
	 * @param solverName name of target solver
	 * @return AbstractSolver or null
	 * */
	default AbstractDESSolver getSolver(String solverName) {
		SolverFactory factory = new SolverFactory();
		return factory.getSimulation(SolverType.getEnumFromString(solverName));
	}
}
