package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.servers.ChatServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MedicoControlador implements Observable, Initializable {
    private final PrincipalControlador principal;
    private Observable observable;

    @FXML
    private Label nombreUsuario;
    @FXML
    private TextArea mensajesChat;
    @FXML
    private TextField mensaje;
    @FXML
    public Button botonEnviar;

    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private boolean conectado = true;

    public MedicoControlador() {
        principal = PrincipalControlador.getInstancia();
        System.out.println(principal.getSesion().getMedico().getNombre());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura el nombre del usuario en la interfaz
        nombreUsuario.setText("Hola " + principal.getSesion().getMedico().getNombre());
        new Thread(this::recibirMensajesServidor).start();
    }

    @FXML
    public void enviarMensaje(javafx.event.ActionEvent actionEvent) {
        String mensajeEnviar = mensaje.getText().trim();
        if (!mensajeEnviar.isEmpty() && conectado) {
            try {
                salida.writeUTF(mensajeEnviar);
                mensajesChat.appendText("Tú: " + mensajeEnviar + "\n");
                mensaje.setText("");
            } catch (IOException e) {
                mensajesChat.appendText("Error al enviar el mensaje: " + e.getMessage() + "\n");
                conectado = false;
            }
        }
    }

    private void recibirMensajesServidor() {
        try {
            while (conectado) {
                String mensajeRecibido = entrada.readUTF();
                mensajesChat.appendText("Paciente: " + mensajeRecibido + "\n");
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

    @FXML
    public void cerrarSesion() {
        principal.getSesion().cerrarSesion();
        principal.cerrarVentana(nombreUsuario);
        principal.navegarVentana(TipoPantalla.INICIO.getRuta(), TipoPantalla.INICIO.getNombre());
        cerrarConexion();
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void notificar() {
        // Implementación del método notificar si es necesario
    }

    public void inicializarConexion(javafx.event.ActionEvent actionEvent) {
        try {
            socket = new Socket("localhost", 8080);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());

            // Identificación del cliente como "DOCTOR"
            salida.writeUTF("DOCTOR");
            System.out.println(ChatServer.getWaitingPatients());
            String bienvenida = entrada.readUTF();
            mensajesChat.appendText("Servidor: " + bienvenida + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            conectado = false;
        }
    }
}
