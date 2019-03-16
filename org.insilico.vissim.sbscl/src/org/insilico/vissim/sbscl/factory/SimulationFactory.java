package org.insilico.vissim.sbscl.factory;

import org.insilico.vissim.sbscl.simulation.AbstractSimulation;
import org.insilico.vissim.sbscl.simulation.OMEXSimulation;
import org.insilico.vissim.sbscl.simulation.SBMLSimulation;
import org.insilico.vissim.sbscl.simulation.SEDMLSimulation;
import org.insilico.vissim.sbscl.simulation.SimulationType;

/**
 * Factory class for appropriate Simulation initialization
 * */
public class SimulationFactory {
	public AbstractSimulation getSimulation(SimulationType type) {
		try {
			switch (type) {
			case SEDML:
					return SEDMLSimulation.class.newInstance();
			case SBML:
					return SBMLSimulation.class.newInstance();
			case OMEX:
					return OMEXSimulation.class.newInstance();
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
