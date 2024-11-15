package MedicalChat.app.controlador;

import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.enums.TipoRegistro;
import MedicalChat.app.modelo.MedicalChat;
import MedicalChat.app.modelo.Medico;
import MedicalChat.app.modelo.Paciente;
import MedicalChat.app.modelo.Sesion;
import MedicalChat.app.servicio.ServiciosEmpresa;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class PrincipalControlador implements ServiciosEmpresa {
    private final MedicalChat medicalChat;
    private final Sesion sesion;
    public static PrincipalControlador INSTANCIA;

    public PrincipalControlador() {
        medicalChat = new MedicalChat();
        sesion = new Sesion();
    }

    public static PrincipalControlador getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new PrincipalControlador();
        }
        return INSTANCIA;
    }

    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana){
        try {
            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);

            if(nombreArchivoFxml.equals(TipoPantalla.MEDICO.getRuta()) ||
                    nombreArchivoFxml.equals(TipoPantalla.PACIENTE.getRuta()) ||
                    nombreArchivoFxml.equals(TipoPantalla.INICIO.getRuta())){
               stage.setMaximized(true);
            }

            // Mostrar la nueva ventana
            stage.show();
            return loader;

        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }

    public void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public ButtonType mostrarAlertaConfirmacion(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        return alert.getResult();
    }

    public void cerrarVentana(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @Override
    public Paciente agregarPaciente(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception {
        return medicalChat.agregarPaciente(nombreCompleto, cedula, numeroTelefono, correoEmail, password);
    }

    @Override
    public Paciente obtenerPaciente(String cedula) throws Exception {
        return medicalChat.obtenerPaciente(cedula);
    }

    @Override
    public Medico agregarMedico(String nombreCompleto, String cedula, String numeroTelefono, String correoEmail, String password) throws Exception {
        return medicalChat.agregarMedico(nombreCompleto, cedula, numeroTelefono, correoEmail, password);
    }

    @Override
    public Medico obtenerMedico(String cedula) throws Exception {
        return medicalChat.obtenerMedico(cedula);
    }

    @Override
    public boolean iniciarSesion(String cedula, String password, TipoRegistro tipo) throws Exception {
        return medicalChat.iniciarSesion(cedula, password, tipo);
    }
}
