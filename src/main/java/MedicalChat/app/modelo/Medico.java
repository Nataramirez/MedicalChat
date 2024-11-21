package MedicalChat.app.modelo;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class Medico extends Persona implements Serializable {
    public Medico(String nombre, String cedula, String telefono, String correo, String contrasena) {
        super(nombre, cedula, telefono, correo, contrasena);
    }
}
