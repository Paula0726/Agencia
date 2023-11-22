package co.edu.uniquindio.agencia.exceptions;

public class ClienteNoEliminado extends Exception {
	private static final long serialVersionUID = 1L;

	public ClienteNoEliminado(String mensaje){
		super(mensaje);
	}	
}
