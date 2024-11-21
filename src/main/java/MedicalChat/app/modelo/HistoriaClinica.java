package MedicalChat.app.modelo;

import MedicalChat.app.enums.TipoAntecedentes;
import MedicalChat.app.enums.TipoGenero;
import MedicalChat.app.enums.TipoHabitos;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
@Builder
public class HistoriaClinica {
    private TipoAntecedentes[] antecedentes;
    private TipoHabitos[] habitos;
    private ArrayList<RegistroConsultas> consultas;

    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "antecedentes=" + Arrays.toString(antecedentes) +
                ", habitos=" + Arrays.toString(habitos) +
                ", consultas=" + consultas +
                '}';
    }
}
