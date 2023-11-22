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
    
    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
        	this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        	if (!this.appInit) this.appInit = init();
        	
        	while (true) {
        		handleInput();
        	}
                       
        } catch (IOException | ClassNotFoundException e){
        	//e.printStackTrace();
        };
    }
    
    private boolean init() {
    	try {
    		String jsonString = Serializar.convertirMapAString(Persistencia.obtenerDatos());
			outputStream.writeObject(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    private void handleInput() throws ClassNotFoundException, IOException {    	            
		Object data = this.inputStream.readObject();
    	String string = (String) data;
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(string);
		String[] instruccion = jsonNode.get("accion").asText().split("-");                
        String base_instruccion = instruccion[0];
        String entidad_instruccion = instruccion[1];
        String datos = jsonNode.get("datos").asText();
        
        switch (base_instruccion) {
	        case "crear":
				Persistencia.escribirLinea(entidad_instruccion, datos);
	            break;
	        case "actualizar":
    			Persistencia.actualizarLinea(entidad_instruccion, datos);
	        	break;
	        case "eliminar":
    			Persistencia.eliminarLinea(entidad_instruccion, datos);
	        	break;
	        default:
	            System.out.println("Acción no reconocida: " + instruccion);
	        }
    }
}