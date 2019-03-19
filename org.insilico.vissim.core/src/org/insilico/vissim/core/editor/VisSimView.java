package org.insilico.vissim.core.editor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.insilico.ui.utils.DialogUtils;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.result.Layer;
import org.insilico.vissim.sbscl.result.Quantity;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
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
		titledPane.setContent(initTable(simulationResult));
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

	/**
	 * Initialize TableView from SimulationResult
	 */
	private TableView<double[]> initTable(SimulationResult result) {
		ObservableList<double[]> data = rearrangeData(result.getLayers().get(0),
				result.getLayers().get(0).getQuantities());
		TableView<double[]> table = new TableView<>(data);
		table.getColumns().setAll(createColumns(result.getLayers().get(0).getQuantities().get(0)));
		return table;
	}

	/**
	 * Reshuffle simulation values into corresponding columns
	 */
	private ObservableList<double[]> rearrangeData(Layer layer, LinkedList<Quantity> quantities) {
		return FXCollections
				.observableArrayList(IntStream.range(0, layer.getQuantities().size())
						.mapToObj(r -> IntStream.range(0, quantities.get(r).getResults().length)
								.mapToDouble(c -> quantities.get(r).getResults()[c]).toArray())
						.collect(Collectors.toList()));
	}

	/**
	 * Initialize table header
	 */
	private List<TableColumn<double[], Double>> createColumns(Quantity q) {
		return IntStream.range(0, q.getResults().length).mapToObj(this::createColumn).collect(Collectors.toList());
	}

	/**
	 * Create new column
	 */
	private TableColumn<double[], Double> createColumn(int c) {
		TableColumn<double[], Double> col = new TableColumn<>(c + "");
		col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[c]));
		return col;
	}
}
