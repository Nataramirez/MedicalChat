package MedicalChat.app.servicio;


import MedicalChat.app.enums.TipoRegistro;
import MedicalChat.app.modelo.*;

public interface ServiciosEmpresa {
    Paciente agregarPaciente(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception;
    Paciente obtenerPaciente(String cedula) throws Exception;
    Medico agregarMedico(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception;
    Medico obtenerMedico(String cedula) throws Exception;
    boolean iniciarSesion(String cedula, String password, TipoRegistro tipo) throws Exception;
    HistoriaClinica crearHistoriaClinica(Paciente paciente) throws Exception;
    Paciente agregarConsulta(String cedula, RegistroConsultas consulta) throws Exception;
}
