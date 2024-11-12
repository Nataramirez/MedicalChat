package MedicalChat.app.controlador;

import MedicalChat.app.enums.TipoAccionesInicio;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Inicio implements Initializable {
    @FXML
    public ComboBox paciente;
    @FXML
    public ComboBox medico;

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
    }


    public void obtenerAccionMedico(ActionEvent actionEvent) {
        System.out.println(medico.getValue());
        if(medico.getValue().equals(TipoAccionesInicio.Ingresar)){
            System.out.println("iniciar sesion médico");
        }

    }
}
