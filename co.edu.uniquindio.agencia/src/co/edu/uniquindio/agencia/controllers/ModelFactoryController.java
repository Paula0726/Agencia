package co.edu.uniquindio.agencia.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

import co.edu.uniquindio.implementation.Agencia;
import co.edu.uniquindio.util.Persistencia;
import lombok.Data;

@Data
public class ModelFactoryController extends Thread {

	// Instancia de la clase principal Agencia
	private Agencia agencia;

	// Flujos de entrada y salida de objetos para la comunicación
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	// Socket del servidor y del cliente
	private ServerSocket serverSocket;
	private Socket clientSocket;

	// Temporizador para tareas programadas
	private Timer timer;

	// Clase interna para el patrón de diseño Singleton
	private static class SingletonHolder {
		private final static ModelFactoryController eINSTANCE = new ModelFactoryController();
	}

	// Método estático para obtener la instancia única del controlador
	public static ModelFactoryController getInstance() {
		return SingletonHolder.eINSTANCE;
	}

	// Constructor por defecto de la clase
	public ModelFactoryController() {
	}

	// Método para inicializar los datos de la agencia con flujos de entrada/salida
	public void inicializarDatos(Object data, ObjectInputStream in, ObjectOutputStream out) throws IOException {
		// Inicialización de la instancia de Agencia y asignación de flujos de entrada/salida
		agencia = (new Agencia()).init();
		inputStream = in;
		outputStream = out;

		// Carga de datos desde la persistencia en el objeto Agencia
		Persistencia.cargarData(agencia, data);
	}
}
