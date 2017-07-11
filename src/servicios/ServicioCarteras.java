package servicios;

import org.json.simple.JSONObject;

import modelo.Cartera;

public class ServicioCarteras {
	
	public Cartera convertirJSON(Object obj) {
		Cartera cartera = new Cartera(); 
		cartera.setCurrency((String) ((JSONObject) obj).get("Currency"));
		cartera.setBalance((Double) ((JSONObject) obj).get("Balance"));
		cartera.setAvailable((Double) ((JSONObject) obj).get("Available"));
		cartera.setPending((Double) ((JSONObject) obj).get("Pending"));
		cartera.setCryptoAddress((String) ((JSONObject) obj).get("CryptoAddress"));
		return cartera;
	}

}
