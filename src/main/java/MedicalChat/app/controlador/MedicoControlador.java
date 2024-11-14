package MedicalChat.app.controlador;

import MedicalChat.app.controlador.observador.Observable;

public class MedicoControlador {
    private final PrincipalControlador principal;
    private  Observable observable;

    public MedicoControlador() {
        principal = PrincipalControlador.getInstancia();
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }
}
