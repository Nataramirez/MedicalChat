package MedicalChat.app.modelo;

import MedicalChat.app.enums.TipoAntecedentes;
import MedicalChat.app.enums.TipoGenero;
import MedicalChat.app.enums.TipoHabitos;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
@Builder
public class HistoriaClinica implements Serializable {
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
