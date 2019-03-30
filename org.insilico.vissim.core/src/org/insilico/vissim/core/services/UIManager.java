package org.insilico.vissim.core.services;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * Provides functionality to control inSilico UI parts
 **/
public class UIManager {
	
	static MApplication app;
	static EModelService service;
	
	/**
	 * Hides right unused workbench parts
	 */
	public static void hideRightWorkbenchPart() {
		MPartStack stack1 = (MPartStack) service.find("org.insilico.ui.partstack.0", app);
		stack1.setVisible(false);
	}
	
	/**
	 * Hides bottom unused workbench parts
	 */
	public static void hideBottomWorkbenchPart() {
		MPartStack stack1 = (MPartStack) service.find("org.insilico.ui.partstack.1", app);
		stack1.setVisible(false);
	}
	
	/**
	 * Hides navigation explorer
	 */
	public static void hideNavigationExplorer() {
		MPartStack part = (MPartStack) service.find("org.insilico.ui.stack.nav.primary", app);
		if (part.isVisible()) {
			part.setVisible(false);
		} else {
			part.setVisible(true);
		}
	}
	
	public static void setControls(MApplication app, EModelService service) {
		UIManager.app = app;
		UIManager.service = service;
	}
}
