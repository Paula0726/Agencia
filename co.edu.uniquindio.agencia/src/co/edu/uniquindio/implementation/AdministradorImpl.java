// Importaciones de paquetes y clases necesarios
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

import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.model.*;
import co.edu.uniquindio.util.Persistencia;
import co.edu.uniquindio.util.util;
import lombok.AllArgsConstructor;

// Clase que implementa la lógica para administrar destinos, paquetes turísticos, guías, etc.
public class AdministradorImpl {

    // Ruta para almacenar los informes generados
    public static final String PATH_ARCHIVO_INFORMES = "archivos/reportes/";

    // Instancia de la agencia que se está gestionando
    private Agencia agencia;

    // Mapa para almacenar usuarios y contraseñas de administradores
    private static Map<String, String> authAdmins = new HashMap<>();

    // Constructor que recibe una instancia de la agencia al ser creada
    public AdministradorImpl(Agencia agencia) {
        this.agencia = agencia;
        authAdmins.put("admin1", "super_admin1");
        authAdmins.put("admin2", "super_admin2");
        authAdmins.put("admin3", "super_admin3");
    }

    // Función para crear un destino turístico
    public void crearDestino(String data) throws DestinoNoCreado {
        Destino destinoExistente = null;
        Destino destinoNuevo = null;
        String[] contenidoSplit = data.split("@@");

        // Verifica si ya existe un destino con el mismo nombre
        destinoExistente = agencia.obtenerDestino(contenidoSplit[1]);
        if (destinoExistente != null) {
            throw new DestinoNoCreado("Ya existe un destino con este nombre.");
        } else {
            // Obtiene un nuevo ID para el destino y lo asigna
            String nuevoId = obtenerNuevoDestinoId(agencia.getListaDestinos(), agencia.getListaDestinos().size() + 1);
            contenidoSplit[0] = nuevoId;

            // Instancia el nuevo destino y lo agrega a la lista de destinos de la agencia
            destinoNuevo = new Destino();
            destinoNuevo = Persistencia.instanciarDestino(destinoNuevo, contenidoSplit);
            agencia.getListaDestinos().add(destinoNuevo);

            // Envía información al servidor (se asume que hay un método util.enviarServidor)
            util.enviarServidor("crear-destino", String.join("@@", contenidoSplit));
        }
    }

    // Función recursiva para obtener un nuevo ID para un destino
    private String obtenerNuevoDestinoId(List<Destino> listaDestinos, int intento) {
        String nuevoId = String.valueOf(intento);

        // Si ya existe un destino con el mismo ID, se llama recursivamente con un nuevo intento
        if (listaDestinos.stream().anyMatch(destino -> nuevoId.equals(destino.getId()))) {
            return obtenerNuevoDestinoId(listaDestinos, intento + 1);
        } else {
            return nuevoId;
        }
    }

    // Función para actualizar la información de un destino
    public void actualizarDestino(Destino destinoActual, String dataDestinoNuevo) throws DestinoNoActualizado {
        String[] contenidoSplitNuevo = dataDestinoNuevo.split("@@");
        // Verifica si la información actualizada es igual a la original
        if (destinoActual != null && destinoActual.convertirEnCadena().equals(dataDestinoNuevo)) {
            throw new DestinoNoActualizado("La información del destino a actualizar es igual a la original.");
        } else {
            try {
                // Envía información al servidor (se asume que hay un método util.enviarServidor)
                util.enviarServidor("actualizar-destino", dataDestinoNuevo);
            } finally {
                // Actualiza la información del destino en la instancia local
                Destino destino = agencia.obtenerDestino(contenidoSplitNuevo[1]);
                Persistencia.instanciarDestino(destino, contenidoSplitNuevo);
            }
        }
    }

    // Función para eliminar un destino
    public void eliminarDestino(String nombre) throws DestinoNoEliminado {
        Destino destino = agencia.obtenerDestino(nombre);

        if (destino == null) {
            throw new DestinoNoEliminado("No existe un destino con el id seleccionado.");
        } else {
            // Remueve el destino de la lista de destinos de la agencia
            agencia.getListaDestinos().remove(destino);
            // Envía información al servidor (se asume que hay un método util.enviarServidor)
            util.enviarServidor("eliminar-destino", destino.getId());
        }
    }

