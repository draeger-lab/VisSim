package org.insilico.vissim.core.editor;

import java.io.IOException;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.core.table.TableType;
import org.insilico.vissim.core.table.VisSimTableFactory;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VisSim.fxml"));
		hideUnusedParts();
		BorderPane simulationPane = null;
		try {
			simulationPane = (BorderPane) loader.load();
		} catch (IOException e) {
			new DialogUtils().showConfirmationDialog("Initialization error", "Something went wrong.",
					"JavaFx is not found. Check if JFX is a part of JRE", AlertType.ERROR, e);
		}
		final LineChart<Number, Number> lineChart = initChart(simulationResult);
		Accordion bottom = (Accordion) simulationPane.getBottom();
		TitledPane titledPane = bottom.getPanes().get(0);
		titledPane.setText("Details: " + lineChart.getTitle());
		TableView<?> valuesTable = VisSimTableFactory.getValuesTable(TableType.VALUES_TABLE, simulationResult);
		titledPane.setContent(valuesTable);
		simulationPane.setCenter(lineChart);
		parent.setCenter(simulationPane);
	}

	/**
	 * Hide unused workbench parts
	 */
	private void hideUnusedParts() {
		MPartStack stack = (MPartStack) service.find("org.insilico.ui.partstack.0", application);
		stack.setVisible(false);
		MPartStack stack1 = (MPartStack) service.find("org.insilico.ui.partstack.1", application);
		stack1.setVisible(false);
	}

	/**
	 * Initialize jfx chart for SimulationResult
	 */
	@SuppressWarnings("unchecked")
	private LineChart<Number, Number> initChart(SimulationResult simRes) {
		final NumberAxis timePointsAxis = new NumberAxis(0, simRes.getTimePoints().length - 1, 1);
		final NumberAxis valuesAxis = new NumberAxis();
		final LineChart<Number, Number> chart = new LineChart<>(timePointsAxis, valuesAxis);
		for (Layer layer : simRes.getLayers()) {
			chart.setTitle(layer.getLayerName());
			LinkedList<Quantity> quantities = layer.getQuantities();
			for (Quantity quantity : quantities) {
				Series<Number, Number> lineChart = new XYChart.Series<Number, Number>();
				lineChart.setName(quantity.getQuantityName());
				ObservableList<Data<Number, Number>> chartData = lineChart.getData();
				for (int i = 0; i < simRes.getTimePoints().length; i++) {
					chartData
							.add(new XYChart.Data<Number, Number>(simRes.getTimePoints()[i], quantity.getResults()[i]));
				}
				chart.getData().addAll(lineChart);
			}
		}
		return chart;
	}


}
