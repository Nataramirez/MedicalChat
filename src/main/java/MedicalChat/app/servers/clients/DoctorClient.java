package MedicalChat.app.servers.clients;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class DoctorClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        boolean bandera = true;
        try (Socket socketServer = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream medicoOutput = new DataOutputStream(socketServer.getOutputStream());
             DataInputStream medicoInput = new DataInputStream(socketServer.getInputStream())) {

            medicoOutput.writeUTF("DOCTOR");

            String respuestaServidor = medicoInput.readUTF();
            JOptionPane.showMessageDialog(null, respuestaServidor);

            if (!respuestaServidor.isBlank()) {
                while (bandera) {
                    String mensaje = JOptionPane.showInputDialog(null, "Doctor escribe tu mensaje (escribe 'exit' para salir):");

                    if (mensaje == null || mensaje.equalsIgnoreCase("exit")) {
                        medicoOutput.writeUTF("exit");
                        bandera = false;
                        break;
                    }

                    medicoOutput.writeUTF(mensaje);
                    respuestaServidor = medicoInput.readUTF();
                    System.out.println("El paciente dice d: " + respuestaServidor);
                }
            }

        } catch (IOException e) {
            System.err.println("Error en la conexión con el servidor: " + e.getMessage());
        }
    }
}
