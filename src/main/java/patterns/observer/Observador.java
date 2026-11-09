package patterns.observer;

import model.Evento;

public interface Observador {
    void actualizar(Evento evento);
}
