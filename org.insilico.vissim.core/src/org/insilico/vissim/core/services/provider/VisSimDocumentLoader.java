package org.insilico.vissim.core.services.provider;

import static org.eclipse.fx.code.editor.Constants.DOCUMENT_URL;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.IInjector;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.core.Messages;
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
				SimulationType simulationType = new Utils().getSimulationType(urlVal.toString().replace("file:", "")); //$NON-NLS-1$ //$NON-NLS-2$
				if (SimulationType.UNKNOWN_ENTITY.equals(simulationType)) {
					new DialogUtils().showConfirmationDialog(Messages.unknown_simulation_type_error, Messages.info_general, //$NON-NLS-2$
							Messages.unknown_data_type_error, AlertType.ERROR);
					return new SimulationRandomizer().initRandomSimulationResult();
				} else {
					AbstractSimulation simulation = factory.getSimulation(simulationType);
					return simulation.simulate(urlVal.toString().replace("file:", ""));
				}
			} catch (Exception e) {
				new DialogUtils().showConfirmationDialog(Messages.simulation_crashed_error, Messages.info_general, //$NON-NLS-2$
						Messages.sbscl_not_found_error, AlertType.ERROR, e);
				return new SimulationRandomizer().initRandomSimulationResult();
			}
		}
		return IInjector.NOT_A_VALUE;
	}
}
