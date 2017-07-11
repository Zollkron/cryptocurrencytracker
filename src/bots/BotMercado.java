package bots;

import principal.ControladorFormularioPrincipal;

public abstract class BotMercado extends Thread {
	
	protected String mercado;
	protected double precioMinimo;
	protected double precioMaximo;
	protected long intervalo;
	protected ControladorFormularioPrincipal controlador;
	protected boolean parar;

	public BotMercado(String mercado, double precioMinimo, double precioMaximo, long intervalo, ControladorFormularioPrincipal controlador) {
		this.mercado = mercado;
		this.precioMinimo = precioMinimo;
		this.precioMaximo = precioMaximo;
		this.intervalo = intervalo;
		this.controlador = controlador;
		parar = false;
	}

	@Override
	public void run() {
		ejecutarAlgoritmo();
	}
	
	protected abstract void ejecutarAlgoritmo();

}
