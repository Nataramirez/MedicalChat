package MedicalChat.app.modelo;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class Persona {
    private String nombre;
    private String cedula;
    private String telefono;
    private String correo;
    private String contrasena;
}
