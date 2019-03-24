package org.insilico.vissim.core.table;

import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

/**
 * Factory class for appropriate {@code TableView} initialization
 * */
public class VisSimTableFactory {
	
	static public TableView<?> getValuesTable(TableType type, SimulationResult result) {
			switch (type) {
			case VALUES_TABLE:
				try {
					return ValuesTableBuilder.class.newInstance().buildTable(result);
				} catch (Exception e) {
					new DialogUtils().showConfirmationDialog("Initialization error", "Something went wrong.",
							"Corresponding table con not be initialized", AlertType.ERROR, e);
				}
			default:
				return null;
			}
	}
}
