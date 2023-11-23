package co.edu.uniquindio.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.EstadoReserva;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.implementation.Agencia;

public class Persistencia {
	/**
	 * Método estático para cargar datos desde un objeto JSON a una instancia de la clase Agencia.
	 * @param agencia Instancia de la clase Agencia.
	 * @param data Objeto que contiene datos en formato JSON.
	 */
	public static void cargarData(Agencia agencia, Object data) {
	    // Convierte el objeto data a una cadena de texto (String) en formato JSON.
	    String jsonString = (String) data;

	    // Inicializa un objeto ObjectMapper para mapear objetos JSON a instancias de clases Java.
	    ObjectMapper objectMapper = new ObjectMapper();

	    try {
	        // Lee el árbol JSON a partir de la cadena de texto JSON.
	        JsonNode jsonNode = objectMapper.readTree(jsonString);

	        // Carga los diferentes tipos de datos en la instancia de Agencia.
	        Persistencia.cargarUsuarios(agencia, jsonNode.get("usuarios"));
	        Persistencia.cargarClientes(agencia, jsonNode.get("clientes"));
	        Persistencia.cargarDestinos(agencia, jsonNode.get("destinos"));
	        Persistencia.cargarGuias(agencia, jsonNode.get("guias"));
	        Persistencia.cargarPaquetes(agencia, jsonNode.get("paquetes"));
	        Persistencia.cargarReservas(agencia, jsonNode.get("reservas"));

	    } catch (Exception e) {
	        // Imprime la traza de la excepción en caso de error.
	        e.printStackTrace();
	    }
	}

	/**
	 * Método privado para cargar usuarios desde un nodo JSON a una instancia de la clase Agencia.
	 * @param agencia Instancia de la clase Agencia.
	 * @param usuarios Nodo JSON que contiene la información de usuarios.
	 */
	private static void cargarUsuarios(Agencia agencia, JsonNode usuarios) {
	    // Llama al método recursivo para cargar usuarios.
	    cargarUsuariosRecursivo(agencia, usuarios, 0);
	}

	/**
	 * Método recursivo para cargar usuarios desde un nodo JSON a una instancia de la clase Agencia.
	 * @param agencia Instancia de la clase Agencia.
	 * @param usuarios Nodo JSON que contiene la información de usuarios.
	 * @param index Índice para recorrer los elementos del nodo JSON.
	 */
	private static void cargarUsuariosRecursivo(Agencia agencia, JsonNode usuarios, int index) {
	    // Verifica si hay más elementos en el nodo JSON.
	    if (index < usuarios.size()) {
	        // Obtiene el nodo del usuario actual.
	        JsonNode usuarioNode = usuarios.get(index);
	        // Obtiene la información del usuario como cadena de texto.
	        String usuarioInfo = usuarioNode.asText();
	        // Divide la información en partes utilizando el separador "@@".
	        String[] contenidoSplit = usuarioInfo.split("@@");

	        // Crea una instancia de la clase Usuario.
	        Usuario usuario = new Usuario();
	        // Llama al método para inicializar el objeto Usuario.
	        instanciarUsuario(usuario, contenidoSplit);
	        // Agrega el usuario a la lista de usuarios de la agencia.
	        agencia.getListaUsuarios().add(usuario);

	        // Llama recursivamente al método para el siguiente usuario.
	        cargarUsuariosRecursivo(agencia, usuarios, index + 1);
	    }
	}
    
    private static void cargarClientes(Agencia agencia, JsonNode clientes) {
        cargarClientesRecursivo(agencia, clientes, 0);
    }
    
    private static void cargarClientesRecursivo(Agencia agencia, JsonNode clientes, int index) {
        if (index < clientes.size()) {
            JsonNode clienteNode = clientes.get(index);
            String clienteInfo = clienteNode.asText();
            String[] contenidoSplit = clienteInfo.split("@@"); 

            Cliente cliente = new Cliente();
            instanciarCliente(cliente, contenidoSplit, agencia);
            agencia.getListaClientes().add(cliente);

            cargarClientesRecursivo(agencia, clientes, index + 1);
        }
    }

    private static void cargarDestinos(Agencia agencia, JsonNode destinos) {
        cargarDestinosRecursivo(agencia, destinos, 0);
    }    
    
