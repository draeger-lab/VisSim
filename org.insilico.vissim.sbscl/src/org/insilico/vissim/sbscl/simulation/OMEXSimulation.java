package org.insilico.vissim.sbscl.simulation;

import java.io.File;
import java.util.Map;

import org.insilico.vissim.sbscl.factory.ResultAdapter;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.simulator.math.odes.MultiTable;
import org.simulator.omex.OMEXArchive;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

public class OMEXSimulation extends AbstractSimulation {

	/**
	 * Executes simulation for OMEX files
	 * 
	 * @return null or solution MultiTable capable to provide convenient data for
	 *         visualization
	 */
	@Override
	public SimulationResult simulate(String path) throws Exception {
		try {
			OMEXArchive archive = new OMEXArchive(new File(path));
			if (archive.containsSBMLModel() && archive.containsSEDMLDescp()) {
				archive.extractArchive(new File(path).getParentFile());
				archive.close();
				// XXX: 
				String sedPath = new File(path).getParent().toString() + "SED-ML path"; 
				SEDMLDocument doc = Libsedml.readDocument(new File(sedPath));
				SedML sedml = doc.getSedMLModel();
				Output wanted = sedml.getOutputs().get(0);
				SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted,
						new File(path).getParentFile().toString());
				
				@SuppressWarnings("rawtypes")
				Map res = exe.runSimulations(); // XXX: parametrize
				@SuppressWarnings("unchecked")
				MultiTable solution = (MultiTable) exe.processSimulationResults(wanted, res);
				return new ResultAdapter(solution).getResult();
			} else {
				// XXX:
				// Not enough for simulation
				// Provide another options
			}
		} catch (Exception e) {
			// XXX:
			// throw Ex. and log
		}
		return null;
	}

}