    // Función para crear un paquete turístico
    public void crearPaqueteTuristico(String data) throws PaqueteNoCreado {
        PaqueteTuristico paqueteExistente = null;
        PaqueteTuristico paqueteNuevo = null;

        String[] contenidoSplit = data.split("@@");

        // Verifica si ya existe un paquete turístico con el mismo nombre
        paqueteExistente = agencia.obtenerPaqueteTuristico(contenidoSplit[1]);
        if (paqueteExistente != null) {
            throw new PaqueteNoCreado("Ya existe un paquete turístico con este nombre.");
        } else {
            // Obtiene un nuevo ID para el paquete y lo asigna
            String nuevoId = obtenerNuevoPaqueteId(agencia.getListaPaquetesTuristicos(), agencia.getListaPaquetesTuristicos().size() + 1) + "";
            contenidoSplit[0] = nuevoId;

            // Instancia el nuevo paquete y lo agrega a la lista de paquetes turísticos de la agencia
            paqueteNuevo = new PaqueteTuristico();
            paqueteNuevo = Persistencia.instanciarPaquete(paqueteNuevo, contenidoSplit, agencia);
            agencia.getListaPaquetesTuristicos().add(paqueteNuevo);

            // Envía información al servidor (se asume que hay un método util.enviarServidor)
            util.enviarServidor("crear-paquete", String.join("@@", contenidoSplit));
        }
    }

    // Función recursiva para obtener un nuevo ID para un paquete turístico
    private String obtenerNuevoPaqueteId(List<PaqueteTuristico> listaPaquetes, int intento) {
        String nuevoId = String.valueOf(intento);

        // Si ya existe un paquete con el mismo ID, se llama recursivamente con un nuevo intento
        if (listaPaquetes.stream().anyMatch(paquete -> nuevoId.equals(paquete.getId()))) {
            return obtenerNuevoPaqueteId(listaPaquetes, intento + 1);
        } else {
            return nuevoId;
        }
    }

    // Función para actualizar la información de un paquete turístico
    public void actualizarPaqueteTuristico(PaqueteTuristico paqueteActual, String dataPaqueteNuevo) throws PaqueteNoActualizado {
        String[] contenidoSplitNuevo = dataPaqueteNuevo.split("@@");

        // Verifica si la información actualizada es igual a la original
        if (paqueteActual != null && paqueteActual.convertirEnCadena().equals(dataPaqueteNuevo)) {
            throw new PaqueteNoActualizado("La información del paquete a actualizar es igual a la original.");
        } else {
            try {
                // Envía información al servidor (se asume que hay un método util.enviarServidor)
                util.enviarServidor("actualizar-paquete", dataPaqueteNuevo);
            } finally {
                // Actualiza la información del paquete en la instancia local
                PaqueteTuristico paquete = agencia.obtenerPaqueteTuristicoId(contenidoSplitNuevo[0]);
                Persistencia.instanciarPaquete(paquete, contenidoSplitNuevo, agencia);
            }
        }
    }

    // Función para eliminar un paquete turístico
    public void eliminarPaqueteTuristico(String nombre) throws PaqueteNoEliminado {
        PaqueteTuristico paquete = agencia.obtenerPaqueteTuristico(nombre);

        if (paquete == null) {
            throw new PaqueteNoEliminado("No existe un paquete con el nombre seleccionado.");
        } else {
            // Remueve el paquete de la lista de paquetes turísticos de la agencia
            agencia.getListaPaquetesTuristicos().remove(paquete);
            // Envía información al servidor (se asume que hay un método util.enviarServidor)
            util.enviarServidor("eliminar-paquete", paquete.getId());
        }
    }

    // Función para crear un guía turístico
    public void crearGuiaTuristico(String data) throws GuiaNoCreado {
        GuiaTuristico guiaExistente = null;
        GuiaTuristico guiaNuevo = null;

        String[] contenidoSplit = data.split("@@");

        // Verifica si ya existe un guía turístico con el mismo número de identificación
        guiaExistente = agencia.obtenerGuiaTuristico(contenidoSplit[1]);
        if (guiaExistente != null) {
            throw new GuiaNoCreado("Ya existe un guía turístico con este número de identificación.");
        } else {
            // Obtiene un nuevo ID para el guía y lo asigna
            String nuevoId = obtenerNuevoGuiaId(agencia.getListaGuiasTuristicos(), agencia.getListaGuiasTuristicos().size() + 1);
            contenidoSplit[0] = nuevoId;

            // Instancia el nuevo guía y lo agrega a la lista de guías turísticos de la agencia
            guiaNuevo = new GuiaTuristico();
            guiaNuevo = Persistencia.instanciarGuia(guiaNuevo, contenidoSplit);
            agencia.getListaGuiasTuristicos().add(guiaNuevo);

            // Envía información al servidor (se asume que hay un método util.enviarServidor)
            util.enviarServidor("crear-guia", String.join("@@", contenidoSplit));
        }
    }

