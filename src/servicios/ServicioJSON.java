package servicios;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServicioJSON {

	public JSONObject leerJSON(String cadenaJSON) {

		JSONObject objetoJSONSalida = null;
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(cadenaJSON);

			objetoJSONSalida = (JSONObject) obj;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return objetoJSONSalida;
	}

	public JSONObject getResultadoBittrex(String cadenaJSON) {

		JSONObject objetoJSONSalida = null;
		JSONObject objetoJSONEntrada = leerJSON(cadenaJSON);
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(objetoJSONEntrada.get("result").toString());

			objetoJSONSalida = (JSONObject) obj;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return objetoJSONSalida;
	}
	
	public JSONArray getResultadoArrayBittrex(String cadenaJSON) {

		JSONArray arrayJSONSalida = null;
		JSONObject objetoJSONEntrada = leerJSON(cadenaJSON);
		try {

			arrayJSONSalida = (JSONArray) objetoJSONEntrada.get("result");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayJSONSalida;
	}

	public double getBalanceTotal(JSONObject objetoJSON) {
		double total = 0;
		ServicioRESTBittrex servicioRESTBittrex = new ServicioRESTBittrex(null, null);
		try {
			JSONArray lista = (JSONArray) objetoJSON.get("result");
			double ultimaCotizacionBTC = 0;
			for (Object obj : lista) {
				String moneda = (String) ((JSONObject) obj).get("Currency");
				double balance = (Double) ((JSONObject) obj).get("Balance");
				if (balance != 0) {
					if (!moneda.equals("BTC")) {
						String market = "BTC-" + moneda;
						//System.out.println(market);
						String mercadoMoneda = servicioRESTBittrex.getTicker(market);
						JSONObject objetoJSONMercadoMoneda = getResultadoBittrex(mercadoMoneda);
						ultimaCotizacionBTC = (Double) objetoJSONMercadoMoneda.get("Last");
					} else {
						ultimaCotizacionBTC = 1;
					}
				}
				total += balance * ultimaCotizacionBTC;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}

}
