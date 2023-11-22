package co.edu.uniquindio.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;

public class util {
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
	
	public static String convertirMapAString(Map<String, String> mensaje) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(mensaje);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void enviarServidor(String instruccion, String datos) {		
		try {
			ModelFactoryController modelFactoryController;
			modelFactoryController = ModelFactoryController.getInstance();

			Map<String, String> mensaje = Map.of("accion", instruccion, "datos", datos);
    		String jsonString = convertirMapAString(mensaje);            
			modelFactoryController.getOutputStream().writeObject(jsonString);
			modelFactoryController.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
