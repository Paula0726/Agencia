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
    
    public boolean validarCupoMaximo(int cantidadPersonas) {
        return cantidadPersonas <= cupoMaximo;
    }

    public boolean validarFechasDisponibles(LocalDate fecha) {
        return fechasDisponibles.contains(fecha);
    }
    
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
    
    private String serviciosAdicionalesToString() {
        return String.join(",", serviciosAdicionales);
    }

    private String destinosToString() {
        return destinos.stream().map(Destino::getId).collect(Collectors.joining(","));
    }

    private String fechasToString() {
        return fechasDisponibles.stream().map(LocalDate::toString).collect(Collectors.joining(","));
    }
}
