package MedicalChat.app.servers.clients;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class PatientClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        boolean bandera = true;

        try (Socket socketServer = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream pacienteOutput = new DataOutputStream(socketServer.getOutputStream());
             DataInputStream pacienteInput = new DataInputStream(socketServer.getInputStream())) {

            pacienteOutput.writeUTF("PACIENTE");

            String respuestaServidor = pacienteInput.readUTF();
            JOptionPane.showMessageDialog(null, respuestaServidor);

                while (bandera) {
                    String mensaje = JOptionPane.showInputDialog(null, "Escribe tu mensaje (escribe 'exit' para salir):");

                    if (mensaje.equalsIgnoreCase("exit")) {
                        pacienteOutput.writeUTF("exit");
                        bandera = false;
                        break;
                    }

                    pacienteOutput.writeUTF(mensaje);
                    respuestaServidor = pacienteInput.readUTF();
                    JOptionPane.showMessageDialog(null, "El doctor dice: " + respuestaServidor);
                }


        } catch (IOException e) {
            System.err.println("Error en la conexión con el servidor: " + e.getMessage());
        }
    }
}
