package org.insilico.vissim.core.services.provider;

import static org.eclipse.fx.code.editor.Constants.DOCUMENT_URL;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.IInjector;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.sbscl.factory.SimulationFactory;
import org.insilico.vissim.sbscl.simulation.AbstractSimulation;
import org.insilico.vissim.sbscl.simulation.SimulationType;
import org.insilico.vissim.sbscl.utils.SimulationRandomizer;
import org.insilico.vissim.sbscl.utils.Utils;
import org.osgi.service.component.annotations.Component;

import javafx.scene.control.Alert.AlertType;

/**
 * Provides a data structure to be visualized if the current document is a
 * <placeholder> file.
 * 
 * <p>
 * Use {@link org.eclipse.fx.code.editor.Constants#DOCUMENT_URL} and
 * {@link org.insilico.vissim.core.Constants#KEY_SBSCL_DOCUMENT} to retrieve
 * corresponding data.
 * </p>
 *
 */
@SuppressWarnings("restriction")
@Component(service = IContextFunction.class, property = { "org.insilico.vissim.sbscl.factory.SimulationResult" })
public class VisSimDocumentLoader extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		Object urlVal = context.get(DOCUMENT_URL);
		if (urlVal == null) {
			Object partVal = context.get(MPart.class);
			if (partVal != null && partVal instanceof MPart) {
				MPart part = (MPart) partVal;
				urlVal = part.getPersistedState().get(DOCUMENT_URL);
				context.set(DOCUMENT_URL, urlVal);
			}
		}
		if (urlVal != null) {
			try {
				SimulationFactory factory = new SimulationFactory();
				SimulationType simulationType = new Utils().getSimulationType(urlVal.toString().replace("file:", ""));
				if (SimulationType.UNKNOWN_ENTITY.equals(simulationType)) {
					new DialogUtils().showConfirmationDialog("Unknown simulation type", "Something went wrong.",
							"This type of data can not be simulated", AlertType.ERROR);
					return new SimulationRandomizer().initRandomSimulationResult();
				} else {
					AbstractSimulation simulation = factory.getSimulation(simulationType);
					return simulation.simulate(urlVal.toString());
				}
			} catch (Exception e) {
				new DialogUtils().showConfirmationDialog("Simulation crashed", "Something went wrong.",
						"Check if SBSCL library properly referenced", AlertType.ERROR, e);
				return new SimulationRandomizer().initRandomSimulationResult();
			}
		}
		return IInjector.NOT_A_VALUE;
	}
}
