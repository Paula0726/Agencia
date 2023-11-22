package co.edu.uniquindio.agencia.exceptions;

public class UsuarioNoCreado extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioNoCreado(String mensaje){
		super(mensaje);
	}	
}