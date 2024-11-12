package MedicalChat.app.modelo;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Persona {
    private String nombre;
    private String cedula;
    private String telefono;
    private String correo;
    private String contrasena;
}
