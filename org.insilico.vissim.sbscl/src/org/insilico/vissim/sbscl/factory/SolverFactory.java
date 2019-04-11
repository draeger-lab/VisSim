package org.insilico.vissim.sbscl.factory;

import org.insilico.vissim.sbscl.simulation.SolverType;
import org.simulator.math.odes.AbstractDESSolver;
import org.simulator.math.odes.AdamsBashforthSolver;
import org.simulator.math.odes.AdamsMoultonSolver;
import org.simulator.math.odes.DormandPrince54Solver;
import org.simulator.math.odes.DormandPrince853Solver;
import org.simulator.math.odes.EulerMethod;
import org.simulator.math.odes.GraggBulirschStoerSolver;
import org.simulator.math.odes.HighamHall54Solver;
import org.simulator.math.odes.RosenbrockSolver;

/**
 * Factory class for appropriate Solver initialization
 * */
public class SolverFactory {
	public AbstractDESSolver getSimulation(SolverType type) {
		try {
			switch (type) {
			case ROSENBOCK_SOLVER:
					return RosenbrockSolver.class.newInstance();
			case ADAMS_BASH_FORTH_SOLVER:
					return AdamsBashforthSolver.class.newInstance();
			case ADAMS_MOULTON_SOLVER:
					return AdamsMoultonSolver.class.newInstance();
			case DORMAND_PRINCE_54_SOLVER:
				return DormandPrince54Solver.class.newInstance();
			case DORMAND_PRINCE_85_SOLVER:
				return DormandPrince853Solver.class.newInstance();
			case GRAGG_BULIRISCH_STOER_SOLVER:
				return GraggBulirschStoerSolver.class.newInstance();
			case HIGHAM_HALL_54_SOLVER:
				return HighamHall54Solver.class.newInstance();
			case EULER_METHOD:
				return EulerMethod.class.newInstance();
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
