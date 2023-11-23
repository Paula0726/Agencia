package co.edu.uniquindio.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;

public class util {
    
    // Método para convertir una cadena de texto en formato JSON a un objeto Map<String, ArrayList<String>>
    @SuppressWarnings("unchecked")
    public static Map<String, ArrayList<String>> convertirStringAMap(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Método para convertir un objeto Map<String, String> a una cadena de texto en formato JSON
    public static String convertirMapAString(Map<String, String> mensaje) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(mensaje);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Método para enviar datos al servidor a través de un controlador de modelo (ModelFactoryController)
    public static void enviarServidor(String instruccion, String datos) {        
        try {
            // Obtener una instancia del controlador de modelo
            ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();

            // Crear un mensaje utilizando la instrucción y los datos proporcionados
            Map<String, String> mensaje = Map.of("accion", instruccion, "datos", datos);
            String jsonString = convertirMapAString(mensaje);

            // Enviar el mensaje al servidor a través del flujo de salida del controlador de modelo
            modelFactoryController.getOutputStream().writeObject(jsonString);
            modelFactoryController.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
