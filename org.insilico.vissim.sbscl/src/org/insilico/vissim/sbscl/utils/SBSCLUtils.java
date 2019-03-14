package org.insilico.vissim.sbscl.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jlibsedml.AbstractTask;
import org.jlibsedml.Curve;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.Plot2D;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.XMLException;
import org.jlibsedml.execution.IProcessedSedMLSimulationResults;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLReader;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.simulator.math.odes.AbstractDESSolver;
import org.simulator.math.odes.MultiTable;
import org.simulator.math.odes.RosenbrockSolver;
import org.simulator.omex.OMEXArchive;
import org.simulator.plot.PlotProcessedSedmlResults;
import org.simulator.sbml.SBMLinterpreter;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

/**
 * Provides basic functionality to work with SBSCL library with
 * general intention is to provide prepared data for JFX visualization.
 * 
 */
public class SBSCLUtils {
	SBMLReader reader = new SBMLReader();

	/**
	 * Executes simulation for SED-ML files
	 * Level/Version combination: 1-1, 1-2
	 * 
	 * @return null or solution MultiTable capable to provide convenient data for visualization
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MultiTable simulateSEDML(String sedmlPath) throws XMLException, OWLOntologyCreationException, IOException {
		File file = new File(sedmlPath);
		String sedmlDir = file.getAbsoluteFile().getParentFile().getAbsolutePath();
		SEDMLDocument doc = Libsedml.readDocument(file);
		SedML sedml = doc.getSedMLModel();
		List<Output> outputs = sedml.getOutputs();
		Output wanted = outputs.get(0);
		SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted, sedmlDir);
		Map<AbstractTask, List<IRawSedmlSimulationResults>> res = exe.run();
		if (res == null || res.isEmpty() || !exe.isExecuted()) {
			// XXX: throw Ex. and log
		} else {
			Map results = exe.runSimulations();
			MultiTable solution = (MultiTable) exe.processSimulationResults(wanted, results);
			return solution;
		}
		return null;
	}

	/**
	 * Executes simulation for OMEX files
	 * 
	 * @return null or solution MultiTable capable to provide convenient data for visualization
	 */
	public MultiTable simulateOMEX(String omexPath) {
		try {
			OMEXArchive archive = new OMEXArchive(new File(omexPath));
			if (archive.containsSBMLModel() && archive.containsSEDMLDescp()) {
				archive.extractArchive(new File(omexPath).getParentFile());
				archive.close();
				// Do simulation
				String sedPath = new File(omexPath).getParent().toString() + getSEDMLPath(omexPath); // XXX: provide impl.
				SEDMLDocument doc = Libsedml.readDocument(new File(sedPath));
				SedML sedml = doc.getSedMLModel();
				Output wanted = sedml.getOutputs().get(0);
				SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted,
						new File(omexPath).getParentFile().toString());
				Map res = exe.runSimulations(); // XXX: parametrize
				MultiTable solution = (MultiTable) exe.processSimulationResults(wanted, res);
				return solution;
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
	
	/**
	 * Provides SED-ML plottable simulation results 
	 * 
	 * @return null or PlotProcessedSedmlResults
	 * */
	@SuppressWarnings("unused")
	private PlotProcessedSedmlResults plotSEDML2D(String sedmlPath, Output wanted, SedMLSBMLSimulatorExecutor exe,
			Map<AbstractTask, List<IRawSedmlSimulationResults>> res) throws IOException {
		IProcessedSedMLSimulationResults pr = exe.processSimulationResults(wanted, res);
		PlotProcessedSedmlResults p;
		List<Curve> curves = null;
		if (wanted.isPlot2d()) {
			Plot2D plots = (Plot2D) wanted;
			curves = plots.getListOfCurves();
			p = new PlotProcessedSedmlResults(pr, curves, wanted.getName());
			p.savePlot(sedmlPath, wanted.getId());
			return p;
		}
		return null;
	}

	/**
	 * Simulates differential equation system  with a chosen solver and given time points.
	 * 
	 * @return null or MultiTable capable to provide convenient data for visualization
	 * */
	public MultiTable simulateDiffEquationSystem(String path, double[] timePoints) {
		try {
			Model model = reader.readSBML(path).getModel();
			SBMLinterpreter interpreter = new SBMLinterpreter(model);
			AbstractDESSolver solver = new RosenbrockSolver();
			MultiTable solution = solver.solve(interpreter, interpreter.getInitialValues(), timePoints);
			return solution;
		} catch (Exception e) {
			// throw Ex. and log
		}
		return null;
	}
	
	/**
	 * Provides absolute paths for a SED-ML file in provided folder
	 * */
	private String getSEDMLPath(String path) {
		// XXX: impl.
		return "";
	}
}