    private static void cargarDestinosRecursivo(Agencia agencia, JsonNode destinos, int index) {
        if (index < destinos.size()) {
            JsonNode destinoNode = destinos.get(index);
            String destinoInfo = destinoNode.asText();
            String[] contenidoSplit = destinoInfo.split("@@");

            Destino destino = new Destino();
            instanciarDestino(destino, contenidoSplit);
            agencia.getListaDestinos().add(destino);

            cargarDestinosRecursivo(agencia, destinos, index + 1);
        }
    }

    private static void cargarGuias(Agencia agencia, JsonNode guias) {
        cargarGuiasRecursivo(agencia, guias, 0);
    }

    private static void cargarGuiasRecursivo(Agencia agencia, JsonNode guias, int index) {
        if (index < guias.size()) {
            JsonNode guiaNode = guias.get(index);
            String guiaInfo = guiaNode.asText();
            String[] contenidoSplit = guiaInfo.split("@@");

            GuiaTuristico guia = new GuiaTuristico();
            instanciarGuia(guia, contenidoSplit);
            agencia.getListaGuiasTuristicos().add(guia);

            cargarGuiasRecursivo(agencia, guias, index + 1);
        }
    }
    
    private static void cargarPaquetes(Agencia agencia, JsonNode paquetes) {
        cargarPaquetesRecursivo(agencia, paquetes, 0);
    }

    private static void cargarPaquetesRecursivo(Agencia agencia, JsonNode paquetes, int index) {
        if (index < paquetes.size()) {
            JsonNode paqueteNode = paquetes.get(index);
            String paqueteInfo = paqueteNode.asText();
            String[] contenidoSplit = paqueteInfo.split("@@");

            PaqueteTuristico paquete = new PaqueteTuristico();
            instanciarPaquete(paquete, contenidoSplit, agencia);
            agencia.getListaPaquetesTuristicos().add(paquete);

            cargarPaquetesRecursivo(agencia, paquetes, index + 1);
        }
    }

    private static void cargarReservas(Agencia agencia, JsonNode reservas) {
        cargarReservasRecursivo(agencia, reservas, 0);
    }

    private static void cargarReservasRecursivo(Agencia agencia, JsonNode reservas, int index) {
        if (index < reservas.size()) {
            JsonNode reservaNode = reservas.get(index);
            String reservaInfo = reservaNode.asText();
            String[] contenidoSplit = reservaInfo.split("@@");

            Reserva reserva = new Reserva();
            instanciarReserva(reserva, contenidoSplit, agencia);
            agencia.getListaReservas().add(reserva);

            cargarReservasRecursivo(agencia, reservas, index + 1);
        }
    }
    
    public static Usuario instanciarUsuario(Usuario usuario, String[] contenidoSplit) {
    	usuario.setId(contenidoSplit[0]);
    	usuario.setUsername(contenidoSplit[1]);
    	usuario.setContrasena(contenidoSplit[2]);
        return usuario;
    }
    
    public static Cliente instanciarCliente(Cliente cliente, String[] contenidoSplit, Agencia agencia) {
    	cliente.setId(contenidoSplit[0]);
        cliente.setIdentificacion(contenidoSplit[1]);
        cliente.setNombreCompleto(contenidoSplit[2]);
        cliente.setCorreoElectronico(contenidoSplit[3]);
        cliente.setNumeroTelefono(contenidoSplit[4]);
        cliente.setDireccionResidencia(contenidoSplit[5]);
        Usuario usuarioEncontrado = agencia.getListaUsuarios().stream()
                .filter(usuario -> usuario.getId().equals(contenidoSplit[6]))
                .findFirst()
                .orElse(null);
        cliente.setUsuario(usuarioEncontrado);
        List<String> listaDestinosBuscados = Arrays.asList(contenidoSplit[7].split(","));
        cliente.setDestinosBuscados(listaDestinosBuscados);
        return cliente;
    }
    
    public static Destino instanciarDestino(Destino destino, String[] contenidoSplit) {
    	destino.setId(contenidoSplit[0]);
        destino.setNombre(contenidoSplit[1]);
        destino.setCiudad(contenidoSplit[2]);
        destino.setDescripcion(contenidoSplit[3]);
		List<String> listaImagenes = Arrays.asList(contenidoSplit[4].split(","));
		destino.setImagenesRepresentativas(listaImagenes);
        destino.setClima(contenidoSplit[5]);
        destino.setCantidadBusquedas(Integer.parseInt(contenidoSplit[6]));
        List<Integer> listaEstrellas = Arrays.asList(contenidoSplit[7].split(",")).stream()
	        .map(Integer::parseInt)
	        .collect(Collectors.toList());
        destino.setEstrellas(listaEstrellas);
        List<String> listaComentarios = Arrays.asList(contenidoSplit[8].split(","));
        destino.setComentarios(listaComentarios);
        return destino;
    }
    
