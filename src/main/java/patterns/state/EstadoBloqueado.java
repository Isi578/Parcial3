package patterns.state;

public class EstadoBloqueado implements EstadoDispositivo {
    @Override public void encender() { }
    @Override public void apagar() { }
    @Override public void bloquear() { }
    @Override public void desbloquear() { }
}
