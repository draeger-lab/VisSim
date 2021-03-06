package org.insilico.vissim.sbscl.simulation;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.insilico.vissim.sbscl.factory.ResultAdapter;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.jlibsedml.AbstractTask;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.execution.IProcessedSedMLSimulationResults;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

public class SEDMLSimulation extends AbstractSimulation {
	/**
	 * Executes simulation for SED-ML files Level/Version combination: 1-1, 1-2
	 * 
	 * @return null or solution MultiTable capable to provide convenient data for
	 *         visualization
	 */
	@Override
	public SimulationResult simulate(String path) throws Exception {
		File file = new File(path.replace("file:", ""));
		String sedmlDir = file.getAbsoluteFile().getParentFile().getAbsolutePath();
		SEDMLDocument doc = Libsedml.readDocument(file);
		SedML sedml = doc.getSedMLModel();
		List<Output> outputs = sedml.getOutputs();
		Output wanted = outputs.get(0);
		SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted, sedmlDir);
		Map<AbstractTask, List<IRawSedmlSimulationResults>> res = exe.run();
		if (res == null || res.isEmpty() || !exe.isExecuted()) {
			//
		} else {
			IProcessedSedMLSimulationResults results = exe.processSimulationResults(wanted, res);
			return new ResultAdapter(results, sedml.getElementName()).getResult();
		}
		return null;
	}

}