    public static GuiaTuristico instanciarGuia(GuiaTuristico guia, String[] contenidoSplit) {
    	guia.setId(contenidoSplit[0]);
        guia.setIdentificacion(contenidoSplit[1]);
        guia.setNombreCompleto(contenidoSplit[2]);
		List<String> listaLenguajes = Arrays.asList(contenidoSplit[3].split(","));
        guia.setLenguajesComunicacion(listaLenguajes);
        guia.setExperienciaAcumulada(contenidoSplit[4]);  
        guia.setPrecio(Integer.parseInt(contenidoSplit[5])); 
        if (contenidoSplit.length > 6) {
        	List<Integer> listaEstrellas = Arrays.asList(contenidoSplit[6].split(",")).stream()
        	        .map(Integer::parseInt)
        	        .collect(Collectors.toList());
            guia.setEstrellas(listaEstrellas);        
    		List<String> listaComentarios = Arrays.asList(contenidoSplit[7].split(","));
            guia.setComentarios(listaComentarios);        	
        }
        return guia;
    }
    
    public static PaqueteTuristico instanciarPaquete(PaqueteTuristico paquete, String[] contenidoSplit, Agencia agencia) {
    	 paquete.setId(contenidoSplit[0]);
         paquete.setNombre(contenidoSplit[1]);
         paquete.setDuracion(contenidoSplit[2]);            
         List<Destino> listaDestinos = agencia.getListaDestinos();
         List<String> destinosIds = Arrays.asList(contenidoSplit[3].split(","));
         List<Destino> destinosSeleccionados = destinosIds.stream()
                 .map(id -> listaDestinos.stream().filter(destino -> destino.getId().equals(id))
                         .findFirst()
                         .orElse(null))
                 .filter(destino -> destino != null)
                 .collect(Collectors.toList());    		    	    		
 		paquete.setDestinos(destinosSeleccionados);
 		List<String> listaServiciosAdicionales = Arrays.asList(contenidoSplit[4].split(","));    	
 		paquete.setServiciosAdicionales(listaServiciosAdicionales);
 		paquete.setPrecio(Integer.parseInt(contenidoSplit[5]));
        paquete.setCupoMaximo(Integer.parseInt(contenidoSplit[6]));
 		List<String> listaFechas = Arrays.asList(contenidoSplit[7].split(","));
 		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<LocalDate> fechasSeleccionadas = listaFechas.stream()
                 .map(fechaStr -> LocalDate.parse(fechaStr.trim(), dateFormatter))
                 .collect(Collectors.toList());
        paquete.setFechasDisponibles(fechasSeleccionadas);      
        return paquete;
    }
    
    public static Reserva instanciarReserva(Reserva reserva, String[] contenidoSplit, Agencia agencia) {    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	
    	reserva.setId(contenidoSplit[0]);
        LocalDate fechaSolicitud = LocalDate.parse(contenidoSplit[1], formatter);
        reserva.setFechaSolicitud(fechaSolicitud);
        LocalDate fechaPlanificada = LocalDate.parse(contenidoSplit[2], formatter);            
        reserva.setFechaPlanificadaViaje(fechaPlanificada);     
        String idCliente = contenidoSplit[3];
        Cliente clienteEncontrado = agencia.getListaClientes().stream()
                .filter(cliente -> cliente.getId().equals(idCliente))
                .findFirst()
                .orElse(null);
        reserva.setCliente(clienteEncontrado);              
        reserva.setCantidadPersonas(Integer.parseInt(contenidoSplit[4]));
        String idPaquete = contenidoSplit[5];
        PaqueteTuristico paqueteEncontrado = agencia.getListaPaquetesTuristicos().stream()
                .filter(paquete -> paquete.getId().equals(idPaquete))
                .findFirst()
                .orElse(null);
        reserva.setPaqueteTuristico(paqueteEncontrado);
        String idGuia = contenidoSplit[6];
        GuiaTuristico guiaEncontrado = agencia.getListaGuiasTuristicos().stream()
                .filter(guia -> guia.getId().equals(idGuia))
                .findFirst()
                .orElse(null);
        reserva.setGuiaTuristico(guiaEncontrado);
        EstadoReserva estadoReserva = EstadoReserva.valueOf(contenidoSplit[7]);
        reserva.setEstadoReserva(estadoReserva);    
        return reserva;
    }
}