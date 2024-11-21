package MedicalChat.app.controlador;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatDoctorControlador implements Initializable {
    private PrincipalControlador principal;
    @FXML
    public Button botonEnviar;
    @FXML
    public TextField mensaje;
    @FXML
    public ScrollPane contenido;
    @FXML
    public VBox cajaMensajes;
    @FXML
    public TextArea mensajesChat;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private boolean conectado = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        principal = PrincipalControlador.getInstancia();
        inicializarConexion();
        new Thread(this::recibirMensajesServidor).start();
    }

    private void inicializarConexion() {
        try {
            socket = new Socket("localhost", 8080);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());

            // Identificación del cliente como "PACIENTE"
            salida.writeUTF("DOCTOR");
            String bienvenida = entrada.readUTF();

            mensajesChat.appendText("Servidor: " + bienvenida + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            conectado = false;
        }
    }

    @FXML
    public void enviarMensaje(javafx.event.ActionEvent actionEvent) {
        String mensajeEnviar = mensaje.getText().trim();
        if (!mensajeEnviar.isEmpty() && conectado) {
            try {
                salida.writeUTF(mensajeEnviar);
                mensajesChat.appendText("Tú: " + mensajeEnviar + "\n");
                mensaje.clear();
            } catch (IOException e) {
                mensajesChat.appendText("Error al enviar el mensaje: " + e.getMessage() + "\n");
                conectado = false;
            }
        }
    }

    private void recibirMensajesServidor() {
        try {
            while (conectado) {
                String mensaje = entrada.readUTF();
                mensajesChat.appendText("Paciente: " + mensaje + "\n");
            }
        } catch (IOException e) {
            mensajesChat.appendText("Conexión cerrada: " + e.getMessage() + "\n");
            conectado = false;
        } finally {
            cerrarConexion();
        }
    }

    private void cerrarConexion() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            mensajesChat.appendText("Error al cerrar la conexión: " + e.getMessage() + "\n");
        }
    }
}
