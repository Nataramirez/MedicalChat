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
}
