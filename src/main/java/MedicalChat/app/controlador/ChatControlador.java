package MedicalChat.app.controlador;

import MedicalChat.app.servers.PacienteHilo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatControlador implements Initializable {
    private final PrincipalControlador principal;
    @FXML
    public Button botonEnviar;
    @FXML
    public TextField mensaje;
    @FXML
    public ScrollPane contenido;
    @FXML
    public VBox cajaMensajes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ChatControlador() {
        principal = PrincipalControlador.getInstancia();
        try {
            Socket socket = new Socket("localhost", 8080);
            PacienteHilo pacienteHilo = new PacienteHilo(socket, principal.getSesion().getPaciente());
            pacienteHilo.start();
            System.out.println("SE CONECTA AL SERVIDOR" + pacienteHilo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
