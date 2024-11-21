package MedicalChat.app.servers;

import MedicalChat.app.modelo.Paciente;
import lombok.Getter;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 8080;
    @Getter
    static List<PacienteHilo> waitingPatients = new ArrayList<>();
    @Getter
    private static List<MedicoHilo> connectedDoctors = new ArrayList<>();


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de chat iniciado en el puerto " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nueva conexión: " + socket);

                DataInputStream serverInput = new DataInputStream(socket.getInputStream());
                String clientType = serverInput.readUTF();


                if (clientType.contains("DOCTOR")) {
                    MedicoHilo doctor = new MedicoHilo(socket);
                    connectedDoctors.add(doctor);
                    doctor.start();
                } else if (clientType.contains("PACIENTE")) {
                    System.out.println(clientType);
                    PacienteHilo patient = new PacienteHilo(socket, clientType.split(",")[1]);
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

}
