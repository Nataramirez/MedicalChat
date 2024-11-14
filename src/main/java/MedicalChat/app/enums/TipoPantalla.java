package MedicalChat.app.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum TipoPantalla {

    INICIO("/inicio.fxml", "MedicalChat Inicio"),
    MEDICO("/medico.fxml", "MedicalChat Médico"),
    PACIENTE("/paciente.fxml", "MedicalChat Paciente"),
    REGISTRO("/registro.fxml", "Registro");

    private String ruta;
    private String nombre;
}
