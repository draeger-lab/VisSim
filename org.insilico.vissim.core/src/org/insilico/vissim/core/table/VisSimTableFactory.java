package org.insilico.vissim.core.table;

import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.scene.control.TableView;

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
					e.printStackTrace();
				}

			default:
				return null;
			}
	}
}
