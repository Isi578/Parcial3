package patterns.observer;

import model.Evento;
import model.Dispositivo;

import java.util.ArrayList;
import java.util.List;

public class DispositivoObservable implements Sujeto {
    private Dispositivo dispositivo;
    private Evento eventoActual;
    private final List<Observador> observadores = new ArrayList<>();

    public DispositivoObservable(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }
    public Evento getEventoActual() { return eventoActual; }
    public void setEventoActual(Evento eventoActual) { this.eventoActual = eventoActual; }

    @Override
    public void agregarObservador(Observador observador) {
    }

    @Override
    public void eliminarObservador(Observador observador) {
    }

    @Override
    public void notificarObservadores() {
    }
}
