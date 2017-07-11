package servicios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;

import javafx.util.Pair;

public class ServicioPropiedades {

	public String getPropiedad(String nombre) {

		InputStream inputStream = null;
		String result = "";
		try {
			Properties prop = new Properties();
			File file;
			file = new File(getClass().getResource("/application.properties").getFile());
			inputStream = new FileInputStream(file);

			if (inputStream != null)
				prop.load(inputStream);

			// get the property value and print it out
			result = prop.getProperty(nombre);
			System.out.println(result);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void setPropiedadesAPI(String codigoApi, String keySecret) {
		try {
			FileReader lector;
			FileWriter escritor;
			lector = new FileReader(getClass().getResource("/application.properties").getFile());
			BufferedReader lectorBuffer = new BufferedReader(lector);
			LinkedList<Pair<String, String>> lista = new LinkedList<Pair<String, String>>();
			int indice = 0;
			LinkedList<Integer> indices = new LinkedList<Integer>(); 
			while (lectorBuffer.ready()) {
				String cadena = lectorBuffer.readLine();
				String[] cadenas;
				if (cadena.contains("#")) {
					lista.add(new Pair<String, String>("#", cadena.substring(1)));
				} else if (cadena.contains("=")) {
					cadenas = cadena.split("=");
					lista.add(new Pair<String, String>(cadenas[0]+"=", cadenas[1]));
					indices.add(indice);
				} else {
					lista.add(new Pair<String, String>("", "\n"));
				}
				indice++;
			}
			lectorBuffer.close();
			lector.close();
			

			lista.remove(lista.get(indices.get(0)));
			lista.add(indices.get(0), new Pair<String, String>("bittrex-codigo-api=", codigoApi));
			
			lista.remove(lista.get(indices.get(1)));
			lista.add(indices.get(1), new Pair<String, String>("bittrex-key-secret=", keySecret));
			
			
			escritor = new FileWriter(getClass().getResource("/application.properties").getFile());
			BufferedWriter escritorBuffer = new BufferedWriter(escritor);
			
			for (Pair<String, String> pair : lista) {
				escritorBuffer.write(pair.getKey()+pair.getValue());
				if(!pair.getValue().contains("\n")) escritorBuffer.newLine();
			}
			escritorBuffer.close();
			escritor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public void setPropiedadesAPI(String codigoApi, String keySecret) {
	// FileOutputStream fos = null;
	// InputStream inputStream = null;
	// try {
	// File file;
	// file = new
	// File(getClass().getResource("/application.properties").getFile());
	// fos = new FileOutputStream(file);
	// Properties prop = new Properties();
	// prop.load(inputStream);
	// prop.setProperty("bittrex-codigo-api", codigoApi.replaceAll(" ", ""));
	// prop.setProperty("bittrex-key-secret", keySecret.replaceAll(" ", ""));
	// prop.store(fos, "Application properties");
	//// System.out.println(prop.getProperty("bittrex-codigo-api"));
	//// System.out.println(prop.getProperty("bittrex-key-secret"));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// try {
	// inputStream.close();
	// fos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

}
