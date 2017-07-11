package modelo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Cartera {

	private SimpleStringProperty currency;
	private SimpleDoubleProperty balance;
	private SimpleDoubleProperty available;
	private SimpleDoubleProperty pending;
	private SimpleStringProperty cryptoAddress;

	public Cartera() {
		currency = new SimpleStringProperty(null);
		balance = new SimpleDoubleProperty(0);
		available = new SimpleDoubleProperty(0);
		pending = new SimpleDoubleProperty(0);
		cryptoAddress = new SimpleStringProperty(null);
	}

	public Cartera(String currency, Double balance, Double available, Double pending, String cryptoAddress) {
		this.currency = new SimpleStringProperty(currency);
		this.balance = new SimpleDoubleProperty(balance);
		this.available = new SimpleDoubleProperty(available);
		this.pending = new SimpleDoubleProperty(pending);
		this.cryptoAddress = new SimpleStringProperty(cryptoAddress);
	}

	public String getCurrency() {
		return currency.get();
	}

	public void setCurrency(String currency) {
		this.currency.set(currency);
	}

	public Double getBalance() {
		return balance.get();
	}

	public void setBalance(Double balance) {
		this.balance.set(balance);
	}

	public Double getAvailable() {
		return available.get();
	}

	public void setAvailable(Double available) {
		this.available.set(available);
	}

	public Double getPending() {
		return pending.get();
	}

	public void setPending(Double pending) {
		this.pending.set(pending);
	}

	public String getCryptoAddress() {
		return cryptoAddress.get();
	}

	public void setCryptoAddress(String cryptoAddress) {
		this.cryptoAddress.set(cryptoAddress);
	}
	
	public SimpleStringProperty getCryptoAddressProperty() {
		return cryptoAddress;
	}
}
