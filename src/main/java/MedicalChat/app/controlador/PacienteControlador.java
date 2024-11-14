package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;

public class PacienteControlador {
    private final PrincipalControlador principal;
    private Observable observable;

    public PacienteControlador() {
        principal = PrincipalControlador.getInstancia();
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }
}
