package bots;

import principal.ControladorFormularioPrincipal;

public abstract class BotMinero extends Thread {
	
	protected String mercado;
	protected double cantidadCorte;
	protected long intervalo;
	protected ControladorFormularioPrincipal controlador;
	protected boolean parar;

	public BotMinero(String mercado, double cantidadCorte, long intervalo, ControladorFormularioPrincipal controlador) {
		this.mercado = mercado;
		this.cantidadCorte = cantidadCorte;
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
