package bots;

import principal.Constantes;
import principal.ControladorFormularioPrincipal;

public class BotSencillo extends BotMercado {

	private double esperarNuevoTurno;
	
	public BotSencillo(String mercado, double precioMinimo, double precioMaximo, long intervalo, ControladorFormularioPrincipal controlador) {
		super(mercado, precioMinimo, precioMaximo, intervalo, controlador);
		esperarNuevoTurno = 0;
	}

	@Override
	protected void ejecutarAlgoritmo() {
		double precioBidActual = 0;
		double precioAskActual = 0;
		double btcDisponible = 0;
		double monedaDisponible = 0;
		double factor = 0.1;
		while(!parar) {
			precioBidActual = controlador.getPrecioActual("Bid");
			precioAskActual = controlador.getPrecioActual("Ask");
			btcDisponible = controlador.getDisponible(Constantes.BITCOIN_ABREVIATURA);
			String moneda = mercado.substring(mercado.indexOf("-") + 1);
			monedaDisponible = controlador.getDisponible(moneda);
			System.out.println("El precio de compra actual es " + precioBidActual);
			System.out.println("El precio de venta actual es " + precioAskActual);
			System.out.println("El dinero disponible actual es " + btcDisponible + " " + Constantes.BITCOIN_ABREVIATURA);
			if(precioBidActual < precioMinimo && btcDisponible >= 0.0005d) {
				double cantidadCompraMaxima = controlador.calcularMaximoCompra();
				controlador.comprar(mercado, cantidadCompraMaxima * factor, precioBidActual);
				esperarNuevoTurno = 600000;
				precioMinimo = precioBidActual;
			} else if(precioAskActual > precioMaximo  && monedaDisponible >= 0.0d) {
				controlador.vender(mercado, monedaDisponible * factor, precioAskActual);
				esperarNuevoTurno = 600000;
				precioMaximo = precioAskActual;
			}
			try {
				BotMercado.sleep(intervalo);
				if(esperarNuevoTurno > 0) esperarNuevoTurno -= intervalo;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}

}
