package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.servers.ChatServer;
import MedicalChat.app.servers.PacienteHilo;
import MedicalChat.app.servers.clients.DoctorClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MedicoControlador implements Observable, Initializable {
    private final PrincipalControlador principal;
    private Observable observable;
    @FXML
    public Label nombreUsuario;
    @FXML
    private ListView<String> listaPacientes; // Asocia este componente en tu archivo FXML.

    private ObjectInputStream entrada; // Cambiamos a ObjectInputStream para leer objetos.

    public MedicoControlador() {
        principal = PrincipalControlador.getInstancia();
        System.out.println(principal.getSesion().getMedico().getNombre());
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void notificar() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nombreUsuario.setText("Hola " + principal.getSesion().getMedico().getNombre());
        // Aquí podrías inicializar la conexión con el servidor y el flujo de entrada.
        try {
            Socket socket = new Socket("localhost", 12345); // Dirección y puerto del servidor.
            entrada = new ObjectInputStream(socket.getInputStream()); // Usamos ObjectInputStream.
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    @FXML
    public void cargarPacientes() {
        try {
            List<String> pacientes = ChatServer.getWaitingPatients(); // Simula un fetch desde el servidor.
            listaPacientes.getItems().setAll(pacientes);
        } catch (Exception e) {
            System.err.println("Error cargando pacientes: " + e.getMessage());
        }
    }

    @FXML
    public void aceptarChat() {
        String pacienteSeleccionado = listaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            try {
                // Asegúrate de tener un método para buscar y asignar el paciente.
                PacienteHilo paciente = ChatServer.assignPatientToDoctor(null); // Pasa el hilo del médico aquí.
                if (paciente != null) {
                    SwingUtilities.invokeLater(DoctorClient::new); // Abre la ventana de chat.
                }
            } catch (Exception e) {
                System.err.println("Error aceptando paciente: " + e.getMessage());
            }
        }
    }

    @FXML
    public void initialize() {
        new Thread(this::escucharActualizaciones).start();
    }

    private void escucharActualizaciones() {
        while (true) {
            try {
                if (entrada != null) {
                    String comando = entrada.readUTF();
                    if (comando.equals("UPDATE_PATIENTS")) {
                        // Leemos la lista de pacientes como un objeto.
                        List<String> pacientes = (List<String>) entrada.readObject();
                        Platform.runLater(() -> listaPacientes.getItems().setAll(pacientes));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error en actualizaciones: " + e.getMessage());
                break;
            }
        }
    }

    public void cerrarSesion() {
        principal.getSesion().cerrarSesion();
        principal.cerrarVentana(nombreUsuario);
        principal.navegarVentana(TipoPantalla.INICIO.getRuta(), TipoPantalla.INICIO.getNombre());
    }
}


