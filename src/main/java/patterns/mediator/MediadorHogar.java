package patterns.mediator;

public interface MediadorHogar {
    void registrarComponente(ComponenteHogar componente);
    void enviarMensaje(String mensaje, ComponenteHogar emisor);
}
