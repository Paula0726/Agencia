package co.edu.uniquindio.agencia.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String contrasena;
    
    public String convertirEnCadena() {
        StringBuilder cadena = new StringBuilder();
        cadena.append(id)
                .append("@@").append(username)
                .append("@@").append(contrasena);
        return cadena.toString();
    }
}
