package co.edu.uniquindio.agencia.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.agencia.services.IModelFactoryService;
import co.edu.uniquindio.implementation.Agencia;
import co.edu.uniquindio.util.Persistencia;
import lombok.Data;

@Data
public class ModelFactoryController extends Thread implements IModelFactoryService {
	
	private Agencia agencia;
		
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private Timer timer;

	private static class SingletonHolder {
		private final static ModelFactoryController eINSTANCE = new ModelFactoryController();	
	}
	
	public static ModelFactoryController getInstance(){
		return SingletonHolder.eINSTANCE;
	}
	
	public ModelFactoryController() {	}
	
	public void inicializarDatos(Object data, ObjectInputStream in, ObjectOutputStream out) throws IOException{		
		agencia = (new Agencia()).init();
		inputStream = in;
		outputStream = out;
				
		Persistencia.cargarData(agencia, data);
	
		//Cliente cliente = agencia.getListaClientes().get(0);
		//agencia.setClienteSession(cliente);		
	}
}