    // Función recursiva para obtener un nuevo ID para un guía turístico
    private String obtenerNuevoGuiaId(List<GuiaTuristico> listaGuias, int intento) {
        String nuevoId = String.valueOf(intento);

        // Si ya existe un guía con el mismo ID, se llama recursivamente con un nuevo intento
        if (listaGuias.stream().anyMatch(guia -> nuevoId.equals(guia.getId()))) {
            return obtenerNuevoGuiaId(listaGuias, intento + 1);
        } else {
            return nuevoId;
        }
    }

    // Función para actualizar la información de un guía turístico
    public void actualizarGuiaTuristico(GuiaTuristico guiaActual, String dataGuiaNuevo) throws PaqueteNoActualizado {
        String[] contenidoSplitNuevo = dataGuiaNuevo.split("@@");

        // Verifica si la información actualizada es igual a la original
        if (guiaActual != null && guiaActual.convertirEnCadena().equals(dataGuiaNuevo)) {
            throw new PaqueteNoActualizado("La información del guía a actualizar es igual a la original.");
        } else {
            try {
                // Envía información al servidor (se asume que hay un método util.enviarServidor)
                util.enviarServidor("actualizar-guia", dataGuiaNuevo);
            } finally {
                // Actualiza la información del guía en la instancia local
                GuiaTuristico guia = agencia.obtenerGuiaTuristico(contenidoSplitNuevo[1]);
                Persistencia.instanciarGuia(guia, contenidoSplitNuevo);
            }
        }
    }

    // Función para eliminar un guía turístico
    public void eliminarGuiaTuristico(String identificacion) throws GuiaNoEliminado {
        GuiaTuristico guia = agencia.obtenerGuiaTuristico(identificacion);

        if (guia == null) {
            throw new GuiaNoEliminado("No existe un guía con el número de identificación seleccionado.");
        } else {
            // Remueve el guía de la lista de guías turísticos de la agencia
            agencia.getListaGuiasTuristicos().remove(guia);
            // Envía información al servidor (se asume que hay un método util.enviarServidor)
            util.enviarServidor("eliminar-guia", guia.getId());
        }
    }

    // Función para verificar el inicio de sesión de un administrador
    public boolean verificarInicioSesion(String usuario, String contrasena) {
        return verificarInicioSesionRecursivo(usuario, contrasena, authAdmins);
    }

    // Función recursiva para verificar el inicio de sesión de un administrador
    public boolean verificarInicioSesionRecursivo(String usuario, String contrasena, Map<String, String> mapUsuariosContrasenas) {
        if (mapUsuariosContrasenas.containsKey(usuario)) {
            if (mapUsuariosContrasenas.get(usuario).equals(contrasena)) {
                return true;
            }
        }

        return false;
    }

 // Función para obtener los destinos más reservados
    public List<Entry<String, Integer>> obtenerDestinosMasReservados(int limit) {
        // Crea un mapa para contar la cantidad de reservas por destino
        Map<String, Integer> contadorDestinos = new HashMap<>();
        contarReservasRecursivo(agencia.getListaReservas(), contadorDestinos, 0);

        // Ordena el mapa por valor de forma descendente y limita el resultado al número especificado
        List<Map.Entry<String, Integer>> topDestinos = contadorDestinos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return topDestinos;
    }

    // Función recursiva para contar las reservas por destino
    private void contarReservasRecursivo(List<Reserva> reservas, Map<String, Integer> contadorDestinos, int index) {
        if (index < reservas.size()) {
            String destino = reservas.get(index).getPaqueteTuristico().getDestinos().get(0).getNombre();
            contadorDestinos.put(destino, contadorDestinos.getOrDefault(destino, 0) + 1);
            contarReservasRecursivo(reservas, contadorDestinos, index + 1);
        }
    }

    // Función para obtener los paquetes más reservados
    public List<Map.Entry<String, Integer>> obtenerPaquetesMasReservados(int limit) {
        // Crea un mapa para contar la cantidad de reservas por paquete turístico
        Map<String, Integer> contadorPaquetes = new HashMap<>();
        calcularContadorPaquetesRecursivo(agencia.getListaReservas(), contadorPaquetes, 0);

        // Ordena el mapa por valor de forma descendente y limita el resultado al número especificado
        List<Map.Entry<String, Integer>> topPaquetes = contadorPaquetes.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return topPaquetes;
    }

