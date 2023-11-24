// Importación de paquetes y clases necesarios
package co.edu.uniquindio.implementation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.model.*;
import co.edu.uniquindio.util.Persistencia;
import co.edu.uniquindio.util.util;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsuarioImpl {
    private Agencia agencia;

    // Método para crear un nuevo cliente
    public void crearCliente(String data) throws ClienteNoCreado {
        // Variables locales para almacenar información del cliente
        Cliente clienteExistente = null;
        Cliente clienteNuevo = null;

        // Dividir la cadena de datos en partes utilizando '@@'
        String[] contenidoSplit = data.split("@@");

        // Verificar si ya existe un cliente con la misma identificación
        clienteExistente = agencia.obtenerCliente(contenidoSplit[1]);
        if (clienteExistente != null) {
            throw new ClienteNoCreado("Ya existe un cliente con este número de identificación.");
        } else {
            // Generar un nuevo ID para el cliente
            String nuevoId = obtenerNuevoClienteId(agencia.getListaClientes(), agencia.getListaClientes().size() + 1);
            contenidoSplit[0] = nuevoId;

            // Crear un nuevo cliente y agregarlo a la lista de clientes
            clienteNuevo = new Cliente();
            clienteNuevo = Persistencia.instanciarCliente(clienteNuevo, contenidoSplit, agencia);
            agencia.getListaClientes().add(clienteNuevo);

            // Enviar información del nuevo cliente al servidor
            util.enviarServidor("crear-cliente", String.join("@@", contenidoSplit));
        }
    }

    // Método privado para obtener un nuevo ID para el cliente
    private String obtenerNuevoClienteId(List<Cliente> listaClientes, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaClientes.stream().anyMatch(cliente -> nuevoId.equals(cliente.getId()))) {
            return obtenerNuevoClienteId(listaClientes, intento + 1);
        } else {
            return nuevoId;
        }
    }

    // Método para actualizar la información de un cliente
    public void actualizarCliente(Cliente clienteActual, String dataClienteNuevo) throws ClienteNoActualizado {
        // Dividir la cadena de datos del nuevo cliente en partes
        String[] contenidoSplitNuevo = dataClienteNuevo.split("@@");

        // Verificar si la información del cliente a actualizar es diferente a la original
        if (clienteActual.convertirEnCadena().equals(dataClienteNuevo)) {
            throw new ClienteNoActualizado("La información del cliente a actualizar es igual a la original.");
        } else {
            try {
                // Enviar información actualizada del cliente al servidor
                util.enviarServidor("actualizar-cliente", dataClienteNuevo);
            } finally {
                // Actualizar la instancia del cliente actual con la nueva información
                Persistencia.instanciarCliente(clienteActual, contenidoSplitNuevo, agencia);
            }
        }
    }

    // Método para eliminar un cliente y su usuario asociado
    public void eliminarClienteUsuario(String identificacion) throws ClienteNoEliminado {
        // Obtener el cliente y usuario correspondientes a la identificación proporcionada
        Cliente cliente = agencia.obtenerCliente(identificacion);
        Usuario usuario = agencia.obtenerUsuario(cliente.getUsuario().getUsername());

        // Verificar si el cliente y el usuario existen
        if (cliente == null || usuario == null) {
            throw new ClienteNoEliminado("No existe un cliente o un usuario con el id seleccionado.");
        } else {
            // Eliminar el cliente de la lista de clientes y el usuario de la lista de usuarios
            agencia.getListaClientes().remove(cliente);
            agencia.getListaUsuarios().remove(usuario);

            // Enviar información de eliminación al servidor
            util.enviarServidor("eliminar-cliente", cliente.getId());
            util.enviarServidor("eliminar-usuario", cliente.getId());
        }
    }

    // Método para crear un nuevo usuario
    public void crearUsuario(String data) throws UsuarioNoCreado {
        // Variables locales para almacenar información del usuario
        Usuario usuarioExistente = null;
        Usuario usuarioNuevo = null;

        // Dividir la cadena de datos en partes utilizando '@@'
        String[] contenidoSplit = data.split("@@");

        // Verificar si ya existe un usuario con el mismo nombre de usuario
        usuarioExistente = agencia.obtenerUsuario(contenidoSplit[1]);
        if (usuarioExistente != null) {
            throw new UsuarioNoCreado("Ya existe un usuario con este nombre de usuario.");
        } else {
            // Generar un nuevo ID para el usuario
            String nuevoId = obtenerNuevoUsuarioId(agencia.getListaUsuarios(), agencia.getListaUsuarios().size() + 1);
            contenidoSplit[0] = nuevoId;

            // Crear un nuevo usuario y agregarlo a la lista de usuarios
            usuarioNuevo = new Usuario();
            usuarioNuevo = Persistencia.instanciarUsuario(usuarioNuevo, contenidoSplit);
            agencia.getListaUsuarios().add(usuarioNuevo);

            // Enviar información del nuevo usuario al servidor
            util.enviarServidor("crear-usuario", String.join("@@", contenidoSplit));
        }
    }

    // Método privado para obtener un nuevo ID para el usuario
    private String obtenerNuevoUsuarioId(List<Usuario> listaUsuarios, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaUsuarios.stream().anyMatch(usuario -> nuevoId.equals(usuario.getId()))) {
            return obtenerNuevoUsuarioId(listaUsuarios, intento + 1);
        } else {
            return nuevoId;
        }
    } 
    
    // Método privado para obtener un nuevo ID para el usuario
    public void actualizarUsuario(Usuario usuarioActual, String dataUsuarioNuevo) throws UsuarioNoActualizado {
        String[] contenidoSplitNuevo = dataUsuarioNuevo.split("@@");

        if (usuarioActual.convertirEnCadena().equals(dataUsuarioNuevo)) {
			throw new UsuarioNoActualizado("La información del usuario a actualizar es igual a la original.");
		} else {
	        try {
	            util.enviarServidor("actualizar-usuario", dataUsuarioNuevo);
	        } finally {
	            Persistencia.instanciarUsuario(usuarioActual, contenidoSplitNuevo);
	        }
	    } 
    }      

    // Método para crear una nueva reserva
    public void crearReserva(String data) throws ReservaNoCreada {
        // Variables locales para almacenar información de la reserva
        Reserva reservaExistente = null;
        Reserva reservaNuevo = null;

        // Dividir la cadena de datos en partes utilizando '@@'
        String[] contenidoSplit = data.split("@@");

        // Verificar si ya existe una reserva con el mismo número de identificación
        reservaExistente = agencia.obtenerReserva(contenidoSplit[0]);
        if (reservaExistente != null) {
            throw new ReservaNoCreada("Ya existe una reserva con este número de identificación.");
        } else {
            // Generar un nuevo ID para la reserva
            String nuevoId = obtenerNuevoReservaId(agencia.getListaReservas(), agencia.getListaReservas().size() + 1);
            contenidoSplit[0] = nuevoId;

            // Crear una nueva reserva y agregarla a la lista de reservas
            reservaNuevo = new Reserva();
            reservaNuevo = Persistencia.instanciarReserva(reservaNuevo, contenidoSplit, agencia);
            agencia.getListaReservas().add(reservaNuevo);

            // Enviar información de la nueva reserva al servidor
            util.enviarServidor("crear-reserva", String.join("@@", contenidoSplit));
        }
    }

    // Método privado para obtener un nuevo ID para la reserva
    private String obtenerNuevoReservaId(List<Reserva> listaReservas, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaReservas.stream().anyMatch(reserva -> nuevoId.equals(reserva.getId()))) {
            return obtenerNuevoReservaId(listaReservas, intento + 1);
        } else {
            return nuevoId;
        }
    }

    // Método para actualizar la información de una reserva
    public void actualizarReserva(Reserva reservaActual, String dataReservaNuevo) throws ReservaNoActualizada {
        // Dividir la cadena de datos de la nueva reserva en partes
        String[] contenidoSplitNuevo = dataReservaNuevo.split("@@");

        // Verificar si la información de la reserva a actualizar es diferente a la original
        if (reservaActual.convertirEnCadena().equals(dataReservaNuevo)) {
            throw new ReservaNoActualizada("La información de la reserva a actualizar es igual a la original.");
        } else {
            try {
                // Enviar información actualizada de la reserva al servidor
                util.enviarServidor("actualizar-reserva", dataReservaNuevo);
            } finally {
                // Actualizar la instancia de la reserva actual con la nueva información
                Persistencia.instanciarReserva(reservaActual, contenidoSplitNuevo, agencia);
            }
        }
    }

    // Método para eliminar una reserva
    public void eliminarReserva(String id) throws ReservaNoEliminada {
        // Obtener la reserva correspondiente al ID proporcionado
        Reserva reserva = agencia.obtenerReserva(id);

        // Verificar si la reserva existe
        if (reserva == null) {
            throw new ReservaNoEliminada("No existe una reserva con el ID seleccionado.");
        } else {
            // Eliminar la reserva de la lista de reservas
            agencia.getListaReservas().remove(reserva);

            // Enviar información de eliminación al servidor
            util.enviarServidor("eliminar-reserva", id);
        }
    }

 // Método para listar reservas pasadas de un cliente
    public List<Reserva> listarReservasPasadasCliente(Cliente cliente) {
        // Obtiene la fecha actual
        LocalDate fechaActual = LocalDate.now();
        
        // Crea una lista para almacenar las reservas pasadas
        List<Reserva> reservasPasadas = new ArrayList<>();
        
        // Llama al método recursivo para realizar la búsqueda de reservas pasadas
        return listarReservasPasadasClienteRecursivo(agencia.getListaReservas(), cliente, fechaActual, reservasPasadas, 0);
    }

    // Método privado para listar reservas pasadas de un cliente de forma recursiva
    private List<Reserva> listarReservasPasadasClienteRecursivo(List<Reserva> reservas, Cliente cliente, LocalDate fechaActual, List<Reserva> acumulador, int index) {
        // Verifica si aún hay reservas por revisar
        if (index < reservas.size()) {
            // Obtiene la reserva actual
            Reserva reserva = reservas.get(index);
            
            // Comprueba si la reserva pertenece al cliente y si la fecha planificada del viaje es anterior a la fecha actual
            if (reserva.getCliente().equals(cliente) && reserva.getFechaPlanificadaViaje().isBefore(fechaActual)) {
                // Agrega la reserva a la lista de reservas pasadas
                acumulador.add(reserva);
            }
            
            // Llama recursivamente al método para la siguiente reserva
            return listarReservasPasadasClienteRecursivo(reservas, cliente, fechaActual, acumulador, index + 1);
        } else {
            // Si no hay más reservas, devuelve la lista acumulada de reservas pasadas
            return acumulador;
        }
    }


    // Método para listar reservas futuras de un cliente
    public List<Reserva> listarReservasFuturasCliente(Cliente cliente) {
        // Obtiene la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Lista para almacenar las reservas futuras del cliente
        List<Reserva> reservasFuturas = new ArrayList<>();
        // Llama al método privado recursivo para obtener las reservas futuras
        return listarReservasFuturasClienteRecursivo(agencia.getListaReservas(), cliente, fechaActual, reservasFuturas, 0);
    }

    // Método privado para listar reservas futuras de un cliente de forma recursiva
    private List<Reserva> listarReservasFuturasClienteRecursivo(List<Reserva> reservas, Cliente cliente, LocalDate fechaActual, List<Reserva> acumulador, int index) {
        // Verifica si aún hay reservas por procesar
        if (index < reservas.size()) {
            // Obtiene la reserva actual
            Reserva reserva = reservas.get(index);
            // Verifica si la reserva pertenece al cliente y si la fecha planificada de viaje es posterior a la fecha actual
            if (reserva.getCliente().equals(cliente) && reserva.getFechaPlanificadaViaje().isAfter(fechaActual)) {
                // Agrega la reserva al acumulador si cumple con los criterios
                acumulador.add(reserva);
            }
            // Llama recursivamente al método con el siguiente índice
            return listarReservasFuturasClienteRecursivo(reservas, cliente, fechaActual, acumulador, index + 1);
        } else {
            // Retorna el acumulador cuando se han procesado todas las reservas
            return acumulador;
        }
    }


 // Método para verificar el inicio de sesión de un usuario
    public boolean verificarInicioSesion(String usuario, String contrasena) {
        // Obtener la lista de usuarios de la agencia
        List<Usuario> listaUsuarios = agencia.getListaUsuarios();
        // Llamar al método privado para realizar la verificación de inicio de sesión de forma recursiva
        return verificarInicioSesionRecursivo(usuario, contrasena, listaUsuarios, 0);
    }

    // Método privado para verificar el inicio de sesión de un usuario de forma recursiva
    private boolean verificarInicioSesionRecursivo(String usuario, String contrasena, List<Usuario> listaUsuarios, int index) {
        // Verificar si el índice está dentro de los límites de la lista de usuarios
        if (index < listaUsuarios.size()) {
            // Obtener el usuario en la posición actual del índice
            Usuario u = listaUsuarios.get(index);
            // Verificar si el nombre de usuario y la contraseña coinciden
            if (u.getUsername().equals(usuario) && u.getContrasena().equals(contrasena)) {
                // Devolver true si las credenciales son correctas
                return true;
            } else {
                // Llamar recursivamente al método con el siguiente índice si las credenciales no son correctas
                return verificarInicioSesionRecursivo(usuario, contrasena, listaUsuarios, index + 1);
            }
        }
        // Devolver false si el índice está fuera de los límites de la lista
        return false;
    }


    /**
     * Califica un destino después de un viaje, agregando la calificación y comentario al destino,
     * actualizando la cantidad de búsquedas y enviando la información al servidor.
     * @param reserva La reserva asociada al viaje.
     * @param destino El destino a calificar.
     * @param calificacion La calificación asignada al destino.
     * @param comentario El comentario asociado a la calificación.
     * @throws CalificacionDestinoNoActualizada Se lanza si se intenta calificar el destino antes del viaje.
     */
    public void calificarDestino(Reserva reserva, Destino destino, int calificacion, String comentario)
            throws CalificacionDestinoNoActualizada {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Verificar si la fecha planificada del viaje ya ocurrió
        if (!reserva.getFechaPlanificadaViaje().isBefore(fechaActual)) {
            throw new CalificacionDestinoNoActualizada("Solo es posible calificar al destino después del viaje.");
        } else {
            // Agregar la calificación y comentario al destino
            destino.getEstrellas().add(calificacion);

            List<String> nuevosComentarios = new ArrayList<>(destino.getComentarios());
            nuevosComentarios.add(comentario);
            destino.setComentarios(nuevosComentarios);

            // Actualizar la cantidad de búsquedas y enviar información al servidor
            destino.setCantidadBusquedas(destino.getCantidadBusquedas() + 1);
            util.enviarServidor("actualizar-destino", destino.convertirEnCadena());
        }
    }

    /**
     * Califica a un guía turístico después de un viaje, agregando la calificación y comentario al guía,
     * y enviando la información al servidor.
     * @param reserva La reserva asociada al viaje.
     * @param guia El guía turístico a calificar.
     * @param calificacion La calificación asignada al guía.
     * @param comentario El comentario asociado a la calificación.
     * @throws CalificacionGuiaNoActualizada Se lanza si se intenta calificar al guía antes del viaje.
     */
    public void calificarGuia(Reserva reserva, GuiaTuristico guia, int calificacion, String comentario)
            throws CalificacionGuiaNoActualizada {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Verificar si la fecha planificada del viaje ya ocurrió
        if (!reserva.getFechaPlanificadaViaje().isBefore(fechaActual)) {
            throw new CalificacionGuiaNoActualizada("Solo es posible calificar al guía después del viaje.");
        } else {
            // Agregar la calificación y comentario al guía
            guia.getEstrellas().add(calificacion);

            List<String> nuevosComentarios = new ArrayList<>(guia.getComentarios());
            nuevosComentarios.add(comentario);
            guia.setComentarios(nuevosComentarios);

            // Enviar información al servidor
            util.enviarServidor("actualizar-guia", guia.convertirEnCadena());
        }
    }


 // Método para recomendar destinos a un cliente
    public List<Destino> recomendarDestinos(Cliente cliente) {
        // Obtener la lista de destinos buscados por el cliente
        List<String> destinosBuscados = null;
        if (cliente != null) {
            destinosBuscados = cliente.getDestinosBuscados();
        }

        // Lista para almacenar los destinos recomendados
        List<Destino> destinosRecomendados = new ArrayList<>();

        // Verificar si el cliente no existe o no ha buscado destinos
        if (cliente == null || destinosBuscados.isEmpty()) {
            // Si no hay información de búsquedas previas, se recomiendan los primeros 3 destinos
            destinosRecomendados = agencia.getListaDestinos().subList(0, 3);
        } else {
            // Recomendar destinos basados en búsquedas previas utilizando un método recursivo
            destinosRecomendados = recomendarDestinosRecursivo(agencia.getListaDestinos(), destinosBuscados, new ArrayList<>(), 0);
        }

        // Devolver la lista de destinos recomendados
        return destinosRecomendados;
    }

    // Método privado para recomendar destinos de forma recursiva
    private List<Destino> recomendarDestinosRecursivo(List<Destino> destinos, List<String> destinosBuscados, List<Destino> acumulador, int index) {
        // Verificar si aún hay destinos por revisar en la lista
        if (index < destinos.size()) {
            // Obtener el destino actual
            Destino destino = destinos.get(index);

            // Verificar si el destino ha sido buscado por el cliente (ignorando mayúsculas y minúsculas)
            if (destinosBuscados.stream().anyMatch(busqueda -> destino.getId().equalsIgnoreCase(busqueda))) {
                // Si el destino ha sido buscado, agregarlo a la lista de destinos recomendados
                acumulador.add(destino);
            }

            // Llamada recursiva para el siguiente destino en la lista
            return recomendarDestinosRecursivo(destinos, destinosBuscados, acumulador, index + 1);
        } else {
            // Si se han revisado todos los destinos, devolver la lista acumulada de destinos recomendados
            return acumulador;
        }
    }

}
