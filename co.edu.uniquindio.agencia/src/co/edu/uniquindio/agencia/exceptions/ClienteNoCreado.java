package co.edu.uniquindio.agencia.exceptions;

public class ClienteNoCreado extends Exception {
	private static final long serialVersionUID = 1L;

	public ClienteNoCreado(String mensaje){
		super(mensaje);
	}	
}