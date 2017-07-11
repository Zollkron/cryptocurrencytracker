package servicios;

import org.json.simple.JSONObject;

import modelo.Orden;

public class ServicioOrdenes {
	
	public Orden convertirJSON(Object obj) {
		Orden orden = new Orden(); 
		orden.setQuantity((Double) ((JSONObject) obj).get("Quantity"));
		orden.setRate((Double) ((JSONObject) obj).get("Rate"));
		return orden;
	}

}
