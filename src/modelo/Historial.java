package modelo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Historial {

	private SimpleStringProperty exchange;
	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty quantityRemaining;
	private SimpleDoubleProperty limit;
	private SimpleDoubleProperty price;
	private SimpleStringProperty orderType;
	private SimpleStringProperty orderUuid;
	private SimpleStringProperty timeStamp;

	public Historial() {
		exchange = new SimpleStringProperty(null);
		quantity = new SimpleDoubleProperty(0);
		quantityRemaining = new SimpleDoubleProperty(0);
		limit = new SimpleDoubleProperty(0);
		price = new SimpleDoubleProperty(0);
		orderType = new SimpleStringProperty(null);
		orderUuid = new SimpleStringProperty(null);
		timeStamp = new SimpleStringProperty(null);
	}

	public Historial(String exchange, Double quantity, Double quantityRemaining, Double limit, Double price, 
			String orderType, String orderUuid, String timeStamp) {
		this.exchange = new SimpleStringProperty(exchange);
		this.quantity = new SimpleDoubleProperty(quantity);
		this.quantityRemaining = new SimpleDoubleProperty(quantityRemaining);
		this.limit = new SimpleDoubleProperty(limit);
		this.price = new SimpleDoubleProperty(price);
		this.orderType = new SimpleStringProperty(orderType);
		this.orderUuid = new SimpleStringProperty(orderUuid);
		this.timeStamp = new SimpleStringProperty(timeStamp);
	}

	public String getExchange() {
		return exchange.get();
	}

	public void setExchange(String exchange) {
		this.exchange.set(exchange);
	}
	
	public Double getQuantity() {
		return Math.round(quantity.get() * 100000d) / 100000d;
	}

	public void setQuantity(Double quantity) {
		this.quantity.set(quantity);
	}
	
	public Double getQuantityRemaining() {
		return Math.round(quantityRemaining.get() * 100000d) / 100000d;
	}

	public void setQuantityRemaining(Double quantityRemaining) {
		this.quantityRemaining.set(quantityRemaining);
	}

	public Double getLimit() {
		return limit.get();
	}

	public void setLimit(Double limit) {
		this.limit.set(limit);
	}
	
	public Double getPrice() {
		return price.get();
	}

	public void setPrice(Double price) {
		this.price.set(price);
	}
	
	public String getOrderType() {
		return orderType.get();
	}

	public void setOrderType(String orderType) {
		this.orderType.set(orderType);
	}
	
	public String getOrderUuid() {
		return orderUuid.get();
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid.set(orderUuid);
	}
	
	public String getTimeStamp() {
		return timeStamp.get();
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp.set(timeStamp);
	}
}
