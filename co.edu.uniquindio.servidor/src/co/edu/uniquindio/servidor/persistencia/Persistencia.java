package co.edu.uniquindio.servidor.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Persistencia {	
	public static final String path = "persistencia/archivos/";
	public static final String RUTA_ARCHIVO_USUARIOS = "usuarios.txt";
	public static final String RUTA_ARCHIVO_CLIENTES = "clientes.txt";
	public static final String RUTA_ARCHIVO_DESTINOS = "destinos.txt";
	public static final String RUTA_ARCHIVO_GUIAS = "guias.txt";
	public static final String RUTA_ARCHIVO_PAQUETES = "paquetes.txt";
	public static final String RUTA_ARCHIVO_RESERVAS = "reservas.txt";
	
	private static final Logger LOGGER = Logger.getLogger(Persistencia.class.getName());	
	
	public static void crearLogger() {
        FileHandler archivo;
        try {
            archivo = new FileHandler("persistencia/log/agencia_log.txt", true);
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(archivo);
            archivo.setFormatter(new SimpleFormatter());
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void logging(String INFO) {
        LOGGER.log(Level.INFO, "INFO: " + INFO);
    }	
	
	private static Map<String, String> mapa = Map.of(
			"usuarios", RUTA_ARCHIVO_USUARIOS,
            "clientes", RUTA_ARCHIVO_CLIENTES,
            "destinos", RUTA_ARCHIVO_DESTINOS,
            "guias", RUTA_ARCHIVO_GUIAS,
            "paquetes", RUTA_ARCHIVO_PAQUETES,
            "reservas", RUTA_ARCHIVO_RESERVAS
    );
	
	public static Map<String, ArrayList<String>> obtenerDatos() throws IOException {
		Map<String, ArrayList<String>> datos = new HashMap<>();
        datos.put("usuarios", leerArchivo(path + RUTA_ARCHIVO_USUARIOS));
        datos.put("clientes", leerArchivo(path + RUTA_ARCHIVO_CLIENTES));
        datos.put("destinos", leerArchivo(path + RUTA_ARCHIVO_DESTINOS));
        datos.put("guias", leerArchivo(path + RUTA_ARCHIVO_GUIAS));
        datos.put("paquetes", leerArchivo(path + RUTA_ARCHIVO_PAQUETES));
        datos.put("reservas", leerArchivo(path + RUTA_ARCHIVO_RESERVAS));
        
        crearLogger();
        logging("DATOS INICIALIZADOS");

        return datos;
	}
	
	public static ArrayList<String> leerArchivo(String ruta) throws IOException {
		ArrayList<String> contenido = new ArrayList<String>();
		FileReader fileReader = new FileReader(ruta);
		BufferedReader buffer = new BufferedReader(fileReader);
		
		String linea = "";
		while ((linea = buffer.readLine()) != null) {
			contenido.add(linea);
		}
		
		buffer.close();
		fileReader.close();
		
		return contenido;		
	}
	
	public static void escribirLinea(String entidad, String linea) {		
		Path archivoPath = Path.of(path+mapa.get(entidad + "s"));
		
		try (Stream<String> lineas = Files.lines(archivoPath)) {
            List<String> lineasActualizadas = Stream.concat(lineas, Stream.of(linea))
                    .collect(Collectors.toList());

            Files.write(archivoPath, lineasActualizadas);
            logging("NUEVO DATO AGREGADO: "+entidad);
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void actualizarLinea(String entidad, String nuevaInformacion) throws IOException {
        Path archivoPath = Path.of(path+mapa.get(entidad + "s"));

        String id = nuevaInformacion.split("@@")[0];        
        List<String> lineas = Files.lines(archivoPath)
                .map(linea -> linea.startsWith(id + "@@") ? nuevaInformacion : linea)
                .collect(Collectors.toList());
        
        Files.write(archivoPath, lineas, StandardOpenOption.TRUNCATE_EXISTING);
        logging("DATO ACTUALIZADO: "+entidad);
	}
	
	public static void eliminarLinea(String entidad, String id) throws IOException {
        System.out.println("Entidad: "+entidad);		
		Path archivoPath = Path.of(path+mapa.get(entidad + "s")); 
        
        List<String> lineas = Files.lines(archivoPath)
                .filter(linea -> !linea.startsWith(id + "@@"))
                .collect(Collectors.toList());

        Files.write(archivoPath, lineas);
        logging("DATO ELIMINADO: "+entidad);
    }
}
