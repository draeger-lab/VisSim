package org.insilico.vissim.sbscl.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jlibsedml.AbstractTask;
import org.jlibsedml.Curve;
import org.jlibsedml.Output;
import org.jlibsedml.Plot2D;
import org.jlibsedml.execution.IProcessedSedMLSimulationResults;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
import org.simulator.plot.PlotProcessedSedmlResults;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

/**
 * Provides basic functionality to work with SBSCL library with general
 * intention is to provide prepared data for JFX visualization.
 * 
 */
public class SBSCLUtils {

	/**
	 * Provides SED-ML plottable simulation results
	 * 
	 * @return null or PlotProcessedSedmlResults
	 */
	public PlotProcessedSedmlResults plotSEDML2D(String sedmlPath, Output wanted, SedMLSBMLSimulatorExecutor exe,
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

}
