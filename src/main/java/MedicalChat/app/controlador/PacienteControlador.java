package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.enums.TipoPantalla;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PacienteControlador implements Observable, Initializable {
    private final PrincipalControlador principal;
    private Observable observable;
    @FXML
    public Label nombreUsuario;

    public PacienteControlador() {
        principal = PrincipalControlador.getInstancia();
        System.out.println(principal.getSesion().getPaciente().getNombre());
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void notificar() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nombreUsuario.setText("Hola " + principal.getSesion().getPaciente().getNombre());
    }

    public void cerrarSesion(ActionEvent actionEvent) {
        principal.getSesion().cerrarSesion();
        principal.cerrarVentana(nombreUsuario);
        principal.navegarVentana(TipoPantalla.INICIO.getRuta(), TipoPantalla.INICIO.getNombre());
    }

    public void verHistoriaClinica() {
       principal.navegarVentana(TipoPantalla.HISTORIA_CLINICA.getRuta(), TipoPantalla.HISTORIA_CLINICA.getNombre());
    }
}
