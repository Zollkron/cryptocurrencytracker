package modelo;

import javafx.beans.property.SimpleDoubleProperty;

public class Orden {

	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty rate;

	public Orden() {
		quantity = new SimpleDoubleProperty(0);
		rate = new SimpleDoubleProperty(0);
	}

	public Orden(Double quantity, Double rate) {
		this.quantity = new SimpleDoubleProperty(quantity);
		this.rate = new SimpleDoubleProperty(rate);
	}

	public Double getQuantity() {
		return Math.round(quantity.get() * 100000d) / 100000d;
	}

	public void setQuantity(Double quantity) {
		this.quantity.set(quantity);
	}

	public Double getRate() {
		return rate.get();
	}

	public void setRate(Double rate) {
		this.rate.set(rate);
	}
}
