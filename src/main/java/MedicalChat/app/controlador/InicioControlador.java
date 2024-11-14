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
    @FXML
    public ComboBox paciente;
    @FXML
    public ComboBox medico;

    public InicioControlador() {
        principal = PrincipalControlador.getInstancia();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paciente.setItems(FXCollections.observableArrayList(TipoAccionesInicio.values()));
        medico.setItems(FXCollections.observableArrayList(TipoAccionesInicio.values()));
    }

    public void obtenerAccionPaciente() {
        System.out.println(paciente.getValue());
        if(paciente.getValue().equals(TipoAccionesInicio.Ingresar)){
            System.out.println("iniciar sesion Paciente");
        }

        if (paciente.getValue().equals(TipoAccionesInicio.Registrarse)) {
            principal.getSesion().setTipoRegistro(TipoRegistro.PACIENTE);
            FXMLLoader loader = principal.navegarVentana(TipoPantalla.REGISTRO.getRuta(), TipoPantalla.REGISTRO.getNombre());
            RegistroControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
        }
    }


    public void obtenerAccionMedico(ActionEvent actionEvent) {
        System.out.println(medico.getValue());
        if(medico.getValue().equals(TipoAccionesInicio.Ingresar)){
            System.out.println("iniciar sesion médico");
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