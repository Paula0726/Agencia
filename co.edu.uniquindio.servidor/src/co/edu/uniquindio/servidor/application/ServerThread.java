package co.edu.uniquindio.servidor.application;

import co.edu.uniquindio.servidor.persistencia.Persistencia;
import co.edu.uniquindio.servidor.persistencia.Serializar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private boolean appInit = false;
    
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    
    private Persistencia persistencia;
    
    // Constructor que recibe un socket como parámetro
    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Inicializa los flujos de entrada y salida del socket
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
            
            // Si la aplicación no se ha inicializado, llama al método init() para enviar datos iniciales al cliente
            if (!this.appInit) this.appInit = init();
            
            // Bucle infinito para manejar la entrada continuamente
            while (true) {
                handleInput();
            }
        } catch (IOException | ClassNotFoundException e){
            // Maneja excepciones de entrada/salida y de clase no encontrada
            // e.printStackTrace();  // Comentado para evitar la impresión de la pila de excepciones
        }
    }
    
    // Inicializa la aplicación enviando datos iniciales al cliente
    private boolean init() {
        try {
            // Convierte los datos obtenidos de la persistencia a formato JSON y los envía al cliente
            String jsonString = Serializar.convertirMapAString(Persistencia.obtenerDatos());
            outputStream.writeObject(jsonString);
        } catch (IOException e) {
            // Maneja excepciones de entrada/salida
            e.printStackTrace();
        }
        return true;
    }
    
    // Maneja la entrada recibida desde el cliente
    private void handleInput() throws ClassNotFoundException, IOException {                    
        // Lee un objeto desde el flujo de entrada
        Object data = this.inputStream.readObject();
        String string = (String) data;
        
        // Convierte la cadena JSON en un objeto JsonNode utilizando la biblioteca Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(string);
        
        // Extrae la acción, entidad e información de datos del JsonNode
        String[] instruccion = jsonNode.get("accion").asText().split("-");                
        String base_instruccion = instruccion[0];
        String entidad_instruccion = instruccion[1];
        String datos = jsonNode.get("datos").asText();
        
        // Realiza acciones según la instrucción recibida
        switch (base_instruccion) {
            case "crear":
                // Crea una nueva línea en la entidad especificada con los datos proporcionados
                Persistencia.escribirLinea(entidad_instruccion, datos);
                break;
            case "actualizar":
                // Actualiza una línea en la entidad especificada con los datos proporcionados
                Persistencia.actualizarLinea(entidad_instruccion, datos);
                break;
            case "eliminar":
                // Elimina una línea en la entidad especificada con los datos proporcionados
                Persistencia.eliminarLinea(entidad_instruccion, datos);
                break;
            default:
                // Imprime un mensaje de error si la acción no es reconocida
                System.out.println("Acción no reconocida: " + instruccion);
        }
    }
}
