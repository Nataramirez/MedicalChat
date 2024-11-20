package MedicalChat.app.servers;

import MedicalChat.app.modelo.Paciente;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 8080;
    static List<PacienteHilo> waitingPatients = new ArrayList<>();
    private static List<MedicoHilo> connectedDoctors = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de chat iniciado en el puerto " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nueva conexión: " + socket);

                DataInputStream serverInput = new DataInputStream(socket.getInputStream());
                String clientType = serverInput.readUTF();

                if (clientType.equals("DOCTOR")) {
                    MedicoHilo doctor = new MedicoHilo(socket);
                    connectedDoctors.add(doctor);
                    doctor.start();
                } else if (clientType.equals("PACIENTE")) {
                    PacienteHilo patient = new PacienteHilo(socket, new Paciente("a", "2", "a", "a", "a"));
                    waitingPatients.add(patient);
                    patient.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static PacienteHilo assignPatientToDoctor(MedicoHilo doctor) {
        if (!waitingPatients.isEmpty()) {
            return waitingPatients.remove(0); // Devuelve el primer paciente en espera
        }
        return null;
    }

    public synchronized static List<String> getWaitingPatients() {
        List<String> patientNames = new ArrayList<>();
        for (PacienteHilo patientThread : waitingPatients) {
            Paciente paciente = patientThread.getPaciente(); // Asegúrate de que `getPaciente()` retorne el objeto correcto.
            if (paciente != null) {
                patientNames.add(paciente.getNombre()); // Llama a `getNombre()` directamente desde `Paciente`.
            }
        }
        return patientNames;
    }


    public synchronized static void iniciarChatEntre(MedicoHilo doctor, PacienteHilo paciente) throws IOException {
        doctor.setPaciente(paciente);
        paciente.setDoctor(doctor);

        // Opcional: notifica a ambos que el chat puede comenzar.
        doctor.sendMessage("Chat iniciado con " + paciente.getPaciente().getNombre());
        paciente.sendMessage("Chat iniciado con el doctor.");
    }

    public synchronized static void notificarMedicos() {
        for (MedicoHilo medico : connectedDoctors) {
            medico.actualizarListaPacientes(getWaitingPatients());
        }
    }


}
