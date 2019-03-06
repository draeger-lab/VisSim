package org.insilico.vissim.core.services.provider;

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.IInjector;
import org.osgi.service.component.annotations.Component;
import org.sbml.jsbml.SBMLDocument;

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
@Component(service = IContextFunction.class, property = { "service.context.key=org.simulator.math.odes.MultiTable" })
public class VisSimDocumentLoader extends ContextFunction {
	Map<String, SBMLDocument> cache = new WeakHashMap<>();

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		// Add corresponding checks for OMEX, SED-ML, SBML
		// Potentially should be divided into separate logical segments
		// SED-ML Level/Version combination checks
		return IInjector.NOT_A_VALUE;
	}

}
