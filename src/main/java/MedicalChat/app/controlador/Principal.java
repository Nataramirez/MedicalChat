package MedicalChat.app.controlador;

import MedicalChat.app.enums.TipoPantalla;
import MedicalChat.app.modelo.MedicalChat;
import MedicalChat.app.servicio.ServiciosEmpresa;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal implements ServiciosEmpresa {
    private final MedicalChat medicalChat;
    public static Principal INSTANCIA;

    public Principal() {
        medicalChat = new MedicalChat();
    }

    public static Principal getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new Principal();
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

            if(nombreArchivoFxml.equals(TipoPantalla.MEDICO.getRuta()) || nombreArchivoFxml.equals(TipoPantalla.PACIENTE.getRuta())){
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
}
