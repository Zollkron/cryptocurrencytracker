package servicios;

import org.json.simple.JSONObject;

import modelo.Mercado;

public class ServicioMercados {
	
	public Mercado convertirJSON(Object obj) {
		Mercado mercado = new Mercado(); 
		mercado.setMarketName((String) ((JSONObject) obj).get("MarketName"));
		mercado.setLast((Double) ((JSONObject) obj).get("Last"));
		mercado.setVolume((Double) ((JSONObject) obj).get("BaseVolume"));
		mercado.setBid((Double) ((JSONObject) obj).get("Bid"));
		mercado.setAsk((Double) ((JSONObject) obj).get("Ask"));
		mercado.setHigh((Double) ((JSONObject) obj).get("High"));
		mercado.setLow((Double) ((JSONObject) obj).get("Low"));
		return mercado;
	}

}
