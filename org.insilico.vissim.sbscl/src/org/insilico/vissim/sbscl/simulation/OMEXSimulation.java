package org.insilico.vissim.sbscl.simulation;

import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.insilico.vissim.sbscl.factory.ResultAdapter;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.utils.Utils;
import org.jlibsedml.AbstractTask;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.execution.IProcessedSedMLSimulationResults;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
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
				List<Path> filesList = Files.walk(Paths.get(new File(path).getParent().toString()))
			     .filter(Files::isRegularFile)
			     .collect(Collectors.toList());
				String sedPath = null; 
				String sedFolder = null;
				for (Path file : filesList) {
					if (new Utils().getSimulationType(file.toString()) == SimulationType.SEDML) {
						sedPath = file.toString();
						sedFolder = file.getParent().toString();
						break;
					}
				}
				File file = new File(sedPath);
				SEDMLDocument doc = Libsedml.readDocument(file);
				SedML sedml = doc.getSedMLModel();

	            Output wanted = sedml.getOutputs().get(0);
	            SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted, sedFolder);

	            Map<AbstractTask, List<IRawSedmlSimulationResults>> res = exe.run();
	            if (res == null || res.isEmpty() || !exe.isExecuted()) {
	                fail("Simulation failed: " + exe.getFailureMessages().get(0));
	            }

	            IProcessedSedMLSimulationResults results = exe.processSimulationResults(wanted, res);
	            return new ResultAdapter(results, sedml.getElementName()).getResult();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
