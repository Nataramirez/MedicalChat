package MedicalChat.app.modelo;

import MedicalChat.app.enums.TipoGenero;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Paciente extends Persona implements Serializable {
    private HistoriaClinica historiaClinica;
    private String fechaNacimiento;
    private TipoGenero sexo;
    private String direccion;

    public Paciente(String nombre, String cedula, String telefono, String correo, String contrasena) {
        super(nombre, cedula, telefono, correo, contrasena);
    }



    @Override
    public String toString() {
        return "Paciente{" +
                "historiaClinica=" + historiaClinica +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", sexo=" + sexo +
                ", direccion='" + direccion + '\'' +
                "} " + super.toString();
    }
}
