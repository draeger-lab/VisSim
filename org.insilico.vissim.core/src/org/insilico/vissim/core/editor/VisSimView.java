package org.insilico.vissim.core.editor;

import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

/**
 * VisSim main view
 * */
public class VisSimView {

	private static final int GEN_NUMS_COUNT = 25;

	@Inject
	EModelService service;

	@Inject
	MApplication application;

	@PostConstruct
	private void init(BorderPane parent) {
		// XXX: Remove dummy example
		MPartStack stack = (MPartStack) service.find("org.insilico.ui.partstack.0", application);
		stack.setVisible(false);
		MPartStack stack1 = (MPartStack) service.find("org.insilico.ui.partstack.1", application);
		stack1.setVisible(false);
		final LineChart<Number, Number> ac = initChart();
		parent.setCenter(ac);
	}

	/**
	 * JFX chart to be removed
	 * */
	@SuppressWarnings("unchecked")
	private LineChart<Number,Number> initChart() {
		final NumberAxis xAxis = new NumberAxis(1, GEN_NUMS_COUNT - 1, 1);
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<Number, Number> ac = new LineChart<>(xAxis, yAxis);
		ac.setTitle("Fully irrelevant title");
		XYChart.Series<Number, Number> dataStreamOrange = new XYChart.Series<Number, Number>();
		XYChart.Series<Number, Number> dataStreamBlueYellow = new XYChart.Series<Number, Number>();
		dataStreamOrange.setName("Fully irrelevant data");
		dataStreamBlueYellow.setName("Even more irrelevant data");
		Random rand = new Random();
		genRandomData(dataStreamOrange, dataStreamBlueYellow, rand);
		ac.getData().addAll(dataStreamOrange, dataStreamBlueYellow);
		return ac;
	}

	/**
	 * Generates random data for the LineChart
	 * */
	private void genRandomData(XYChart.Series<Number, Number> dataStreamOrange,
			XYChart.Series<Number, Number> dataStreamBlueYellow, Random rand) {
		for (int i = 0; i < GEN_NUMS_COUNT - 2; i++) {
			dataStreamOrange.getData().add(new XYChart.Data<Number, Number>(rand.nextInt(GEN_NUMS_COUNT), rand.nextInt(GEN_NUMS_COUNT)));
			dataStreamBlueYellow.getData().add(new XYChart.Data<Number, Number>(rand.nextInt(GEN_NUMS_COUNT), rand.nextInt(GEN_NUMS_COUNT)));
		}
	}
}
