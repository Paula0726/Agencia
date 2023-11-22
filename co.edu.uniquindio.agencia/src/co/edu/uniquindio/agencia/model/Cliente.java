package co.edu.uniquindio.agencia.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String identificacion;
    private String nombreCompleto;
    private String correoElectronico;
    private String numeroTelefono;
    private String direccionResidencia;
    private Usuario usuario;
    private List<String> destinosBuscados;
    
    public String convertirEnCadena() {
        StringBuilder cadena = new StringBuilder();

        cadena.append(id)
                .append("@@").append(identificacion)
                .append("@@").append(nombreCompleto)
                .append("@@").append(correoElectronico)
                .append("@@").append(numeroTelefono)
                .append("@@").append(direccionResidencia)
                .append("@@").append(usuario.getId())
                .append("@@").append(String.join(",", destinosBuscados));

        return cadena.toString();
    }
}
