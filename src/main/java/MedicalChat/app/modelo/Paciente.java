package MedicalChat.app.modelo;

import lombok.*;

@Getter
@Setter
@ToString
public class Paciente extends Persona {
    public Paciente(String nombre, String cedula, String telefono, String correo, String contrasena) {
        super(nombre, cedula, telefono, correo, contrasena);
    }
}
