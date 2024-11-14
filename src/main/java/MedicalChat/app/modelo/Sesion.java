package MedicalChat.app.modelo;

import MedicalChat.app.enums.TipoRegistro;
import lombok.*;

@Getter
@Setter
public class Sesion {
    private TipoRegistro tipoRegistro;
    private Paciente paciente;
    private Medico medico;

    public Sesion(){
        tipoRegistro = null;
        paciente = null;
        medico = null;
    }

    public void cerrarSesion(){
        tipoRegistro = null;
        paciente = null;
        medico = null;
    }
}
