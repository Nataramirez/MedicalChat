package MedicalChat.app.modelo;

import MedicalChat.app.servicio.ServiciosEmpresa;

import java.util.ArrayList;

public class MedicalChat implements ServiciosEmpresa {
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;

    public MedicalChat() {
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
    }

    @Override
    public Paciente agregarPaciente(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception {
        validarCampos(nombreCompleto, cedula, correoEmail, password);

        if(obtenerPaciente(cedula) != null){
            throw new Exception("Ya existe un usuario con el número de identificación ingresada.");
        }

        Paciente paciente;
        try {
            paciente = new Paciente(nombreCompleto, cedula, numeroTelefono, correoEmail, password);
            pacientes.add(paciente);
        }catch (Exception e){
            throw new Exception("No se puede crear un nuevo paciente");
        }

        return paciente;
    }

    @Override
    public Paciente obtenerPaciente(String cedula) throws Exception {
        try {
            for (Paciente paciente : pacientes) {
                if (paciente.getCedula().equals(cedula)) {
                    return paciente;
                }
            }
            return null;
        }catch (Exception e){
            throw new Exception("No se puede buscar Paciente");
        }
    }

    @Override
    public Medico agregarMedico(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception {
        validarCampos(nombreCompleto, cedula, correoEmail, password);

        if(obtenerMedico(cedula) != null){
            throw new Exception("Ya existe un usuario con el número de identificación ingresada.");
        }

        Medico medico;
        try {
            medico = new Medico(nombreCompleto, cedula, numeroTelefono, correoEmail, password);
            medicos.add(medico);
        }catch (Exception e){
            throw new Exception("No se puede crear un nuevo paciente");
        }

        return medico;
    }

    @Override
    public Medico obtenerMedico(String cedula) throws Exception {
        try {
            for (Medico medico : medicos) {
                if (medico.getCedula().equals(cedula)) {
                    return medico;
                }
            }
            return null;
        }catch (Exception e){
            throw new Exception("No se puede buscar Médico");
        }
    }

    private void validarCampos(String nombreCompleto, String cedula, String correoEmail, String password) throws Exception {
        if(cedula.isEmpty() || cedula.isBlank() ){
            throw new Exception("El número de identificación es obligatorio");
        }

        if(nombreCompleto.isEmpty() || nombreCompleto.isBlank()){
            throw new Exception("El nombre es obligatorio");
        }

        if(correoEmail.isEmpty() || correoEmail.isBlank()){
            throw new Exception("El correo electrónico es obligatorio");
        }

        if(password.isEmpty() || password.isBlank()){
            throw new Exception("La contraseña es obligatoria");
        }
    }
}
