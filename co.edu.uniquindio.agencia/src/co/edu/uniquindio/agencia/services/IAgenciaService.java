package co.edu.uniquindio.agencia.services;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.edu.uniquindio.agencia.exceptions.CalificacionGuiaNoActualizada;
import co.edu.uniquindio.agencia.exceptions.ClienteNoCreado;
import co.edu.uniquindio.agencia.exceptions.ClienteNoEliminado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoCreado;
import co.edu.uniquindio.agencia.exceptions.DestinoNoEliminado;
import co.edu.uniquindio.agencia.exceptions.GuiaNoCreado;
import co.edu.uniquindio.agencia.exceptions.GuiaNoEliminado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoCreado;
import co.edu.uniquindio.agencia.exceptions.PaqueteNoEliminado;
import co.edu.uniquindio.agencia.exceptions.ReservaNoCreada;
import co.edu.uniquindio.agencia.exceptions.ReservaNoEliminada;
import co.edu.uniquindio.agencia.exceptions.UsuarioNoCreado;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.agencia.model.Usuario;

public interface IAgenciaService {
    // Métodos relacionados con Cliente
    String crearCliente(String data);
    String actualizarCliente(Cliente clienteActual, String dataClienteNuevo);
    String eliminarClienteUsuario(String id);

    // Métodos relacionados con Destino
    String crearDestino(String data);
    String actualizarDestino(Destino destinoActual, String dataDestinoNuevo);
    String eliminarDestino(String id);

    // Métodos relacionados con PaqueteTuristico
    String crearPaqueteTuristico(String data);
    String actualizarPaqueteTuristico(PaqueteTuristico paqueteActual, String dataPaqueteNuevo);
    String eliminarPaqueteTuristico(String id);

    // Métodos relacionados con GuiaTuristico
    String crearGuiaTuristico(String data);
    String actualizarGuiaTuristico(GuiaTuristico guiaActual, String dataGuiaNuevo);
    String eliminarGuiaTuristico(String id);

    // Métodos relacionados con Reserva
    String crearReserva(String data);
    String actualizarReserva(Reserva reservaActual, String dataReservaNuevo);
    String eliminarReserva(String id);
    List<Reserva> listarReservasPasadasCliente(Cliente cliente);
    List<Reserva> listarReservasFuturasCliente(Cliente cliente);

    // Métodos adicionales
    List<Entry<String, Integer>> obtenerDestinosMasReservados(int limit);
    List<Entry<String, Integer>> obtenerPaquetesMasReservados(int limit);
    List<Entry<String, Integer>> obtenerGuiasMejorPuntuados();
    List<Entry<String, Integer>> obtenerDestinosMasBuscados();
    void generarInforme(String nombreArchivo);
    
    // Métodos relacionados con Usuario
    String crearUsuario(String data);
    String actualizarUsuario(Usuario usuarioActual, String dataUsuarioNuevo);

    // Métodos relacionados con Calificación y Recomendaciones
    String calificarDestino(Reserva reserva, Destino destino, int calificacion, String comentario);
    String calificarGuia(Reserva reserva, GuiaTuristico guia, int calificacion, String comentario);
    List<Destino> recomendarDestinos(Cliente cliente);    
}
