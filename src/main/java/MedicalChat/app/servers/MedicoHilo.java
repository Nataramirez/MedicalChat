package MedicalChat.app.servers;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class MedicoHilo extends Thread {
    private Socket socket;
    private DataOutputStream out; // Deberá ser reemplazado por ObjectOutputStream
    private DataInputStream in;
    private ObjectOutputStream objectOut; // Usaremos este flujo para enviar objetos
    private PacienteHilo currentPatient;

    public MedicoHilo(Socket socket) {
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            objectOut = new ObjectOutputStream(socket.getOutputStream()); // Inicializa el ObjectOutputStream
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
                    currentPatient.sendMessage("Doctor: " + message);
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

    public void setPaciente(PacienteHilo paciente) {
        this.currentPatient = paciente;
    }

    public void actualizarListaPacientes(List<String> pacientes) {
        try {
            out.writeUTF("UPDATE_PATIENTS");
            objectOut.writeObject(pacientes); // Ahora usamos el ObjectOutputStream para enviar objetos
        } catch (IOException e) {
            System.err.println("Error actualizando lista de pacientes: " + e.getMessage());
        }
    }
}
