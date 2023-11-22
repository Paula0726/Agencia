package co.edu.uniquindio.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uniquindio.agencia.exceptions.CalificacionDestinoNoActualizada;
import co.edu.uniquindio.agencia.exceptions.CalificacionGuiaNoActualizada;
import co.edu.uniquindio.agencia.exceptions.ClienteNoActualizado;
import co.edu.uniquindio.agencia.exceptions.ClienteNoCreado;
import co.edu.uniquindio.agencia.exceptions.ClienteNoEliminado;
import co.edu.uniquindio.agencia.exceptions.ReservaNoActualizada;
import co.edu.uniquindio.agencia.exceptions.ReservaNoCreada;
import co.edu.uniquindio.agencia.exceptions.ReservaNoEliminada;
import co.edu.uniquindio.agencia.exceptions.UsuarioNoActualizado;
import co.edu.uniquindio.agencia.exceptions.UsuarioNoCreado;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.util.Persistencia;
import co.edu.uniquindio.util.util;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsuarioImpl {
	private Agencia agencia;
	
	public void crearCliente(String data) throws ClienteNoCreado {  
		Cliente clienteExistente = null;
		Cliente clienteNuevo = null;
		
        String[] contenidoSplit = data.split("@@");
		
		clienteExistente = agencia.obtenerCliente(contenidoSplit[1]);
		if (clienteExistente != null) {
			throw new ClienteNoCreado("Ya existe un cliente con este número de identificación.");
		} else {	
			String nuevoId = obtenerNuevoClienteId(agencia.getListaClientes(), agencia.getListaClientes().size() + 1);
	    	contenidoSplit[0] = nuevoId;  	
			
			clienteNuevo = new Cliente();	
			clienteNuevo = Persistencia.instanciarCliente(clienteNuevo, contenidoSplit, agencia);
            agencia.getListaClientes().add(clienteNuevo);
			
			util.enviarServidor("crear-cliente", data);
		}
	}
	
	private String obtenerNuevoClienteId(List<Cliente> listaClientes, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaClientes.stream().anyMatch(cliente -> nuevoId.equals(cliente.getId()))) {
            return obtenerNuevoClienteId(listaClientes, intento + 1);
        } else {
            return nuevoId;
        }
    }
	
	public void actualizarCliente(Cliente clienteActual, String dataClienteNuevo) throws ClienteNoActualizado {		
		String[] contenidoSplitNuevo = dataClienteNuevo.split("@@");
				
		if (clienteActual.convertirEnCadena().equals(dataClienteNuevo)) {
			throw new ClienteNoActualizado("La información del cliente a actualizar es igual a la original.");
		} else {
			try {
				util.enviarServidor("actualizar-cliente", dataClienteNuevo);
			} finally {
				Persistencia.instanciarCliente(clienteActual, contenidoSplitNuevo, agencia);
			}	
		}						
	}

	public void eliminarClienteUsuario(String identificacion) throws ClienteNoEliminado { 
		Cliente cliente = agencia.obtenerCliente(identificacion);
		Usuario usuario = agencia.obtenerUsuario(cliente.getUsuario().getUsername());
		
		if (cliente == null || usuario == null) {
			throw new ClienteNoEliminado("No existe un cliente o un usuario con el id seleccionado.");
		} else {
			agencia.getListaClientes().remove(cliente);
			agencia.getListaUsuarios().remove(usuario);
			util.enviarServidor("eliminar-cliente", cliente.getId());
			util.enviarServidor("eliminar-usuario", cliente.getId());
		}
	}
	
	public void crearUsuario(String data) throws UsuarioNoCreado {
        Usuario usuarioExistente = null;
        Usuario usuarioNuevo = null;

        String[] contenidoSplit = data.split("@@");

        usuarioExistente = agencia.obtenerUsuario(contenidoSplit[1]);
        if (usuarioExistente != null) {
            throw new UsuarioNoCreado("Ya existe un usuario con este nombre de usuario.");
        } else {
        	String nuevoId = obtenerNuevoUsuarioId(agencia.getListaUsuarios(), agencia.getListaUsuarios().size() + 1);
	    	contenidoSplit[0] = nuevoId;  	
        	
            usuarioNuevo = new Usuario();
            usuarioNuevo = Persistencia.instanciarUsuario(usuarioNuevo, contenidoSplit);
            agencia.getListaUsuarios().add(usuarioNuevo);

            util.enviarServidor("crear-usuario", data);
        }
    }
	
	private String obtenerNuevoUsuarioId(List<Usuario> listaUsuarios, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaUsuarios.stream().anyMatch(usuario -> nuevoId.equals(usuario.getId()))) {
            return obtenerNuevoUsuarioId(listaUsuarios, intento + 1);
        } else {
            return nuevoId;
        }
    }

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
	
	public void crearReserva(String data) throws ReservaNoCreada { 
		Reserva reservaExistente = null;
		Reserva reservaNuevo = null;
		
        String[] contenidoSplit = data.split("@@");
		
        reservaExistente = agencia.obtenerReserva(contenidoSplit[0]);
		if (reservaExistente != null) {
			throw new ReservaNoCreada("Ya existe una reserva con este número de identificación.");
		} else {
			String nuevoId = obtenerNuevoReservaId(agencia.getListaReservas(), agencia.getListaReservas().size() + 1);
	    	contenidoSplit[0] = nuevoId;
			
			reservaNuevo = new Reserva();			
			reservaNuevo = Persistencia.instanciarReserva(reservaNuevo, contenidoSplit, agencia);
            agencia.getListaReservas().add(reservaNuevo);
			
			util.enviarServidor("crear-reserva", data);
		}
	}
	
	private String obtenerNuevoReservaId(List<Reserva> listaReservas, int intento) {
        String nuevoId = String.valueOf(intento);

        if (listaReservas.stream().anyMatch(reserva -> nuevoId.equals(reserva.getId()))) {
            return obtenerNuevoReservaId(listaReservas, intento + 1);
        } else {
            return nuevoId;
        }
    }
	
	public void actualizarReserva(Reserva reservaActual, String dataReservaNuevo) throws ReservaNoActualizada {
		 String[] contenidoSplitNuevo = dataReservaNuevo.split("@@");

	        if (reservaActual.convertirEnCadena().equals(dataReservaNuevo)) {
				throw new ReservaNoActualizada("La información de la reserva a actualizar es igual a la original.");
			} else {
		        try {
		            util.enviarServidor("actualizar-reserva", dataReservaNuevo);
		        } finally {
		            Persistencia.instanciarReserva(reservaActual, contenidoSplitNuevo, agencia);
		        }
		    } 
	}
	
	public void eliminarReserva(String id) throws ReservaNoEliminada {
		Reserva reserva = agencia.obtenerReserva(id);
		
		if (reserva == null) {
			throw new ReservaNoEliminada("No existe una reserva con el id seleccionado.");
		} else {
			agencia.getListaReservas().remove(reserva);
			util.enviarServidor("eliminar-reserva", id);
		}
	}
	
	public List<Reserva> listarReservasPasadasCliente(Cliente cliente) {
        LocalDate fechaActual = LocalDate.now();
        List<Reserva> reservasPasadas = new ArrayList<>();
        return listarReservasPasadasClienteRecursivo(agencia.getListaReservas(), cliente, fechaActual, reservasPasadas, 0);
    }

    private List<Reserva> listarReservasPasadasClienteRecursivo(List<Reserva> reservas, Cliente cliente, LocalDate fechaActual, List<Reserva> acumulador, int index) {
        if (index < reservas.size()) {
            Reserva reserva = reservas.get(index);
            if (reserva.getCliente().equals(cliente) && reserva.getFechaPlanificadaViaje().isBefore(fechaActual)) {
                acumulador.add(reserva);
            }
            return listarReservasPasadasClienteRecursivo(reservas, cliente, fechaActual, acumulador, index + 1);
        } else {
            return acumulador;
        }
    }

	public List<Reserva> listarReservasFuturasCliente(Cliente cliente) {
        LocalDate fechaActual = LocalDate.now();
        List<Reserva> reservasFuturas = new ArrayList<>();
        return listarReservasFuturasClienteRecursivo(agencia.getListaReservas(), cliente, fechaActual, reservasFuturas, 0);
    }

    private List<Reserva> listarReservasFuturasClienteRecursivo(List<Reserva> reservas, Cliente cliente, LocalDate fechaActual, List<Reserva> acumulador, int index) {
        if (index < reservas.size()) {
            Reserva reserva = reservas.get(index);
            if (reserva.getCliente().equals(cliente) && reserva.getFechaPlanificadaViaje().isAfter(fechaActual)) {
                acumulador.add(reserva);
            }
            return listarReservasFuturasClienteRecursivo(reservas, cliente, fechaActual, acumulador, index + 1);
        } else {
            return acumulador;
        }
    }
    
    public boolean verificarInicioSesion(String usuario, String contrasena) {
        List<Usuario> listaUsuarios = agencia.getListaUsuarios();
        return verificarInicioSesionRecursivo(usuario, contrasena, listaUsuarios, 0);
    }
    
    private boolean verificarInicioSesionRecursivo(String usuario, String contrasena, List<Usuario> listaUsuarios, int index) {
        if (index < listaUsuarios.size()) {
            Usuario u = listaUsuarios.get(index);
            if (u.getUsername().equals(usuario) && u.getContrasena().equals(contrasena)) {
                return true;
            } else {
                return verificarInicioSesionRecursivo(usuario, contrasena, listaUsuarios, index + 1);
            }
        }

        return false;
    }    
    
    public void calificarDestino(Reserva reserva, Destino destino, int calificacion, String comentario) throws CalificacionDestinoNoActualizada {
        LocalDate fechaActual = LocalDate.now();
    	if (!reserva.getFechaPlanificadaViaje().isBefore(fechaActual)) {
			throw new CalificacionDestinoNoActualizada("Solo es posible calificar al destino despues del viaje.");
		} else {
			destino.getEstrellas().add(calificacion);
			
			List<String> nuevosComentarios = new ArrayList<>(destino.getComentarios());
	        nuevosComentarios.add(comentario);
	        destino.setComentarios(nuevosComentarios);			
				        	        
	        destino.setCantidadBusquedas(destino.getCantidadBusquedas() + 1);
	        util.enviarServidor("actualizar-destino", destino.convertirEnCadena()); 
        }
    }
    
    public void calificarGuia(Reserva reserva, GuiaTuristico guia, int calificacion, String comentario) throws CalificacionGuiaNoActualizada { 
    	LocalDate fechaActual = LocalDate.now();
    	if (!reserva.getFechaPlanificadaViaje().isBefore(fechaActual)) {
			throw new CalificacionGuiaNoActualizada("Solo es posible calificar al guía despues del viaje.");
		} else {
			guia.getEstrellas().add(calificacion);
			
			List<String> nuevosComentarios = new ArrayList<>(guia.getComentarios());
	        nuevosComentarios.add(comentario);
	        guia.setComentarios(nuevosComentarios);			
			
        	util.enviarServidor("actualizar-guia", guia.convertirEnCadena()); 
        }
    }
    
    public List<Destino> recomendarDestinos(Cliente cliente) {
    	List<String> destinosBuscados = null;
    	if (cliente != null) {
    		destinosBuscados = cliente.getDestinosBuscados();            
    	}
    	
    	List<Destino> destinosRecomendados = new ArrayList<>();
        if (cliente == null || destinosBuscados.isEmpty()) {
            destinosRecomendados = agencia.getListaDestinos().subList(0, 3);
        } else {
            destinosRecomendados = recomendarDestinosRecursivo(agencia.getListaDestinos(), destinosBuscados, destinosRecomendados, 0);
        }
        return destinosRecomendados;
    }

    private List<Destino> recomendarDestinosRecursivo(List<Destino> destinos, List<String> destinosBuscados, List<Destino> acumulador, int index) {
        if (index < destinos.size()) {
            Destino destino = destinos.get(index);
            if (destinosBuscados.stream().anyMatch(busqueda -> destino.getId().equalsIgnoreCase(busqueda))) {
                acumulador.add(destino);
            }
            return recomendarDestinosRecursivo(destinos, destinosBuscados, acumulador, index + 1);
        } else {
            return acumulador;
        }
    }
}
