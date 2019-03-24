package org.insilico.vissim.core.editor;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.core.Messages;
import org.insilico.vissim.core.chart.ChartType;
import org.insilico.vissim.core.chart.VisSimChartFactory;
import org.insilico.vissim.core.services.UIManager;
import org.insilico.vissim.core.table.TableType;
import org.insilico.vissim.core.table.VisSimTableFactory;
import org.insilico.vissim.sbscl.factory.SimulationResult;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

/**
 * VisSim main view
 */
public class VisSimView {

	@Inject
	SimulationResult simulationResult;

	@Inject
	EModelService service;

	@Inject
	MApplication application;

	@PostConstruct
	private void init(BorderPane parent) {
		UIManager.hideBottomWorkbenchPart(service, application);
		UIManager.hideRightWorkbenchPart(service, application);
		BorderPane simulationPane = loadFXML(Messages.fxml_file);
		setSimulationTable(VisSimTableFactory.getValuesTable(TableType.VALUES_TABLE, simulationResult), "Values", simulationPane); //$NON-NLS-1$
		simulationPane.setCenter(VisSimChartFactory.getValuesTable(ChartType.LINE_CHART, simulationResult));
		parent.setCenter(simulationPane);
	}

	/**
	 * Show specified {@code TableView} at the bottom of the workbench; {@code TableView} will be wrapped
	 * into {@link Accordion}
	 * */
	private void setSimulationTable(TableView<?> table, String tableName, BorderPane simulationPane) {
		Accordion bottom = (Accordion) simulationPane.getBottom();
		TitledPane titledPane = bottom.getPanes().get(0);
		titledPane.setText(tableName + simulationResult.getLayers().getFirst().getLayerName());
		titledPane.setContent(table);
	}

	/**
	 * Load corresponding FXML configuration file. Show error dialog if not
	 * possible.
	 */
	private BorderPane loadFXML(String path) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		BorderPane simulationPane = null;
		try {
			simulationPane = (BorderPane) loader.load();
		} catch (IOException e) {
			new DialogUtils().showConfirmationDialog(Messages.init_error, Messages.info_general, //$NON-NLS-1$ //$NON-NLS-2$
					Messages.jfx_not_installed, AlertType.ERROR, e);
		}
		return simulationPane;
	}
}
