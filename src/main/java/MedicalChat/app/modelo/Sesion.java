package MedicalChat.app.modelo;

import MedicalChat.app.controlador.PrincipalControlador;
import MedicalChat.app.enums.TipoRegistro;
import lombok.*;

@Getter
@Setter
public class Sesion {
    private TipoRegistro tipoRegistro;
    private Paciente paciente;
    private Medico medico;
    public static Sesion INSTANCIA;


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

    public static Sesion getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new Sesion();
        }
        return INSTANCIA;
    }
}
