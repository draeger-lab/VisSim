package org.insilico.vissim.sbscl.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.insilico.vissim.sbscl.simulation.SimulationType;
import org.jlibsedml.Libsedml;
import org.sbml.jsbml.SBMLReader;
import org.simulator.omex.OMEXArchive;

/**
 * Some basic utilities which need to be in helping project (for shifting operations or
 * other reasons), which are not toolkit dependent.
 */
public class Utils {

	/**
	 * Provides the type of corresponding simulation
	 * 
	 * <p>
	 * Every simulation is eponymous to the type of the file specified.
	 * e.g SimulationType.SEDML is a simulation described with SED-ML file. 
	 * 
	 * @param path absolute path to the file
	 * @return {@link SimulationType}
	 * */
	public SimulationType getSimulationType(String path) {
			if (Libsedml.isSEDML(new File(path)))
				return SimulationType.SEDML;
			if (isOMEXArchiv(path)) {
				return SimulationType.OMEX;
			}
			if (isSBML(path)) {
				return SimulationType.SBML;
			}
		return SimulationType.UNKNOWN_ENTITY;
	}

	/**
	 * Checks if provided file is a OMEXArchiv (combination of SED-ML
	 * and SBML file with additional data)
	 * 
	 * @param path absolute path to the file
	 * */
	private boolean isOMEXArchiv(String path) {
		OMEXArchive archive;
		try {
			archive = new OMEXArchive(new File(path));
			assertNotNull(archive);
			assertTrue(archive.containsSBMLModel());
			assertTrue(archive.containsSEDMLDescp());
			archive.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}
	
	/**
	 * Checks if provided file is a SBML file
	 * XXX: more efficient check should be considered
	 * 
	 * @param path absolute path to the file
	 * */
	private boolean isSBML(String path) {
		try {
			// this check should be changed
			// reading file to define file type is inefficient
			SBMLReader.read(path);
			return Boolean.TRUE;
		} catch (XMLStreamException e) {
			return Boolean.FALSE;
		}
	}
}
