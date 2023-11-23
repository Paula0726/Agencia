package co.edu.uniquindio.agencia.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteTuristico implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String duracion;
    private List<Destino> destinos;
    private List<String> serviciosAdicionales;
    private int precio;
    private int cupoMaximo;
    private List<LocalDate> fechasDisponibles;
    
    // Método para validar si la cantidad de personas no supera el cupo máximo
    public boolean validarCupoMaximo(int cantidadPersonas) {
        return cantidadPersonas <= cupoMaximo;
    }

    // Método para validar si una fecha dada está disponible en el paquete turístico
    public boolean validarFechasDisponibles(LocalDate fecha) {
        return fechasDisponibles.contains(fecha);
    }
    
    // Método para convertir el paquete turístico en una cadena de texto
    public String convertirEnCadena() {
        StringBuilder cadena = new StringBuilder();

        cadena.append(id)
                .append("@@").append(nombre)
                .append("@@").append(duracion)
                .append("@@").append(destinosToString())
                .append("@@").append(serviciosAdicionalesToString())
                .append("@@").append(precio)
                .append("@@").append(cupoMaximo)
                .append("@@").append(fechasToString());

        return cadena.toString();
    }
    
    // Método privado para convertir la lista de servicios adicionales en una cadena
    private String serviciosAdicionalesToString() {
        return String.join(",", serviciosAdicionales);
    }

    // Método privado para convertir la lista de destinos en una cadena
    private String destinosToString() {
        return destinos.stream().map(Destino::getId).collect(Collectors.joining(","));
    }

    // Método privado para convertir la lista de fechas disponibles en una cadena
    private String fechasToString() {
        return fechasDisponibles.stream().map(LocalDate::toString).collect(Collectors.joining(","));
    }
}
