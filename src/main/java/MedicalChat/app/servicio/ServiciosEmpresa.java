package MedicalChat.app.servicio;


import MedicalChat.app.enums.TipoRegistro;
import MedicalChat.app.modelo.Medico;
import MedicalChat.app.modelo.Paciente;

public interface ServiciosEmpresa {
    Paciente agregarPaciente(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception;
    Paciente obtenerPaciente(String cedula) throws Exception;
    Medico agregarMedico(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception;
    Medico obtenerMedico(String cedula) throws Exception;
    boolean iniciarSesion(String cedula, String password, TipoRegistro tipo) throws Exception;
}
