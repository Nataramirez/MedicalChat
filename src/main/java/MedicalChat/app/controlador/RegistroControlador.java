package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.enums.TipoRegistro;
import MedicalChat.app.modelo.Medico;
import MedicalChat.app.modelo.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroControlador implements Observable {
    private final PrincipalControlador principal;
    private Observable observable;
    @FXML
    public TextField nombre;
    @FXML
    public TextField cedula;
    @FXML
    public TextField telefono;
    @FXML
    public TextField correo;
    @FXML
    public PasswordField password;

    public RegistroControlador() {
        principal = PrincipalControlador.getInstancia();
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void notificar() {}

    public void registro() {
        if(nombre.getText().isEmpty() || cedula.getText().isEmpty() || telefono.getText().isEmpty() ||
                correo.getText().isEmpty() || password.getText().isEmpty()) {
            principal.mostrarAlerta("Todos los campos son obligatorios para el registro",
                    Alert.AlertType.WARNING);
        }else {
            try {
                Integer.parseInt(cedula.getText());
            }catch (NumberFormatException e) {
                cedula.setText("");
                principal.mostrarAlerta("El campo cédula debe ser numérico", Alert.AlertType.WARNING);
            }

            try {
                if(principal.getSesion().getTipoRegistro().equals(TipoRegistro.PACIENTE)){
                    Paciente paciente = principal.obtenerPaciente(cedula.getText());
                    if(paciente == null) {
                        Paciente pacienteCreado = crearPaciente(nombre.getText(), cedula.getText(), telefono.getText(), correo.getText(), password.getText());
                        ButtonType respuesta = principal.mostrarAlertaConfirmacion("Paciente creado con exito. ¿Deseas iniciar sesión?", Alert.AlertType.CONFIRMATION);
                        validarIngreso(respuesta, pacienteCreado);
                    } else {
                        principal.mostrarAlerta("El usuario ya existe", Alert.AlertType.WARNING);
                    }
                }else {
                    Medico medico = principal.obtenerMedico(cedula.getText());
                    if(medico == null) {
                        Medico medicoCreado = crearMedico(nombre.getText(), cedula.getText(), telefono.getText(), correo.getText(), password.getText());
                        ButtonType respuesta = principal.mostrarAlertaConfirmacion("Médico creado con exito. ¿Deseas iniciar sesión?", Alert.AlertType.CONFIRMATION);
                        validarIngreso(respuesta, medicoCreado);
                    } else {
                        principal.mostrarAlerta("El usuario ya existe", Alert.AlertType.WARNING);
                    }
                }
            } catch (Exception e) {
                System.out.println("*** " + e.getMessage() + " ***");
                principal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private Paciente crearPaciente(String nombreCompleto, String cedula, String telefono, String correo, String password) throws Exception {
        try {
            return principal.agregarPaciente(nombreCompleto, cedula, telefono, correo, password);
        } catch (Exception e) {
            System.out.println("*** REGISTRO PACIENTE: El paciente no fue creado ***");
            throw new Exception("Usuario no creado");
        }
    }

    private Medico crearMedico(String nombreCompleto, String cedula, String telefono, String correo, String password) throws Exception {
        try {
            return principal.agregarMedico(nombreCompleto, cedula, telefono, correo, password);
        } catch (Exception e) {
            System.out.println("*** REGISTRO MEDICO: El médico no fue creado ***");
            throw new Exception("Usuario no creado");
        }
    }

    private void validarIngreso(ButtonType respuesta, Paciente paciente) {
        if (respuesta == ButtonType.OK) {
            principal.getSesion().setPaciente(paciente);
            principal.cerrarVentana(cedula);
            FXMLLoader loader = principal.navegarVentana(TipoPantalla.PACIENTE.getRuta(), TipoPantalla.PACIENTE.getNombre());
            PacienteControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
        }else {
            principal.getSesion().cerrarSesion();
            principal.cerrarVentana(nombre);
        }
    }

    private void validarIngreso(ButtonType respuesta, Medico medico) {
        if (respuesta == ButtonType.OK) {
            principal.getSesion().setMedico(medico);
            principal.cerrarVentana(nombre);
            FXMLLoader loader = principal.navegarVentana(TipoPantalla.MEDICO.getRuta(), TipoPantalla.MEDICO.getNombre());
            MedicoControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
        }else {
            principal.getSesion().cerrarSesion();
            principal.cerrarVentana(nombre);
        }
    }
}
