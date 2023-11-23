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
public class Destino implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String ciudad;
    private String descripcion;
    private List<String> imagenesRepresentativas;
    private String clima;
    private int cantidadBusquedas;
    private List<Integer> estrellas;
    private List<String> comentarios;
    
    // Método para convertir la información del cliente en una cadena de texto
    public String convertirEnCadena() {
        StringBuilder cadena = new StringBuilder();

        // Concatenación de los atributos del cliente separados por "@@"
        cadena.append(id)
            .append("@@").append(nombre)
            .append("@@").append(ciudad)
            .append("@@").append(descripcion)
            .append("@@").append(String.join(",", imagenesRepresentativas))
            .append("@@").append(clima)
            .append("@@").append(cantidadBusquedas)
            .append("@@").append(estrellas.stream().map(Object::toString).collect(Collectors.joining(",")))
            .append("@@").append(String.join(",", comentarios));

        return cadena.toString();
    }
}