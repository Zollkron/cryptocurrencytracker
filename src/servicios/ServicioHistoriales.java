package servicios;

import org.json.simple.JSONObject;

import modelo.Historial;

public class ServicioHistoriales {
	
	public Historial convertirJSON(Object obj) {
		Historial historial = new Historial(); 
		historial.setExchange((String) ((JSONObject) obj).get("Exchange"));
		historial.setQuantity((Double) ((JSONObject) obj).get("Quantity"));
		try {
			historial.setQuantityRemaining((Double) ((JSONObject) obj).get("QuantityRemaining"));
			historial.setLimit((Double) ((JSONObject) obj).get("Limit"));
		} catch (Exception e) {
			historial.setQuantityRemaining((double) 0);
			historial.setLimit((double) 0);
		}
		historial.setPrice((Double) ((JSONObject) obj).get("Price"));
		historial.setOrderType((String) ((JSONObject) obj).get("OrderType"));
		historial.setOrderUuid((String) ((JSONObject) obj).get("OrderUuid"));
		historial.setTimeStamp((String) ((JSONObject) obj).get("TimeStamp"));
		return historial;
	}

}
