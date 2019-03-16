package org.insilico.vissim.sbscl.result;

public class Quantity {
	private String quantityName;
	private double[] results;

	public Quantity(String quantityName, double[] results) {
		super();
		this.quantityName = quantityName;
		this.results = results;
	}

	public String getQuantityName() {
		return quantityName;
	}

	public void setQuantityName(String quantityName) {
		this.quantityName = quantityName;
	}

	public double[] getResults() {
		return results;
	}

	public void setResults(double[] results) {
		this.results = results;
	}
}
