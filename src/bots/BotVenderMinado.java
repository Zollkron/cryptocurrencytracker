package bots;

import principal.ControladorFormularioPrincipal;

public class BotVenderMinado extends BotMinero {

	private double esperarNuevoTurno;

	public BotVenderMinado(String mercado, double precioCorte, long intervalo,
			ControladorFormularioPrincipal controlador) {
		super(mercado, precioCorte, intervalo, controlador);
		esperarNuevoTurno = 0;
	}

	@Override
	protected void ejecutarAlgoritmo() {
		double precioBidActual = 0;
		double monedaDisponible = 0;
		while (!parar) {
			precioBidActual = controlador.getPrecioActual("Bid");
			String moneda = mercado.substring(mercado.indexOf("-") + 1);
			monedaDisponible = controlador.getDisponible(moneda);
			System.out.println("El precio de compra actual es " + precioBidActual);
			System.out.println("La cantidad de moneda es " + monedaDisponible);
			if (monedaDisponible > cantidadCorte) {
				double cantidadAVender = monedaDisponible - cantidadCorte;
				System.out.println("La cantidad que se va a vender es de " + cantidadAVender);
				controlador.vender(mercado, cantidadAVender, precioBidActual);
			}
			esperarNuevoTurno = 200000;
			try {
				while(esperarNuevoTurno > 0) {
					BotVenderMinado.sleep(intervalo);
					esperarNuevoTurno -= intervalo;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
