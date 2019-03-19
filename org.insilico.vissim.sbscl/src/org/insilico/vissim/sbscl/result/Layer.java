package org.insilico.vissim.sbscl.result;

import java.util.LinkedList;

public class Layer {
	private String layerName;
	private LinkedList<Quantity> quantities = new LinkedList<Quantity>();

	public LinkedList<Quantity> getQuantities() {
		return quantities;
	}

	public void setQuantities(LinkedList<Quantity> quantities) {
		this.quantities = quantities;
	}

	public void addQuantity(Quantity quantity) {
		this.quantities.add(quantity);
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
}
