package co.edu.uniquindio.agencia.exceptions;

public class UsuarioNoActualizado extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioNoActualizado(String mensaje){
		super(mensaje);
	}	
}