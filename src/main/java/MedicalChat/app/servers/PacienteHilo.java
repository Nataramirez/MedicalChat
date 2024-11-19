package MedicalChat.app.servers;

import MedicalChat.app.modelo.Paciente;

import java.io.*;
import java.net.Socket;

public class PacienteHilo extends Thread {
    private final Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean isWaiting = true;
    private MedicoHilo assignedDoctor;
    private final Paciente paciente;

    public PacienteHilo(Socket socket, Paciente patient) {
        this.socket = socket;
        this.paciente = patient;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error al crear los flujos de entrada/salida: " + e.getMessage());
        }
    }

    public void run() {
        try {
            out.writeUTF("Esperando a un médico para iniciar el chat...");
            System.out.println("Paciente conectado, esperando a un médico..." + paciente.getNombre());
            while (isWaiting) {
                Thread.sleep(1000);
            }

            String message;
            while (true) {
                message = in.readUTF();
                System.out.println("Mensaje recibido del paciente: " + message);
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("El paciente ha salido del chat.");
                    break;
                }
                System.out.println("Paciente dice dddd: " + message);
                if (assignedDoctor != null) {
                    assignedDoctor.sendMessage("Paciente: " + message);
                }
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en la comunicación con el paciente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Cerrando la conexión del paciente...");
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del paciente: " + e.getMessage());
            }
        }
    }


    public void startChat() {
        isWaiting = false;
        try {
            out.writeUTF("Un médico ha aceptado tu solicitud. Inicia el chat...");
        } catch (IOException e) {
            System.err.println("Error al iniciar el chat con el paciente: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje al paciente: " + e.getMessage());
        }
    }

    public void setDoctor(MedicoHilo doctor) {
        this.assignedDoctor = doctor;
    }
}
