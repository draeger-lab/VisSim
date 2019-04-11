/**
 * 
 */
package org.insilico.vissim.sbscl.simulation;

public enum SolverType {
	ROSENBOCK_SOLVER, ADAMS_BASH_FORTH_SOLVER, ADAMS_MOULTON_SOLVER, DORMAND_PRINCE_54_SOLVER, DORMAND_PRINCE_85_SOLVER,
	GRAGG_BULIRISCH_STOER_SOLVER, HIGHAM_HALL_54_SOLVER, EULER_METHOD;

	/**
	 * Converts string to corresponding Enum
	 * 
	 * @param string name of target Enum
	 * @return corresponding enum, or null
	 */
	public static SolverType getEnumFromString(String string) {
		if (string != null) {
			try {
				return Enum.valueOf(SolverType.class, string.trim().replaceAll(" ", "_").toUpperCase());
			} catch (IllegalArgumentException ex) {
				//
			}
		}
		return null;
	}
}
