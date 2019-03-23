package org.insilico.vissim.core.services;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * Provides functionality to control inSilico UI parts
 **/
public class UIManager {
	
	/**
	 * Hides right unused workbench parts
	 */
	public static void hideRightWorkbenchPart(EModelService service, MApplication app) {
		MPartStack stack1 = (MPartStack) service.find("org.insilico.ui.partstack.0", app);
		stack1.setVisible(false);
	}
	
	/**
	 * Hides bottom unused workbench parts
	 */
	public static void hideBottomWorkbenchPart(EModelService service, MApplication app) {
		MPartStack stack1 = (MPartStack) service.find("org.insilico.ui.partstack.1", app);
		stack1.setVisible(false);
	}
}
