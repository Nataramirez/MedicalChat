package MedicalChat.app.modelo;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class RegistroConsultas {
    private String motivoConsulta;
    private String diagnostico;
    private RecetaMedica recetaMedica;
}
