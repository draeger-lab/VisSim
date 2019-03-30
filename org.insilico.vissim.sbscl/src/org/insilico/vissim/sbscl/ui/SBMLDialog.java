package org.insilico.vissim.sbscl.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

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

	private Dialog<Pair<String, String>> timingDialog = new Dialog<>();

	private final static int DEFAULT_TIMEPOINTS_NUMBER = 25;
	private final static double DEFAULT_TIMEPOINT_DURATION = 1d;

	private static int timepointsNumber = DEFAULT_TIMEPOINTS_NUMBER;
	private static double timepointDuration = DEFAULT_TIMEPOINT_DURATION;
	private static boolean isCanceled = false;

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

	public void fireCancel() {
		setCanceled(true);
		close();
	}

	private void close() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void fireOK() {
		if (!timePointsTextField.textProperty().get().trim().isEmpty()) {
			timepointsNumber = Integer.parseInt(timePointsTextField.textProperty().get().trim());
		}
		if (!durationTextField.textProperty().get().trim().isEmpty()) {
			timepointDuration = Double.parseDouble(durationTextField.textProperty().get().trim());
		}
		close();
	}

	public void showHelp() {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(HELP_URI));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Runtime rt = Runtime.getRuntime();
			String[] browsers = {"firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx" };

			StringBuffer cmd = new StringBuffer();
			for (int i = 0; i < browsers.length; i++)
				if (i == 0)
					cmd.append(String.format("%s \"%s\"", browsers[i], HELP_URI));
				else
					cmd.append(String.format(" || %s \"%s\"", browsers[i], HELP_URI));
			try {
				rt.exec(new String[] { "sh", cmd.toString()});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void resetValues() {
		timepointsNumber = DEFAULT_TIMEPOINTS_NUMBER;
		timepointDuration = DEFAULT_TIMEPOINT_DURATION;
	}

	public int getTimepointsNumber() {
		return timepointsNumber;
	}

	public double getTimepointDuration() {
		return timepointDuration;
	}

	public static boolean isCanceled() {
		return isCanceled;
	}

	public static void setCanceled(boolean isCanceled) {
		SBMLDialog.isCanceled = isCanceled;
	}
}
