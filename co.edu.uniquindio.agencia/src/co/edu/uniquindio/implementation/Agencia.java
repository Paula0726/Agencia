package co.edu.uniquindio.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.edu.uniquindio.agencia.exceptions.CalificacionDestinoNoActualizada;
import co.edu.uniquindio.agencia.exceptions.CalificacionGuiaNoActualizada;
import co.edu.uniquindio.agencia.exceptions.ClienteNoActualizado;
import co.edu.uniquindio.agencia.exceptions.ClienteNoCreado;
import co.edu.uniquindio.agencia.exceptions.ClienteNoEliminado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoActualizado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoCreado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoEliminado;
import co.edu.uniquindio.agencia.exceptions.GuiaNoCreado;
import co.edu.uniquindio.agencia.exceptions.GuiaNoEliminado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoActualizado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoCreado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoEliminado;
import co.edu.uniquindio.agencia.exceptions.ReservaNoActualizada;
import co.edu.uniquindio.agencia.exceptions.ReservaNoCreada;
import co.edu.uniquindio.agencia.exceptions.ReservaNoEliminada;
import co.edu.uniquindio.agencia.exceptions.UsuarioNoActualizado;
import co.edu.uniquindio.agencia.exceptions.UsuarioNoCreado;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.agencia.services.IAgenciaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agencia implements IAgenciaService {		
	
	private AdministradorImpl administrador;
	private UsuarioImpl usuario;
	private Cliente clienteSession;
	
	private static final long serialVersionUID = 1L;
	ArrayList<Usuario> listaUsuarios = new ArrayList<>();
	ArrayList<Cliente> listaClientes = new ArrayList<>();
	ArrayList<Destino> listaDestinos = new ArrayList<>();
	ArrayList<GuiaTuristico> listaGuiasTuristicos = new ArrayList<>();
	ArrayList<PaqueteTuristico> listaPaquetesTuristicos = new ArrayList<>();
	ArrayList<Reserva> listaReservas = new ArrayList<>();
			
	public Agencia init() {
		administrador = new AdministradorImpl(this);
		usuario = new UsuarioImpl(this);
		return this;
	}
	
	public Cliente obtenerCliente(String identificacion) {
        return obtenerClienteRecursivo(listaClientes, identificacion, 0);
    }

    private Cliente obtenerClienteRecursivo(List<Cliente> clientes, String identificacion, int index) {
        if (index < clientes.size()) {
            Cliente cliente = clientes.get(index);
            if (cliente.getIdentificacion().equals(identificacion)) {
                return cliente;
            }
            return obtenerClienteRecursivo(clientes, identificacion, index + 1);
        } else {
            return null;
        }
    }
    
    public Cliente obtenerClienteId(String id) {
        return obtenerClienteRecursivoId(listaClientes, id, 0);
    }

    private Cliente obtenerClienteRecursivoId(List<Cliente> clientes, String id, int index) {
        if (index < clientes.size()) {
            Cliente cliente = clientes.get(index);
            if (cliente.getId().equals(id)) {
                return cliente;
            }
            return obtenerClienteRecursivoId(clientes, id, index + 1);
        } else {
            return null;
        }
    }
	
    public Destino obtenerDestino(String nombre) {
        return obtenerDestinoRecursivo(listaDestinos, nombre, 0);
    }

    private Destino obtenerDestinoRecursivo(List<Destino> destinos, String nombre, int index) {
        if (index < destinos.size()) {
            Destino destino = destinos.get(index);
            if (destino.getNombre().equals(nombre)) {
                return destino;
            }
            return obtenerDestinoRecursivo(destinos, nombre, index + 1);
        } else {
            return null;
        }
    }
	
    public PaqueteTuristico obtenerPaqueteTuristico(String nombre) {
        return obtenerPaqueteTuristicoRecursivo(listaPaquetesTuristicos, nombre, 0);
    }

    private PaqueteTuristico obtenerPaqueteTuristicoRecursivo(List<PaqueteTuristico> paquetes, String nombre, int index) {
        if (index < paquetes.size()) {
            PaqueteTuristico paquete = paquetes.get(index);
            if (paquete.getNombre().equals(nombre)) {
                return paquete;
            }
            return obtenerPaqueteTuristicoRecursivo(paquetes, nombre, index + 1);
        } else {
            return null;
        }
    }
    
    public PaqueteTuristico obtenerPaqueteTuristicoId(String id) {
        return obtenerPaqueteTuristicoRecursivoId(listaPaquetesTuristicos, id, 0);
    }

    private PaqueteTuristico obtenerPaqueteTuristicoRecursivoId(List<PaqueteTuristico> paquetes, String id, int index) {
        if (index < paquetes.size()) {
            PaqueteTuristico paquete = paquetes.get(index);
            if (paquete.getId().equals(id)) {
                return paquete;
            }
            return obtenerPaqueteTuristicoRecursivoId(paquetes, id, index + 1);
        } else {
            return null;
        }
    }
	
    public GuiaTuristico obtenerGuiaTuristico(String identificacion) {
        return obtenerGuiaTuristicoRecursivo(listaGuiasTuristicos, identificacion, 0);
    }

    private GuiaTuristico obtenerGuiaTuristicoRecursivo(List<GuiaTuristico> guias, String identificacion, int index) {
        if (index < guias.size()) {
            GuiaTuristico guia = guias.get(index);
            if (guia.getIdentificacion().equals(identificacion)) {
                return guia;
            }
            return obtenerGuiaTuristicoRecursivo(guias, identificacion, index + 1);
        } else {
            return null;
        }
    }
	
    public Usuario obtenerUsuario(String username) {
        return obtenerUsuarioRecursivo(listaUsuarios, username, 0);
    }

    private Usuario obtenerUsuarioRecursivo(List<Usuario> usuarios, String username, int index) {
        if (index < usuarios.size()) {
            Usuario usuario = usuarios.get(index);
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
            return obtenerUsuarioRecursivo(usuarios, username, index + 1);
        } else {
            return null;
        }
    }
	
    public Reserva obtenerReserva(String id) {
        return obtenerReservaRecursivo(listaReservas, id, 0);
    }

    private Reserva obtenerReservaRecursivo(List<Reserva> reservas, String id, int index) {
        if (index < reservas.size()) {
            Reserva reserva = reservas.get(index);
            if (reserva.getId().equals(id)) {
                return reserva;
            }
            return obtenerReservaRecursivo(reservas, id, index + 1);
        } else {
            return null;
        }
    }	
	
	@Override
	public String crearCliente(String data) { 				
		String mensaje = "";
		try {
			usuario.crearCliente(data);
			mensaje = "Cliente creado con exito.";
		} catch (ClienteNoCreado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String actualizarCliente(Cliente clienteActual, String dataClienteNuevo) {				
		String mensaje = "";
		try {
			usuario.actualizarCliente(clienteActual, dataClienteNuevo);
			mensaje = "Cliente actualizado con exito.";
		} catch (ClienteNoActualizado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}

	@Override
	public String eliminarClienteUsuario(String identificacion) {
		String mensaje = "";
		try {
			usuario.eliminarClienteUsuario(identificacion);
			mensaje = "Cliente y usuario eliminados con exito.";
		} catch (ClienteNoEliminado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String crearUsuario(String data) { 		
		String mensaje = "";
		try {
			usuario.crearUsuario(data);
			mensaje = "Usuario creado con exito.";
		} catch (UsuarioNoCreado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
    public String actualizarUsuario(Usuario usuarioActual, String dataUsuarioNuevo) {		
		String mensaje = "";
		try {
			usuario.actualizarUsuario(usuarioActual, dataUsuarioNuevo);
			mensaje = "Cliente actualizado con exito.";
		} catch (UsuarioNoActualizado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
    @Override
	public String crearReserva(String data){
    	String mensaje = "";
		try {
	    	usuario.crearReserva(data);
			mensaje = "Reserva creada con exito.\nTu reserva se encuentra pendiente.\nDirigete al apartado de \"Mis reservas\" para confirmarla.";
		} catch (ReservaNoCreada e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
    }
    
    @Override
    public String actualizarReserva(Reserva reservaActual, String dataReservaNuevo) {		
		String mensaje = "";
		try {
			usuario.actualizarReserva(reservaActual, dataReservaNuevo);
			mensaje = "Reserva actualizada con exito.";
		} catch (ReservaNoActualizada e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String eliminarReserva(String id)  {		
		String mensaje = "";
		try {
			usuario.eliminarReserva(id);
			mensaje = "Reserva eliminada con exito.";
		} catch (ReservaNoEliminada e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public List<Reserva> listarReservasPasadasCliente(Cliente cliente) {
		return usuario.listarReservasPasadasCliente(cliente);
	}
	
	@Override
	public List<Reserva> listarReservasFuturasCliente(Cliente cliente) {
		return usuario.listarReservasFuturasCliente(cliente);
	}	
    
    @Override
    public String calificarDestino(Reserva reserva, Destino destino, int calificacion, String comentario) {    	
    	String mensaje = "";
		try {
	    	usuario.calificarDestino(reserva, destino, calificacion, comentario);
			mensaje = "Destino calificado con exito.";
		} catch (CalificacionDestinoNoActualizada e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
    }
    
    @Override
    public String calificarGuia(Reserva reserva, GuiaTuristico guia, int calificacion, String comentario) {   	
    	String mensaje = "";
		try {
	    	usuario.calificarGuia(reserva, guia, calificacion, comentario);
			mensaje = "Guía calificado con exito.";
		} catch (CalificacionGuiaNoActualizada e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
    }
    
    @Override
    public List<Destino> recomendarDestinos(Cliente cliente) {
    	return usuario.recomendarDestinos(cliente);
    }
	
	
	@Override
	public String crearDestino(String data) {		
		String mensaje = "";
		try {
			administrador.crearDestino(data);
			mensaje = "Destino creado con exito.";
		} catch (DestinoNoCreado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String actualizarDestino(Destino destinoActual, String dataDestinoNuevo) {				
		String mensaje = "";
		try {
			administrador.actualizarDestino(destinoActual, dataDestinoNuevo);
			mensaje = "Destino actualizado con exito.";
		} catch (DestinoNoActualizado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}

	@Override
	public String eliminarDestino(String nombre) {	
		String mensaje = "";
		try {
			administrador.eliminarDestino(nombre);
			mensaje = "Destino eliminado con exito.";
		} catch (DestinoNoEliminado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String crearPaqueteTuristico(String data) {		
		String mensaje = "";
		try {
			administrador.crearPaqueteTuristico(data);
			mensaje = "Paquete turístico creado con exito.";
		} catch (PaqueteNoCreado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String actualizarPaqueteTuristico(PaqueteTuristico paqueteActual, String dataPaqueteNuevo) {				
		String mensaje = "";
		try {
			administrador.actualizarPaqueteTuristico(paqueteActual, dataPaqueteNuevo);
			mensaje = "Paquete Turístico actualizado con exito.";
		} catch (PaqueteNoActualizado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}

	@Override
	public String eliminarPaqueteTuristico(String nombre) {
		String mensaje = "";
		try {
			administrador.eliminarPaqueteTuristico(nombre);
			mensaje = "Paquete turístico eliminado con exito.";
		} catch (PaqueteNoEliminado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String crearGuiaTuristico(String data) {
		String mensaje = "";
		try {
			administrador.crearGuiaTuristico(data);
			mensaje = "Guía turístico creado con exito.";
		} catch (GuiaNoCreado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}
	
	@Override
	public String actualizarGuiaTuristico(GuiaTuristico guiaActual, String dataGuiaNuevo) {				
		String mensaje = "";
		try {
			administrador.actualizarGuiaTuristico(guiaActual, dataGuiaNuevo);
			mensaje = "Guía Turístico actualizado con exito.";
		} catch (PaqueteNoActualizado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}

	@Override
	public String eliminarGuiaTuristico(String identificacion) {
		String mensaje = "";
		try {
			administrador.eliminarGuiaTuristico(identificacion);
			mensaje = "Guía turístico eliminado con exito.";
		} catch (GuiaNoEliminado e) {
			mensaje = e.getMessage();
		}		
		return mensaje;
	}

	@Override
	public List<Map.Entry<String, Integer>> obtenerDestinosMasReservados(int limit) {
		return administrador.obtenerDestinosMasReservados(limit);
	}
	
	@Override
	public List<Map.Entry<String, Integer>> obtenerPaquetesMasReservados(int limit) {
		return administrador.obtenerPaquetesMasReservados(limit);
	}
	
	@Override
	public List<Map.Entry<String, Integer>> obtenerGuiasMejorPuntuados() {
		return administrador.obtenerGuiasMejorPuntuados();
	}
	
	@Override
	public List<Map.Entry<String, Integer>> obtenerDestinosMasBuscados() {
		return administrador.obtenerDestinosMasBuscados();
	}	

	@Override
	public void generarInforme(String nombreArchivo) { 
		administrador.generarInforme(nombreArchivo);
	}
}
