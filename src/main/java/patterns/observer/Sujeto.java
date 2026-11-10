package patterns.observer;

public interface Sujeto {

    void agregarObservador(Observador observador);

    void eliminarObservador(Observador observador);

    void notificarObservadores();

    default void addObserver(Observador observer) {
        agregarObservador(observer);
    }

    default void removeObserver(Observador observer) {
        eliminarObservador(observer);
    }

    default void notifyObservers() {
        notificarObservadores();
    }
}