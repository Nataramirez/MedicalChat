package MedicalChat.app.modelo;

import MedicalChat.app.enums.TipoEspecialidad;
import lombok.*;


@Getter
@Setter
@ToString
public class Medico extends Persona{
    private TipoEspecialidad especialidad;

    public Medico(String nombre, String cedula, String telefono, String correo, String contrasena, TipoEspecialidad especialidad) {
        super(nombre, cedula, telefono, correo, contrasena);
        this.especialidad = especialidad;
    }
}
