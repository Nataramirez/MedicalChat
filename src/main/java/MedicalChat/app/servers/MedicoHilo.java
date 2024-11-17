package MedicalChat.app.servers;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

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
                    String mensaje = JOptionPane.showInputDialog(null,
                            "Hay " + ChatServer.waitingPatients.size() + " pacientes en espera. Escribe " +
                                    "'ACEPTAR' para tomar el primer paciente en la lista.");

                    if ("ACEPTAR".equalsIgnoreCase(mensaje)) {
                        PacienteHilo patient = ChatServer.assignPatientToDoctor(this);
                        if (patient != null) {
                            out.writeUTF("Chat iniciado con un paciente.");
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
            String message = in.readUTF();
            while (true) {
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("El paciente ha salido del chat.");
                    break;
                }
                if (currentPatient != null) {
                    currentPatient.sendMessage("Doctor: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
}
