package org.insilico.vissim.sbscl.simulation;

import javax.inject.Inject;

import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.fx.core.ThreadSynchronize;
import org.insilico.vissim.sbscl.factory.SimulationResult;

/**
 * Base class for VisSim simulation.
 * 
 */
abstract public class AbstractSimulation {
	@Inject
	ThreadSynchronize sync;
	

	public void run(String path) {
		Job job = Job.create("Run simulation...", (ICoreRunnable) monitor -> {
			try {
				simulate(path);
			} catch (Exception e) {
				sync.asyncExec(() -> {
					// inform UI
				});
			}
		});
		job.schedule();
	}

	public abstract SimulationResult simulate(String path) throws Exception;
}
