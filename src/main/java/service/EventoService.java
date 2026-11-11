package service;

import model.Evento;
import patterns.observer.HistorialObserver;

import java.util.List;
import java.util.Objects;

public class EventoService {
    private final HistorialObserver historialObserver;

    public EventoService(HistorialObserver historialObserver) {
        this.historialObserver = Objects.requireNonNull(
                historialObserver,
                "History observer cannot be null."
        );
    }

    public List<Evento> obtenerHistorialEventos() {
        return List.copyOf(historialObserver.getEventHistory());
    }
}
