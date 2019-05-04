package org.insilico.vissim.core.table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.insilico.vissim.core.Messages;
import org.insilico.vissim.sbscl.factory.SimulationResult;
import org.insilico.vissim.sbscl.result.Quantity;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;


/**
 * Provides {@code TableView} for values distribution of some {@code SimulationResult} 
 * */
public class ValuesTableBuilder implements TableBuilder {
	final static int CHECK_BOX_COLUMN = -2;
	final static int NAME_COLUMN = -1;
	static double tickTime;

	/**
	 * Initialize TableView from SimulationResult
	 * @return TableView parameterized with {@link Quantity}
	 */
	public TableView<?> buildTable(SimulationResult result) {
		tickTime = result.getTimePoints()[1];
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
		return IntStream.range(NAME_COLUMN, q.getResults().length).mapToObj(this::createColumn)
				.collect(Collectors.toList());
	}

	/**
	 * Create new column depending on the placing number
	 * All helper columns should have negative id's
	 */
	private TableColumn<Quantity, ?> createColumn(int c) {
		if (c == CHECK_BOX_COLUMN) {
			TableColumn<Quantity, Boolean> col = new TableColumn<Quantity, Boolean>(""); //$NON-NLS-1$
			setFactoriesForCheckBoxCol(col);
			return col;
		} else if (c == NAME_COLUMN) {
			TableColumn<Quantity, String> col = new TableColumn<>(Messages.table_name_col);
			col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getQuantityName()));
			return col;
		} else {
			TableColumn<Quantity, String> col = new TableColumn<>(round(c * tickTime, 1) + " sec."); //$NON-NLS-1$
			col.setPrefWidth(75);
			col.setCellValueFactory(
					param -> new ReadOnlyObjectWrapper<String>(Double.toString(param.getValue().getResults()[c])));
			return col;
		}
	}

	/**
	 * Set ups for additional checkbox column. Contributes separate checkbox for
	 * every column in the table. Should be connected with corresponding
	 * {@link Quantity}.
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
	
	/**
	 * Helper function to round doubles.
	 * */
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
