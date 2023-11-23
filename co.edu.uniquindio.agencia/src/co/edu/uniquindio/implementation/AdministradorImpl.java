package co.edu.uniquindio.implementation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import co.edu.uniquindio.agencia.exceptions.DestinoNoActualizado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoCreado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoEliminado;
import co.edu.uniquindio.agencia.exceptions.GuiaNoCreado;
import co.edu.uniquindio.agencia.exceptions.GuiaNoEliminado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoActualizado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoCreado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoEliminado;
import co.edu.uniquindio.agencia.exceptions.UsuarioNoActualizado;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.util.Persistencia;
import co.edu.uniquindio.util.util;
import lombok.AllArgsConstructor;

public class AdministradorImpl {
	public static final String PATH_ARCHIVO_INFORMES = "archivos/reportes/";
	
	private Agencia agencia;
	private static Map<String, String> authAdmins = new HashMap<>();
	
	public AdministradorImpl(Agencia agencia) {
		this.agencia = agencia;
        authAdmins.put("admin1", "super_admin1");
        authAdmins.put("admin2", "super_admin2");
        authAdmins.put("admin3", "super_admin3");
    }

	// Funcion para crear destino
	public void crearDestino(String data) throws DestinoNoCreado { 
	    Destino destinoExistente = null;
	    Destino destinoNuevo = null;
	    String[] contenidoSplit = data.split("@@");

	    destinoExistente = agencia.obtenerDestino(contenidoSplit[1]);
	    if (destinoExistente != null) {
	        throw new DestinoNoCreado("Ya existe un destino con este nombre.");
	    } else {
	    	String nuevoId = obtenerNuevoDestinoId(agencia.getListaDestinos(), agencia.getListaDestinos().size() + 1);
	    	contenidoSplit[0] = nuevoId;  	
	    	
	    	destinoNuevo = new Destino();
	        destinoNuevo = Persistencia.instanciarDestino(destinoNuevo, contenidoSplit);
	        agencia.getListaDestinos().add(destinoNuevo);

	        util.enviarServidor("crear-destino", String.join("@@", contenidoSplit));	    
	    }
	}
	
