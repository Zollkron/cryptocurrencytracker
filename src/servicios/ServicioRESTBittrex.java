package servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ServicioRESTBittrex {

	private String apiKey;
	private String keySecret;

	public ServicioRESTBittrex(String apiKey, String keySecret) {
		this.apiKey = apiKey;
		this.keySecret = keySecret;
	}

	public String getMarkets() {
		String salida = null;
		try {

			URL url = new URL("https://bittrex.com/api/v1.1/public/getmarkets");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getTicker(String market) {
		String salida = null;
		try {

			URL url = new URL("https://bittrex.com/api/v1.1/public/getticker?market=" + market);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getCurrencies() {
		String salida = null;
		try {

			URL url = new URL("https://bittrex.com/api/v1.1/public/getcurrencies");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getMarketSummaries() {
		String salida = null;
		try {

			URL url = new URL("https://bittrex.com/api/v1.1/public/getmarketsummaries");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getOrderBook(String market, String type, String depth) {
		String salida = null;
		try {

			URL url = new URL("https://bittrex.com/api/v1.1/public/getorderbook?market=" + market + "&type=" + type
					+ "&depth=" + depth);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getBuyOrders(String market, String depth) {
		return getOrderBook(market, "buy", depth);
	}

	public String getSellOrders(String market, String depth) {
		return getOrderBook(market, "sell", depth);
	}

	public String getMarketHistory(String market, String count) {
		String salida = null;
		try {

			URL url = new URL(
					"https://bittrex.com/api/v1.1/public/getmarkethistory?market=" + market + "&count=" + count);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public static String calcularHMAC(String uri, String keySecret, String algo) {
		String resultado = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keySecret).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);

			byte[] bytes = mac.doFinal(uri.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			resultado = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return resultado;
	}

	public String getBalances() {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = new URL("https://bittrex.com/api/v1.1/account/getbalances?apikey=" + apiKey + "&nonce=" + time);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getOpenOrders(String market) {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = new URL("https://bittrex.com/api/v1.1/market/getopenorders?apikey=" + apiKey + "&nonce=" + time
					+ "&market=" + market);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String buyLimit(String market, String quantity, String rate) {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = new URL("https://bittrex.com/api/v1.1/market/buylimit?apikey=" + apiKey + "&nonce=" + time
					+ "&market=" + market + "&quantity=" + quantity + "&rate=" + rate);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String sellLimit(String market, String quantity, String rate) {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = new URL("https://bittrex.com/api/v1.1/market/selllimit?apikey=" + apiKey + "&nonce=" + time
					+ "&market=" + market + "&quantity=" + quantity + "&rate=" + rate);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String cancel(String uuid) {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = new URL(
					"https://bittrex.com/api/v1.1/market/cancel?apikey=" + apiKey + "&nonce=" + time + "&uuid=" + uuid);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String getOrderHistory(String market, String count) {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = new URL("https://bittrex.com/api/v1.1/account/getorderhistory?apikey=" + apiKey + "&nonce=" + time
					+ "&market=" + market + "&count=" + count);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

	public String withdraw(String currency, String quantity, String address, String paymentId) {
		String salida = null;
		try {

			String time = String.valueOf(new Date().getTime());
			URL url = null;
			if (paymentId != null)
				url = new URL("https://bittrex.com/api/v1.1/account/withdraw?apikey=" + apiKey + "&nonce=" + time
						+ "&currency=" + currency + "&quantity=" + quantity + "&address=" + address + "&paymentid="
						+ paymentId);
			else
				url = new URL("https://bittrex.com/api/v1.1/account/withdraw?apikey=" + apiKey + "&nonce=" + time
						+ "&currency=" + currency + "&quantity=" + quantity + "&address=" + address);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String apiSign = calcularHMAC(url.toString(), keySecret, "HMACSHA512");

			conn.setRequestProperty("APISIGN", apiSign);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Fallo : Código de error HTTP : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Respuesta del Servidor .... \n");
			String output;
			salida = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				salida += output;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return salida;
	}

}
