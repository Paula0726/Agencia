package co.edu.uniquindio.agencia.exceptions;

public class ReservaNoEliminada extends Exception {
	private static final long serialVersionUID = 1L;

	public ReservaNoEliminada(String mensaje){
		super(mensaje);
	}	
}