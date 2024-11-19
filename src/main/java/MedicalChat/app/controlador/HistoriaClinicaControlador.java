package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;
import MedicalChat.app.modelo.HistoriaClinica;
import MedicalChat.app.modelo.Paciente;
import MedicalChat.app.modelo.RegistroConsultas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HistoriaClinicaControlador implements Initializable, Observable {
    private final PrincipalControlador principal;
    private Observable observable;
    private Paciente paciente;
    @FXML
    public TextField telefono;
    @FXML
    public TextField correo;
    @FXML
    public TextField direccion;
    @FXML
    public TextField nombre;
    @FXML
    public TextField identificacion;
    @FXML
    public TextField fechaNacimiento;
    @FXML
    public TextField sexo;

    public HistoriaClinicaControlador() {
        principal = PrincipalControlador.getInstancia();
        paciente = principal.getSesion().getPaciente();
        System.out.println(principal.getSesion().getPaciente().toString());
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nombre.setText(paciente.getNombre());
        identificacion.setText(paciente.getCedula());
        fechaNacimiento.setText(paciente.getFechaNacimiento());
        //sexo.setText(paciente.getSexo().toString());
        telefono.setText(paciente.getTelefono());
        correo.setText(paciente.getCorreo());
        direccion.setText(paciente.getDireccion());
    }

    @Override
    public void notificar() {

    }

    public void descargar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                File pdfPath = new File(selectedFile.getAbsolutePath() + File.separator + "Historia-Clinica" + paciente.getCedula() + ".pdf");
                crearDocumento(pdfPath);
            } else {
                System.out.println("Lo seleccionado no es un directorio.");
            }
        }
    }

    public void crearDocumento(File ruta) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                contentStream.newLineAtOffset(80, 700);
                for (String line : formatoHistoriaClinica(paciente)) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15); // Mover a la siguiente línea
                }
                contentStream.endText();
            }
            document.save(ruta);
            principal.mostrarAlerta("Documento descargado con éxito", Alert.AlertType.INFORMATION);
            principal.cerrarVentana(nombre);
            System.out.println(formatoRecetasMedicas(paciente.getHistoriaClinica()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String[] formatoHistoriaClinica(Paciente paciente) {
        return new String[]{
                "HISTORIA CLÍNICA",
                " ",
                "INFORMACIÓN PERSONAL",
                "NOMBRE: " + paciente.getNombre(),
                "IDENTIFICACIÓN: " + paciente.getCedula(),
                "FECHA DE NACIMIENTO: " + paciente.getFechaNacimiento(),
                " ",
                "INFORMACIÓN DE CONTACTO",
                "TELÉFONO: " + paciente.getTelefono(),
                "CORREO: " + paciente.getCorreo(),
                "DIRECCIÓN: " + paciente.getDireccion(),
                " ",
                "HÁBITOS: " + Arrays.toString(paciente.getHistoriaClinica().getHabitos()),
                " ",
                "ANTECEDENTES: " + Arrays.toString(paciente.getHistoriaClinica().getAntecedentes()),
                " ",
                "HISTÓRICO CONSULTAS",
                formatoRecetasMedicas(paciente.getHistoriaClinica()).toString()
        };
    }

    private ArrayList<String> formatoRecetasMedicas(HistoriaClinica historiaClinica) {
        ArrayList<String> consultas = new ArrayList<>();
        if(historiaClinica.getConsultas() != null) {
            for (RegistroConsultas registroConsulta : historiaClinica.getConsultas()) {
                consultas.add("MOTIVO DE CONSULTA: " + registroConsulta.getMotivoConsulta());
                consultas.add("DIAGNOSTICO: " + registroConsulta.getDiagnostico());
            }
        }
        return consultas;
    }
}
