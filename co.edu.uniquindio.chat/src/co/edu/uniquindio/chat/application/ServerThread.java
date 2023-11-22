package co.edu.uniquindio.chat.application;

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
        
    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
        	this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        	
        	while (true) {
        		handleInput();
        	}
                       
        } catch (IOException | ClassNotFoundException e){
        	//e.printStackTrace();
        };
    }
    
    
    private void handleInput() throws ClassNotFoundException, IOException {    	            
		Object data = this.inputStream.readObject();
    	if (data != null) {
    		//String string = (String) data;    		
    		outputStream.writeObject("El equipo de soporte se encuentra inactivo.");
            outputStream.flush();
        		
    	}		
		
    	/*
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(string);
		String[] instruccion = jsonNode.get("accion").asText().split("-");                
        String base_instruccion = instruccion[0];
        String entidad_instruccion = instruccion[1];
        String datos = jsonNode.get("datos").asText();     
        */          
    }
}