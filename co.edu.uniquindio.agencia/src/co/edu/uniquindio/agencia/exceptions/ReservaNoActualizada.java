package co.edu.uniquindio.agencia.exceptions;

public class ReservaNoActualizada extends Exception {
	private static final long serialVersionUID = 1L;

	public ReservaNoActualizada(String mensaje){
		super(mensaje);
	}	
}