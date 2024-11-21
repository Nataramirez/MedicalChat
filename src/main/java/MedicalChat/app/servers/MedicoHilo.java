package MedicalChat.app.servers;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

@Getter
@Setter
public class MedicoHilo extends Thread {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private PacienteHilo currentPatient;

    public MedicoHilo(Socket socket) {
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println("Bienvenido, Doctor. Esperando por pacientes...");
            while (true) {
                if (!ChatServer.waitingPatients.isEmpty()) {
                    System.out.println(ChatServer.getWaitingPatients());
                    String mensaje = JOptionPane.showInputDialog(null,
                            "Hay " + ChatServer.waitingPatients.size() + " pacientes en espera. Escribe " +
                                    "'ACEPTAR' para tomar el primer paciente en la lista.");

                    if ("ACEPTAR".equalsIgnoreCase(mensaje)) {
                        PacienteHilo patient = ChatServer.assignPatientToDoctor(this);
                        if (patient != null) {
                            out.writeUTF("Chat iniciado con un paciente." + patient.getCedula());
                            currentPatient = patient;
                            patient.setDoctor(this);
                            patient.startChat();
                            chatWithPatient();
                        } else {
                            out.writeUTF("No hay pacientes en espera.");
                        }
                    }
                } else {
                    Thread.sleep(2000); // Espera antes de volver a comprobar la lista de pacientes
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chatWithPatient() {
        try {
            String message;
            while (true) {
                message = in.readUTF();
                    System.out.println("Mensaje recibido del doctor: " + message);
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("El doctor ha salido del chat.");
                        break;
                    }
                    System.out.println("Doctor dice: " + message);
                    if (currentPatient != null) {
                        currentPatient.sendMessage(message);
                    }
            }

        } catch (IOException e) {
            System.err.println("Error en la comunicación con el doctor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Cerrando la conexión del doctor...");
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del paciente: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
}
