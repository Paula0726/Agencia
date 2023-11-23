package co.edu.uniquindio.agencia.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuiaTuristico implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String identificacion;
    private String nombreCompleto;
    private List<String> lenguajesComunicacion;
    private String experienciaAcumulada;    
    private int precio;
    private List<Integer> estrellas;
    private List<String> comentarios;
    
    // Método para convertir la información del cliente en una cadena de texto
    public String convertirEnCadena() {
        StringBuilder cadena = new StringBuilder();

        // Concatenación de los atributos del cliente separados por "@@"
        cadena.append(id)
            .append("@@").append(identificacion)
            .append("@@").append(nombreCompleto)
            .append("@@").append(String.join(",", lenguajesComunicacion))
            .append("@@").append(experienciaAcumulada)
        	.append("@@").append(precio)
            .append("@@").append(estrellas.stream().map(Object::toString).collect(Collectors.joining(",")))
        	.append("@@").append(String.join(",", comentarios));


        return cadena.toString();
    }
}