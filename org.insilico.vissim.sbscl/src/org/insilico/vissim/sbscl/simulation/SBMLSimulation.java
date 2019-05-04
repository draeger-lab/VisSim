package org.insilico.vissim.sbscl.simulation;

import org.insilico.vissim.sbscl.factory.ResultAdapter;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.ui.SBMLDialog;
import org.insilico.vissim.sbscl.utils.SBSCLUtils;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.UnitDefinition;
import org.simulator.math.odes.AbstractDESSolver;
import org.simulator.math.odes.MultiTable;
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
		SimulationResult result = null;
			Model model = reader.readSBML(path).getModel();
			UnitDefinition timeUnitsInstance = model.getTimeUnitsInstance();
			
			SBMLinterpreter interpreter = new SBMLinterpreter(model);
			SBMLDialog sbmlDialog = new SBMLDialog();
			sbmlDialog.initDialog();
			AbstractDESSolver solver = getSolver(SBMLDialog.getSolverType());
			if (SBMLDialog.isCanceled() || solver == null) {
				throw new Exception();
			}
			double[] tp = getTimePoints(sbmlDialog);
			MultiTable solution = solver.solve(interpreter, interpreter.getInitialValues(), tp);
			result = new ResultAdapter(solution, timeUnitsInstance.getName()).getResult();
			return result;
	}

	/**
	 * Produces simulation time points depending on specified values.
	 * */
	private double[] getTimePoints(SBMLDialog sbmlDialog) {
		double[] tp = new double[sbmlDialog.getTimepointsNumber()];
		double sum = 0.0d;
		double timepointDuration = sbmlDialog.getTimepointDuration();
		for (int i = 0; i < sbmlDialog.getTimepointsNumber(); i++) {
			tp[i] = SBSCLUtils.round(sum, 1);
			sum += timepointDuration;
		}
		return tp;
	}

}
