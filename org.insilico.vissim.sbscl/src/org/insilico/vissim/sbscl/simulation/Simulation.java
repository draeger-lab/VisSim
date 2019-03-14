package org.insilico.vissim.sbscl.simulation;

import javax.inject.Inject;

import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.fx.core.ThreadSynchronize;

/**
 * Base class for VisSim simulation.
 * 
 * */
abstract public class Simulation {
	@Inject
	ThreadSynchronize sync;

	public void run() {
		Job job = Job.create("Run simulation...", (ICoreRunnable) monitor -> {
			simulate();
			sync.asyncExec(() -> {
				// inform UI
			});
		});
		job.schedule();
	}

	public abstract SimulationResult simulate();
}
