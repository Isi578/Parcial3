package patterns.observer;

import model.Dispositivo;
import model.Evento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DispositivoObservable implements Sujeto {

    private final Dispositivo dispositivo;
    private Evento eventoActual;

    private final List<Observador> observadores =
            new ArrayList<>();

    public DispositivoObservable(
            Dispositivo dispositivo) {

        this.dispositivo = Objects.requireNonNull(
                dispositivo,
                "Device cannot be null."
        );
    }

    @Override
    public void agregarObservador(
            Observador observador) {

        Objects.requireNonNull(
                observador,
                "Observer cannot be null."
        );

        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(
            Observador observador) {

        if (observador == null) {
            return;
        }

        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        Evento eventToPublish = Objects.requireNonNull(
                eventoActual,
                "No event is available to notify."
        );

        List<Observador> observersSnapshot =
                new ArrayList<>(observadores);

        for (Observador observador : observersSnapshot) {
            observador.actualizar(eventToPublish);
        }
    }

    public void publishEvent(Evento evento) {
        validateEvent(evento);
        this.eventoActual = evento;
        notificarObservadores();
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public Evento getEventoActual() {
        return eventoActual;
    }

    public void setEventoActual(Evento eventoActual) {
        validateEvent(eventoActual);
        this.eventoActual = eventoActual;
    }

    public int getObserverCount() {
        return observadores.size();
    }

    public boolean hasObservers() {
        return !observadores.isEmpty();
    }

    public boolean containsObserver(
            Observador observador) {

        return observadores.contains(observador);
    }

    public List<Observador> getObservers() {
        return Collections.unmodifiableList(
                observadores
        );
    }

    private void validateEvent(Evento evento) {
        Objects.requireNonNull(
                evento,
                "Event cannot be null."
        );

        if (evento.getDispositivo() != null
                && evento.getDispositivo()
                != dispositivo) {

            throw new IllegalArgumentException(
                    "The event belongs to a different device."
            );
        }
    }
}
