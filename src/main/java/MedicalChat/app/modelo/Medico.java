package MedicalChat.app.modelo;

import lombok.*;


@Getter
@Setter
@ToString
public class Medico extends Persona{
    public Medico(String nombre, String cedula, String telefono, String correo, String contrasena) {
        super(nombre, cedula, telefono, correo, contrasena);
    }
}
