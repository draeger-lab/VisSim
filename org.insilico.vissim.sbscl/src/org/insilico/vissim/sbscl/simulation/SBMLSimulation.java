package org.insilico.vissim.sbscl.simulation;

import org.insilico.vissim.sbscl.factory.ResultAdapter;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLReader;
import org.simulator.math.odes.AbstractDESSolver;
import org.simulator.math.odes.MultiTable;
import org.simulator.math.odes.RosenbrockSolver;
import org.simulator.sbml.SBMLinterpreter;

public class SBMLSimulation extends AbstractSimulation {

	/**
	 * Simulates differential equation system with a chosen solver and given time
	 * points.
	 * 
	 * @return null or MultiTable capable to provide convenient data for
	 *         visualization
	 */
	@Override
	public SimulationResult simulate(String path) throws Exception {
		SBMLReader reader = new SBMLReader();
		try {
			Model model = reader.readSBML(path).getModel();
			SBMLinterpreter interpreter = new SBMLinterpreter(model);
			AbstractDESSolver solver = new RosenbrockSolver();
			MultiTable solution = solver.solve(interpreter, interpreter.getInitialValues(), generateTimePoints());
			return new ResultAdapter(solution).getResult();
		} catch (Exception e) {
			// throw Ex. and log
		}
		return null;
	}
	
	/**
	 * Generates time points for simulation
	 * */
	public double[] generateTimePoints() {
		// XXX: evaluate how to provide time points: user interaction and automatic generation
		return null;
	}

}
