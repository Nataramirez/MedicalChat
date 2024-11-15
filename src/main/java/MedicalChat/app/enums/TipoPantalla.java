package MedicalChat.app.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum TipoPantalla {

    INICIO("/inicio.fxml", "MedicalChat Inicio"),
    MEDICO("/medico.fxml", "MedicalChat Médico"),
    PACIENTE("/paciente.fxml", "MedicalChat Paciente"),
    REGISTRO("/registro.fxml", "Registro"),
    INICIO_SESION("/inicioSesion.fxml", "Inicio sesión");


    private String ruta;
    private String nombre;
}
