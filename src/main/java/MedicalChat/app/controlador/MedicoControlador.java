package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.enums.TipoPantalla;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MedicoControlador implements Observable, Initializable {
    private final PrincipalControlador principal;
    private Observable observable;
    @FXML
    public Label nombreUsuario;

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
    }

    public void cerrarSesion() {
        principal.getSesion().cerrarSesion();
        principal.cerrarVentana(nombreUsuario);
        principal.navegarVentana(TipoPantalla.INICIO.getRuta(), TipoPantalla.INICIO.getNombre());
    }
}
