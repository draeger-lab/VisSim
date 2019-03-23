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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

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
	private TableView<Quantity> initTable(SimulationResult result) {
		ObservableList<Quantity> data = rearrangeData(result.getLayers().get(0).getQuantities());
		TableView<Quantity> table = new TableView<>(data);

		table.getColumns().setAll(createColumns(result.getLayers().get(0).getQuantities().get(0)));
		return table;
	}


	/**
	 * Reshuffle simulation values into corresponding columns
	 */
	private ObservableList<Quantity> rearrangeData(LinkedList<Quantity> quantities) {
		return FXCollections.observableArrayList(quantities);
	}

	/**
	 * Initialize table header
	 */
	private List<TableColumn<Quantity, ?>> createColumns(Quantity q) {
		return IntStream.range(-2, q.getResults().length).mapToObj(this::createColumn).collect(Collectors.toList());
	}

	/**
	 * Create new column
	 */
	private TableColumn<Quantity, ?> createColumn(int c) {
		if (c == -2) {
			TableColumn<Quantity, Boolean> col = new TableColumn<Quantity, Boolean>("");
			setFactoriesForCheckBoxCol(col);
			return col;
		} else if (c == -1) {
			TableColumn<Quantity, String> col = new TableColumn<>("Name");
			col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getQuantityName()));
			return col;
		} else {
			TableColumn<Quantity, String> col = new TableColumn<>(c + "");
			col.setCellValueFactory(
					param -> new ReadOnlyObjectWrapper<String>(Double.toString(param.getValue().getResults()[c])));
			return col;
		}
	}

	/**
	 * Set ups for additional checkbox column. Contributes separate checkbox for every column in
	 * the table. Should be connected with corresponding {@link Quantity}.
	 * 
	 */
	private void setFactoriesForCheckBoxCol(TableColumn<Quantity, Boolean> col) {
		col.setCellValueFactory(new Callback<CellDataFeatures<Quantity, Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Quantity, Boolean> param) {
				Quantity quantity = param.getValue();

				SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(quantity.isShown());
				booleanProp.addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						quantity.setShown(newValue);
					}
				});
				return booleanProp;
			}
		});

		col.setCellFactory(new Callback<TableColumn<Quantity, Boolean>, TableCell<Quantity, Boolean>>() {
			@Override
			public TableCell<Quantity, Boolean> call(TableColumn<Quantity, Boolean> p) {
				CheckBoxTableCell<Quantity, Boolean> cell = new CheckBoxTableCell<Quantity, Boolean>();
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		});
	}
}
