package co.edu.uniquindio.chat.application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket clientSocket;  // Socket del cliente
    private boolean appInit = false;  // Bandera para la inicialización de la aplicación

    private ObjectOutputStream outputStream;  // Flujo de salida de objetos
    private ObjectInputStream inputStream;  // Flujo de entrada de objetos

    // Constructor que recibe el socket del cliente
    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Inicialización de flujos de entrada y salida
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Bucle infinito para manejar la entrada del cliente continuamente
            while (true) {
                handleInput();
            }

        } catch (IOException | ClassNotFoundException e) {
            // Manejo de excepciones (comentado para evitar impresión de pila)
            // e.printStackTrace();
        }
    }

    // Método para manejar la entrada del cliente
    private void handleInput() throws ClassNotFoundException, IOException {
        // Lectura del objeto enviado por el cliente
        Object data = this.inputStream.readObject();

        // Verificación de si se recibió algún dato
        if (data != null) {
            // Envío de un mensaje de respuesta al cliente
            outputStream.writeObject("El equipo de soporte se encuentra inactivo.");
            outputStream.flush();
        }
    }
}