	private String obtenerNuevoDestinoId(List<Destino> listaDestinos, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaDestinos.stream().anyMatch(destino -> nuevoId.equals(destino.getId()))) {
            return obtenerNuevoDestinoId(listaDestinos, intento + 1);
        } else {
            return nuevoId;
        }
    }
	
	public void actualizarDestino(Destino destinoActual, String dataDestinoNuevo) throws DestinoNoActualizado {
	    String[] contenidoSplitNuevo = dataDestinoNuevo.split("@@");
	    if (destinoActual != null && destinoActual.convertirEnCadena().equals(dataDestinoNuevo)) {
			throw new DestinoNoActualizado("La información del destino a actualizar es igual a la original.");
		} else {
		    try {
		        util.enviarServidor("actualizar-destino", dataDestinoNuevo);
		    } finally {
			    Destino destino = agencia.obtenerDestino(contenidoSplitNuevo[1]);
		    	Persistencia.instanciarDestino(destino, contenidoSplitNuevo);	
		    }
		}
	}
	
	public void eliminarDestino(String nombre) throws DestinoNoEliminado {
	    Destino destino = agencia.obtenerDestino(nombre);

	    if (destino == null) {
	        throw new DestinoNoEliminado("No existe un destino con el id seleccionado.");
	    } else {
	        agencia.getListaDestinos().remove(destino);
	        util.enviarServidor("eliminar-destino", destino.getId());
	    }
	}
	
	public void crearPaqueteTuristico(String data) throws PaqueteNoCreado { 
	    PaqueteTuristico paqueteExistente = null;
	    PaqueteTuristico paqueteNuevo = null;

	    String[] contenidoSplit = data.split("@@");
	    
	    paqueteExistente = agencia.obtenerPaqueteTuristico(contenidoSplit[1]);
	    if (paqueteExistente != null) {
	        throw new PaqueteNoCreado("Ya existe un paquete turístico con este nombre.");
	    } else {
	    	String nuevoId = obtenerNuevoPaqueteId(agencia.getListaPaquetesTuristicos(), agencia.getListaPaquetesTuristicos().size() + 1) + "";
	    	contenidoSplit[0] = nuevoId;
	    	
	        paqueteNuevo = new PaqueteTuristico();
	        paqueteNuevo = Persistencia.instanciarPaquete(paqueteNuevo, contenidoSplit, agencia);
	        agencia.getListaPaquetesTuristicos().add(paqueteNuevo);
	        
	        util.enviarServidor("crear-paquete", String.join("@@", contenidoSplit));
	    }
	}
	
	private String obtenerNuevoPaqueteId(List<PaqueteTuristico> listaPaquetes, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaPaquetes.stream().anyMatch(paquete -> nuevoId.equals(paquete.getId()))) {
            return obtenerNuevoPaqueteId(listaPaquetes, intento + 1);
        } else {
            return nuevoId;
        }
    }
	
	public void actualizarPaqueteTuristico(PaqueteTuristico paqueteActual, String dataPaqueteNuevo) throws PaqueteNoActualizado {
	    String[] contenidoSplitNuevo = dataPaqueteNuevo.split("@@");	    	    
	    
	    if (paqueteActual != null && paqueteActual.convertirEnCadena().equals(dataPaqueteNuevo)) {
			throw new PaqueteNoActualizado("La información del paquete a actualizar es igual a la original.");
		} else { 
		 	try {
		        util.enviarServidor("actualizar-paquete", dataPaqueteNuevo);
		    } finally {
			    PaqueteTuristico paquete = agencia.obtenerPaqueteTuristicoId(contenidoSplitNuevo[0]);
		    	Persistencia.instanciarPaquete(paquete, contenidoSplitNuevo, agencia);		    	
		    }	
		}
	}
	
	public void eliminarPaqueteTuristico(String nombre) throws PaqueteNoEliminado {
	    PaqueteTuristico paquete = agencia.obtenerPaqueteTuristico(nombre);

	    if (paquete == null) {
	        throw new PaqueteNoEliminado("No existe un paquete con el nombre seleccionado.");
	    } else {
	        agencia.getListaPaquetesTuristicos().remove(paquete);
	        util.enviarServidor("eliminar-paquete", paquete.getId());
	    }
	}

	public void crearGuiaTuristico(String data) throws GuiaNoCreado {
	    GuiaTuristico guiaExistente = null;
	    GuiaTuristico guiaNuevo = null;

	    String[] contenidoSplit = data.split("@@");

	    guiaExistente = agencia.obtenerGuiaTuristico(contenidoSplit[1]);
	    if (guiaExistente != null) {
	        throw new GuiaNoCreado("Ya existe un guía turístico con este número de identificación.");
	    } else {
	    	String nuevoId = obtenerNuevoGuiaId(agencia.getListaGuiasTuristicos(), agencia.getListaGuiasTuristicos().size() + 1);
	    	contenidoSplit[0] = nuevoId;	    	
	    	
	    	guiaNuevo = new GuiaTuristico();
	        guiaNuevo = Persistencia.instanciarGuia(guiaNuevo, contenidoSplit);
	        agencia.getListaGuiasTuristicos().add(guiaNuevo);
	        
	        util.enviarServidor("crear-guia", String.join("@@", contenidoSplit));
	    }
	}
	
	private String obtenerNuevoGuiaId(List<GuiaTuristico> listaGuias, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaGuias.stream().anyMatch(guia -> nuevoId.equals(guia.getId()))) {
            return obtenerNuevoGuiaId(listaGuias, intento + 1);
        } else {
            return nuevoId;
        }
    }

	public void actualizarGuiaTuristico(GuiaTuristico guiaActual, String dataGuiaNuevo) throws PaqueteNoActualizado {
	    String[] contenidoSplitNuevo = dataGuiaNuevo.split("@@");

	    if (guiaActual != null && guiaActual.convertirEnCadena().equals(dataGuiaNuevo)) {
			throw new PaqueteNoActualizado("La información del guía a actualizar es igual a la original.");
		} else {
		    try {
		        util.enviarServidor("actualizar-guia", dataGuiaNuevo);
		    } finally {
			    GuiaTuristico guia = agencia.obtenerGuiaTuristico(contenidoSplitNuevo[1]);
		    	Persistencia.instanciarGuia(guia, contenidoSplitNuevo);
		    } 
	    }
	}

	public void eliminarGuiaTuristico(String identificacion) throws GuiaNoEliminado { 
	    GuiaTuristico guia = agencia.obtenerGuiaTuristico(identificacion);

	    if (guia == null) {
	        throw new GuiaNoEliminado("No existe un guía con el número de identificación seleccionado.");
	    } else {
	        agencia.getListaGuiasTuristicos().remove(guia);
	        util.enviarServidor("eliminar-guia", guia.getId());
	    }
	}
	
	public boolean verificarInicioSesion(String usuario, String contrasena) {
        return verificarInicioSesionRecursivo(usuario, contrasena, authAdmins);
    }

    public boolean verificarInicioSesionRecursivo(String usuario, String contrasena, Map<String, String> mapUsuariosContrasenas) {
        if (mapUsuariosContrasenas.containsKey(usuario)) {
            if (mapUsuariosContrasenas.get(usuario).equals(contrasena)) {
                return true; 
            }
        }

        return false; 
    }
	
	public List<Entry<String, Integer>> obtenerDestinosMasReservados(int limit) {
		Map<String, Integer> contadorDestinos = new HashMap<>();
        contarReservasRecursivo(agencia.getListaReservas(), contadorDestinos, 0);

        List<Map.Entry<String, Integer>> topDestinos = contadorDestinos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return topDestinos;
    }
	
	private void contarReservasRecursivo(List<Reserva> reservas, Map<String, Integer> contadorDestinos, int index) {
        if (index < reservas.size()) {
            String destino = reservas.get(index).getPaqueteTuristico().getDestinos().get(0).getNombre();
            contadorDestinos.put(destino, contadorDestinos.getOrDefault(destino, 0) + 1);
            contarReservasRecursivo(reservas, contadorDestinos, index + 1);
        }
    }
	
	public List<Map.Entry<String, Integer>> obtenerPaquetesMasReservados(int limit) {
        Map<String, Integer> contadorPaquetes = new HashMap<>();
        calcularContadorPaquetesRecursivo(agencia.getListaReservas(), contadorPaquetes, 0);

        List<Map.Entry<String, Integer>> topPaquetes = contadorPaquetes.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return topPaquetes;
    }

    private void calcularContadorPaquetesRecursivo(List<Reserva> reservas, Map<String, Integer> contadorPaquetes, int index) {
        if (index < reservas.size()) {
            Reserva reserva = reservas.get(index);
            String nombrePaquete = reserva.getPaqueteTuristico().getNombre();
            contadorPaquetes.put(nombrePaquete, contadorPaquetes.getOrDefault(nombrePaquete, 0) + 1);
            calcularContadorPaquetesRecursivo(reservas, contadorPaquetes, index + 1);
        }
    }

    public List<Map.Entry<String, Integer>> obtenerGuiasMejorPuntuados() {
        List<Map.Entry<String, Integer>> guiasOrdenados = new ArrayList<>();
        ordenarGuiasRecursivo(agencia.getListaGuiasTuristicos(), guiasOrdenados, 0);

        return guiasOrdenados;
    }

    private void ordenarGuiasRecursivo(List<GuiaTuristico> guias, List<Map.Entry<String, Integer>> guiasOrdenados, int index) {
        if (index < guias.size()) {
            GuiaTuristico guia = guias.get(index);
            double promedio = guia.getEstrellas().stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
            
            guiasOrdenados.add(new AbstractMap.SimpleEntry<>(guia.getNombreCompleto(), (int) promedio));
            ordenarGuiasRecursivo(guias, guiasOrdenados, index + 1);
        }
    }
	
    public List<Map.Entry<String, Integer>> obtenerDestinosMasBuscados() {
        List<Map.Entry<String, Integer>> destinosBuscados = new ArrayList<>();
        ordenarDestinosRecursivo(agencia.getListaDestinos(), destinosBuscados, 0);

        return destinosBuscados;
    }

    private void ordenarDestinosRecursivo(List<Destino> destinos, List<Map.Entry<String, Integer>> destinosBuscados, int index) {
        if (index < destinos.size()) {
            Destino destino = destinos.get(index);
            destinosBuscados.add(new AbstractMap.SimpleEntry<>(destino.getNombre(), destino.getCantidadBusquedas()));
            ordenarDestinosRecursivo(destinos, destinosBuscados, index + 1);
        }
    }
    
    public void generarInforme(String nombreArchivo) {
        String informe = construirInforme();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_ARCHIVO_INFORMES+nombreArchivo))) {
            writer.write(informe);
            System.out.println("Informe generado correctamente en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    private String construirInforme() {
        StringBuilder informe = new StringBuilder();

        informe.append("Top destinos más reservados:\n");
        informe.append(obtenerResultadosString(obtenerDestinosMasReservados(5)));

        informe.append("\nTop paquetes más reservados:\n");
        informe.append(obtenerResultadosString(obtenerPaquetesMasReservados(5)));

        informe.append("\nGuias mejor puntuados:\n");
        informe.append(obtenerResultadosString(obtenerGuiasMejorPuntuados()));

        informe.append("\nDestinos más buscados:\n");
        informe.append(obtenerResultadosString(obtenerDestinosMasBuscados()));

        return informe.toString();
    }

    private <T, U> String obtenerResultadosString(List<Map.Entry<T, U>> resultados) {
        return obtenerResultadosStringRecursivo(resultados, 0, new StringBuilder());
    }

    private <T, U> String obtenerResultadosStringRecursivo(List<Map.Entry<T, U>> resultados, int index, StringBuilder builder) {
        if (index == resultados.size()) {
            return builder.toString();
        }

        Map.Entry<T, U> entry = resultados.get(index);
        builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");

        return obtenerResultadosStringRecursivo(resultados, index + 1, builder);
    }
}
