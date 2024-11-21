package MedicalChat.app.modelo;

import MedicalChat.app.enums.TipoRegistro;
import MedicalChat.app.servicio.ServiciosEmpresa;
import MedicalChat.app.utils.Persistencia;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MedicalChat implements ServiciosEmpresa {
    private ArrayList<Paciente> pacientes  = new ArrayList<>();;
    private ArrayList<Medico> medicos = new ArrayList<>();
    private ArrayList<HistoriaClinica> historiasClinicas  = new ArrayList<>();;
    private Sesion sesion;
    private Persistencia persistencia = new Persistencia();

    public MedicalChat() {
        try {
            sesion = Sesion.getInstancia();
            cargarDatos();
        }catch (Exception e){
            System.out.println(e);
        }
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
            HistoriaClinica historiaClinica = crearHistoriaClinica(paciente);
            paciente.setHistoriaClinica(historiaClinica);
            guardarDatos();
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
            throw new Exception("No se puede buscar el paciente");
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
            guardarDatos();
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

    @Override
    public boolean iniciarSesion(String cedula, String password, TipoRegistro tipo) throws Exception {
        Object usuario = tipo.equals(TipoRegistro.MEDICO) ? obtenerMedico(cedula) : obtenerPaciente(cedula);

        if (usuario == null || !esContrasenaValida(usuario, password)) {
            return false;
        }

        if (tipo.equals(TipoRegistro.MEDICO)) {
            sesion.setMedico((Medico) usuario);
        } else {
            sesion.setPaciente((Paciente) usuario);
        }

        return true;
    }

    @Override
    public HistoriaClinica crearHistoriaClinica(Paciente paciente) throws Exception {
        HistoriaClinica historiaClinica;
        try {
            historiaClinica = HistoriaClinica.builder()
                    .consultas(new ArrayList<>())
                    .build();
            guardarDatos();            return historiaClinica;
        }catch (Exception e){
            throw new Exception("No se puede crear un historia de clinica");
        }

    }

    @Override
    public Paciente agregarConsulta(String cedula, RegistroConsultas consulta) throws Exception {
        Paciente paciente = obtenerPaciente(cedula);
        for (Paciente pacienteGuardado: pacientes){
            if (pacienteGuardado.getCedula().equals(paciente.getCedula())) {
                pacienteGuardado.getHistoriaClinica().getConsultas().add(consulta);
            }
        }
        guardarDatos();
        return paciente;
    }

    private boolean esContrasenaValida(Object usuario, String password) {
        if (usuario instanceof Medico) {
            return ((Medico) usuario).getContrasena().equals(password);
        } else if (usuario instanceof Paciente) {
            return ((Paciente) usuario).getContrasena().equals(password);
        }
        return false;
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

    public void cargarDatos() {
        try {
            ArrayList<Paciente> pacientesCargados = (ArrayList<Paciente>) persistencia.deserializarObjetoXML("pacientes.xml");
            ArrayList<Medico> medicosCargados = (ArrayList<Medico>) persistencia.deserializarObjetoXML("medicos.xml");
            ArrayList<HistoriaClinica> historiasCargadas = (ArrayList<HistoriaClinica>) persistencia.deserializarObjetoXML("historias.xml");

            if (pacientesCargados != null) {
                pacientes.addAll(pacientesCargados);
            }

            if (medicosCargados != null) {
                medicos.addAll(medicosCargados);
            }

            if (historiasCargadas != null) {
                historiasClinicas.addAll(historiasCargadas);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public void guardarDatos() throws Exception {
        try {
            persistencia.serializarObjeto("pacientes.xml",pacientes);
            persistencia.serializarObjeto("historias.xml",historiasClinicas);
            persistencia.serializarObjeto("medicos.xml",medicos);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }
}
