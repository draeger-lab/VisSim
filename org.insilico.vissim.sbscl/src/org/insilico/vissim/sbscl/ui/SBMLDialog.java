package org.insilico.vissim.sbscl.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.sbml.jsbml.SBMLDocument;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * <p>
 * A Dialog in JavaFX wraps a {@link DialogPane} and provides the necessary API
 * to present options for simulating {@link SBMLDocument}. In JavaFX 8u40 this
 * essentially means that the {@link DialogPane} is shown to users inside a
 * {@link Stage}.
 * </p>
 * <p>
 * Time stamps related data is saved in static manner to ensure consistency
 * while another instance of this controller can be created from another view
 * implementation.
 * </p>
 *
 */
public class SBMLDialog {

	private static final String HELP_URI = "https://draeger-lab.github.io/SBSCL/apidocs/overview-summary.html";

	@FXML
	private javafx.scene.control.Button cancelButton;

	@FXML
	private javafx.scene.control.RadioButton diffSimRButton;

	@FXML
	private javafx.scene.control.RadioButton sedSimRButton;

	@FXML
	private javafx.scene.control.TextField timePointsTextField;

	@FXML
	private javafx.scene.control.TextField durationTextField;

	@FXML
	private javafx.scene.control.Hyperlink help;
	
	@FXML
	private ChoiceBox<String> solverChoiceBox;

	private Dialog<Pair<String, String>> timingDialog = new Dialog<>();

	private final static int DEFAULT_TIMEPOINTS_NUMBER = 25;
	private final static double DEFAULT_TIMEPOINT_DURATION = 1d;
	private final static String ROSENBOCK_SOLVER = "RosenbockSolver";

	private static int timepointsNumber = DEFAULT_TIMEPOINTS_NUMBER;
	private static double timepointDuration = DEFAULT_TIMEPOINT_DURATION;
	private static String solverType = ROSENBOCK_SOLVER;
	private static boolean isCanceled = false;

	/**
	 * Initialize corresponding dialog from FXML template
	 * This resets static time stamps
	 * 
	 * */
	public void initDialog() {
		resetValues();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SBMLDialog.fxml"));
		GridPane grid = null;
		try {
			grid = (GridPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timingDialog.setTitle("SBML Simulation");
		timingDialog.getDialogPane().setContent(grid);
		timingDialog.showAndWait();
	}

	/**
	 * Closes the dialog and saves provided values
	 * */
	public void fireOK() {
		if (!timePointsTextField.textProperty().get().trim().isEmpty()) {
			timepointsNumber = Integer.parseInt(timePointsTextField.textProperty().get().trim());
		}
		if (!durationTextField.textProperty().get().trim().isEmpty()) {
			timepointDuration = Double.parseDouble(durationTextField.textProperty().get().trim());
		}
		setSolverType(solverChoiceBox.getValue().toString());
		close();
	}

	/**
	 * Abort simulation for selected file
	 * */
	public void fireCancel() {
		setCanceled(true);
		close();
	}

	/**
	 * Closes related Stage
	 * */
	private void close() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Provide help documentation about simulation in general. Official SBSCL web resource will be shown.
	 * Cross-platform implementation. Works for almost all "known" browsers.
	 * */
	public void showHelp() {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(HELP_URI));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Runtime rt = Runtime.getRuntime();
			String[] browsers = { "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx", "safari" };

			StringBuffer cmd = new StringBuffer();
			for (int i = 0; i < browsers.length; i++)
				if (i == 0)
					cmd.append(String.format("%s \"%s\"", browsers[i], HELP_URI));
				else
					cmd.append(String.format(" || %s \"%s\"", browsers[i], HELP_URI));
			try {
				rt.exec(new String[] { "sh", cmd.toString() });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Resets time stamps
	 * */
	public void resetValues() {
		timepointsNumber = DEFAULT_TIMEPOINTS_NUMBER;
		timepointDuration = DEFAULT_TIMEPOINT_DURATION;
	}

	/**
	 * Returns amount of time points
	 * */
	public int getTimepointsNumber() {
		return timepointsNumber;
	}

	/**
	 * Returns one time stamp duration
	 * */
	public double getTimepointDuration() {
		return timepointDuration;
	}
	
	public static boolean isCanceled() {
		return isCanceled;
	}

	public static void setCanceled(boolean isCanceled) {
		SBMLDialog.isCanceled = isCanceled;
	}

	/**
	 * @return the solverType
	 */
	public static String getSolverType() {
		return solverType;
	}

	/**
	 * @param solverType the solverType to set
	 */
	public static void setSolverType(String solverType) {
		SBMLDialog.solverType = solverType;
	}
}
