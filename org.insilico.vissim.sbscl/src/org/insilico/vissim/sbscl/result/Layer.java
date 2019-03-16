package org.insilico.vissim.sbscl.result;

import java.util.LinkedList;

public class Layer {
	private LinkedList<Quantity> quantities;

	public LinkedList<Quantity> getQuantities() {
		return quantities;
	}

	public void setQuantities(LinkedList<Quantity> quantities) {
		this.quantities = quantities;
	}

	public void addQuantity(Quantity quantity) {
		this.quantities.add(quantity);
	}
}