    // Función recursiva para contar las reservas por paquete turístico
    private void calcularContadorPaquetesRecursivo(List<Reserva> reservas, Map<String, Integer> contadorPaquetes, int index) {
        if (index < reservas.size()) {
            Reserva reserva = reservas.get(index);
            String nombrePaquete = reserva.getPaqueteTuristico().getNombre();
            contadorPaquetes.put(nombrePaquete, contadorPaquetes.getOrDefault(nombrePaquete, 0) + 1);
            calcularContadorPaquetesRecursivo(reservas, contadorPaquetes, index + 1);
        }
    }

    // Función para obtener los guías turísticos mejor puntuados
    public List<Map.Entry<String, Integer>> obtenerGuiasMejorPuntuados() {
        // Lista para almacenar los guías ordenados por promedio de estrellas
        List<Map.Entry<String, Integer>> guiasOrdenados = new ArrayList<>();
        ordenarGuiasRecursivo(agencia.getListaGuiasTuristicos(), guiasOrdenados, 0);

        return guiasOrdenados;
    }

    // Función recursiva para ordenar los guías por promedio de estrellas
    private void ordenarGuiasRecursivo(List<GuiaTuristico> guias, List<Map.Entry<String, Integer>> guiasOrdenados, int index) {
        if (index < guias.size()) {
            GuiaTuristico guia = guias.get(index);
            double promedio = guia.getEstrellas().stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);

            guiasOrdenados.add(new AbstractMap.SimpleEntry<>(guia.getNombreCompleto(), (int) promedio));
            ordenarGuiasRecursivo(guias, guiasOrdenados, index + 1);
        }
    }

    // Función para obtener los destinos más buscados
    public List<Map.Entry<String, Integer>> obtenerDestinosMasBuscados() {
        // Lista para almacenar los destinos ordenados por cantidad de búsquedas
        List<Map.Entry<String, Integer>> destinosBuscados = new ArrayList<>();
        ordenarDestinosRecursivo(agencia.getListaDestinos(), destinosBuscados, 0);

        return destinosBuscados;
    }

    // Función recursiva para ordenar los destinos por cantidad de búsquedas
    private void ordenarDestinosRecursivo(List<Destino> destinos, List<Map.Entry<String, Integer>> destinosBuscados, int index) {
        if (index < destinos.size()) {
            Destino destino = destinos.get(index);
            destinosBuscados.add(new AbstractMap.SimpleEntry<>(destino.getNombre(), destino.getCantidadBusquedas()));
            ordenarDestinosRecursivo(destinos, destinosBuscados, index + 1);
        }
    }
    
    // Función para generar un informe y escribirlo en un archivo
    public void generarInforme(String nombreArchivo) {
        // Construir el contenido del informe
        String informe = construirInforme();

        // Intentar escribir el informe en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_ARCHIVO_INFORMES + nombreArchivo))) {
            writer.write(informe);
            System.out.println("Informe generado correctamente en " + nombreArchivo);
        } catch (IOException e) {
            // Manejar errores de escritura en el archivo
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    // Función para construir el contenido del informe
    private String construirInforme() {
        // Inicializar un StringBuilder para construir el informe
        StringBuilder informe = new StringBuilder();

        // Agregar secciones específicas al informe
        informe.append("Top destinos más reservados:\n");
        informe.append(obtenerResultadosString(obtenerDestinosMasReservados(5)));

        informe.append("\nTop paquetes más reservados:\n");
        informe.append(obtenerResultadosString(obtenerPaquetesMasReservados(5)));

        informe.append("\nGuias mejor puntuados:\n");
        informe.append(obtenerResultadosString(obtenerGuiasMejorPuntuados()));

        informe.append("\nDestinos más buscados:\n");
        informe.append(obtenerResultadosString(obtenerDestinosMasBuscados()));

        // Convertir el StringBuilder a una cadena y devolver el informe completo
        return informe.toString();
    }

    // Función para obtener una representación en cadena de los resultados
    private <T, U> String obtenerResultadosString(List<Map.Entry<T, U>> resultados) {
        // Llamar a la función recursiva para construir la cadena de resultados
        return obtenerResultadosStringRecursivo(resultados, 0, new StringBuilder());
    }

    // Función recursiva para construir la representación en cadena de los resultados
    private <T, U> String obtenerResultadosStringRecursivo(List<Map.Entry<T, U>> resultados, int index, StringBuilder builder) {
        // Verificar si se han procesado todos los resultados
        if (index == resultados.size()) {
            // Devolver la cadena de resultados construida
            return builder.toString();
        }

        // Obtener la entrada actual y agregarla al StringBuilder
        Map.Entry<T, U> entry = resultados.get(index);
        builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");

        // Llamar recursivamente para procesar la siguiente entrada
        return obtenerResultadosStringRecursivo(resultados, index + 1, builder);
    }

}

