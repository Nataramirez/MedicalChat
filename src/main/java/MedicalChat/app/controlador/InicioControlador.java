package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.enums.TipoAccionesInicio;
import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.enums.TipoRegistro;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioControlador implements Initializable, Observable {
    private final PrincipalControlador principal;
    private Observable observable;
    @FXML
    public ComboBox paciente;
    @FXML
    public ComboBox medico;

    public InicioControlador() {
        principal = PrincipalControlador.getInstancia();
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paciente.setItems(FXCollections.observableArrayList(TipoAccionesInicio.values()));
        medico.setItems(FXCollections.observableArrayList(TipoAccionesInicio.values()));
    }

    public void obtenerAccionPaciente() {
        if(paciente.getValue().equals(TipoAccionesInicio.Ingresar)){
            principal.getSesion().setTipoRegistro(TipoRegistro.PACIENTE);
            principal.navegarVentana(TipoPantalla.INICIO_SESION.getRuta(), TipoPantalla.INICIO_SESION.getNombre());
        }

        if (paciente.getValue().equals(TipoAccionesInicio.Registrarse)) {
            principal.getSesion().setTipoRegistro(TipoRegistro.PACIENTE);
            FXMLLoader loader = principal.navegarVentana(TipoPantalla.REGISTRO.getRuta(), TipoPantalla.REGISTRO.getNombre());
            RegistroControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
        }
    }


    public void obtenerAccionMedico(ActionEvent actionEvent) {
        if(medico.getValue().equals(TipoAccionesInicio.Ingresar)){
            principal.getSesion().setTipoRegistro(TipoRegistro.MEDICO);
            principal.navegarVentana(TipoPantalla.INICIO_SESION.getRuta(), TipoPantalla.INICIO_SESION.getNombre());
        }

        if (medico.getValue().equals(TipoAccionesInicio.Registrarse)) {
            principal.getSesion().setTipoRegistro(TipoRegistro.MEDICO);
            FXMLLoader loader = principal.navegarVentana(TipoPantalla.REGISTRO.getRuta(), TipoPantalla.REGISTRO.getNombre());
            RegistroControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
        }
    }

    @Override
    public void notificar() {}
}
