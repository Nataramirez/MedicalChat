package MedicalChat.app.controlador;

import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.enums.TipoRegistro;
import MedicalChat.app.modelo.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class InicioSesionControlador {
    private final PrincipalControlador principal;
    @FXML
    public TextField cedula;
    @FXML
    public PasswordField contraseña;

    public InicioSesionControlador() {
        principal = PrincipalControlador.getInstancia();
    }

    @FXML
    public void ingresar(ActionEvent event) throws Exception {
        if (principal.iniciarSesion(cedula.getText(), contraseña.getText(), principal.getSesion().getTipoRegistro())){
            principal.mostrarAlerta("Inicio de sesión exitoso.", Alert.AlertType.INFORMATION);

            if (principal.getSesion() != null && principal.getSesion().getPaciente() != null) {
                principal.navegarVentana(TipoPantalla.PACIENTE.getRuta(), TipoPantalla.PACIENTE.getNombre());
            } else {
                principal.navegarVentana(TipoPantalla.MEDICO.getRuta(), TipoPantalla.MEDICO.getNombre());
            }

            principal.cerrarVentana(cedula);

        } else {
            principal.mostrarAlerta("Cédula o contraseña incorrectos. Intentelo nuevamente.", Alert.AlertType.WARNING);
        }
    }
}
