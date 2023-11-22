package co.edu.uniquindio.agencia.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private LocalDate fechaSolicitud;
    private LocalDate fechaPlanificadaViaje;
    private Cliente cliente;
    private int cantidadPersonas;
    private PaqueteTuristico paqueteTuristico;
    private GuiaTuristico guiaTuristico;
    private EstadoReserva estadoReserva;
    
    public String convertirEnCadena() {
        StringBuilder cadena = new StringBuilder();

        cadena.append(id)
                .append("@@")
                .append(fechaSolicitud)
                .append("@@")
                .append(fechaPlanificadaViaje)
                .append("@@")
                .append(cliente.getId())
                .append("@@")
                .append(cantidadPersonas)
                .append("@@")
                .append(paqueteTuristico.getId())
                .append("@@")
                .append(guiaTuristico.getId())
                .append("@@")
                .append(estadoReserva);

        return cadena.toString();
    }
 
}