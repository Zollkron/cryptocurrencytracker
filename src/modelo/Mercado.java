package modelo;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Mercado {
	
	private SimpleStringProperty marketName;
	private SimpleDoubleProperty last;
	private SimpleDoubleProperty volume;
	private SimpleDoubleProperty bid;
	private SimpleDoubleProperty ask;
	private SimpleDoubleProperty high;
	private SimpleDoubleProperty low;

	public Mercado() {
		marketName = new SimpleStringProperty(null);
		last = new SimpleDoubleProperty(0);
		volume = new SimpleDoubleProperty(0);
		bid = new SimpleDoubleProperty(0);
		ask = new SimpleDoubleProperty(0);
		high = new SimpleDoubleProperty(0);
		low = new SimpleDoubleProperty(0);
	}

	public Mercado(String marketName, Double last, Double volume, Double bid, Double ask, Double high, Double low) {
		this.marketName = new SimpleStringProperty(marketName);
		this.last = new SimpleDoubleProperty(last);
		this.volume = new SimpleDoubleProperty(volume);
		this.bid = new SimpleDoubleProperty(bid);
		this.ask = new SimpleDoubleProperty(ask);
		this.high = new SimpleDoubleProperty(high);
		this.low = new SimpleDoubleProperty(low);
	}

	public String getMarketName() {
		return marketName.get();
	}

	public void setMarketName(String marketName) {
		this.marketName.set(marketName);
	}

	public Double getLast() {
		return last.get();
	}

	public void setLast(Double last) {
		this.last.set(last);
	}

	public Double getVolume() {
		return volume.get();
	}

	public void setVolume(Double volume) {
		this.volume.set(volume);
	}

	public Double getBid() {
		return bid.get();
	}

	public void setBid(Double bid) {
		this.bid.set(bid);
	}

	public Double getAsk() {
		return ask.get();
	}

	public void setAsk(Double ask) {
		this.ask.set(ask);
	}
	
	public Double getHigh() {
		return high.get();
	}

	public void setHigh(Double high) {
		this.high.set(high);
	}

	public Double getLow() {
		return low.get();
	}

	public void setLow(Double low) {
		this.low.set(low);
	}
}
