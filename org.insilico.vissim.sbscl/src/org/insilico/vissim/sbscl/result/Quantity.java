package org.insilico.vissim.sbscl.result;

public class Quantity {
	private String quantityName;
	private double[] results;
	private boolean isShown = true;

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

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}
}
