package patterns.state;

public class EstadoApagado implements EstadoDispositivo {
    @Override public void encender() { }
    @Override public void apagar() { }
    @Override public void bloquear() { }
    @Override public void desbloquear() { }
}
