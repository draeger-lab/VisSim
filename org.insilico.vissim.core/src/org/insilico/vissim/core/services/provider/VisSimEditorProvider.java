package org.insilico.vissim.core.services.provider;

import org.eclipse.fx.code.editor.fx.e4.EditorClassURLProvider;
import org.insilico.vissim.core.editor.VisSimView;
import org.osgi.service.component.annotations.Component;

/**
 * Provides an editor part for <placeholder> files. 
 * @see {@link org.eclipse.fx.code.editor.fx.e4.EditorClassURLProvider}
 */
@Component
public class VisSimEditorProvider implements EditorClassURLProvider {
	private final static String[] DATA_TYPE = {"xml", "zip"};
	private final static String CORE_BUNDLE = "bundleclass://org.insilico.vissim.core/";

	@Override
	public boolean test(String t) {
		return t.endsWith(DATA_TYPE[0]) || t.endsWith(DATA_TYPE[1]);
	}

	@Override
	public String getBundleClassURI(String uri) {
		return CORE_BUNDLE + VisSimView.class.getName();
	}

}
