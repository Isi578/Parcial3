package patterns.state;

public final class EstadoEncendido implements EstadoDispositivo {

    @Override public EstadoDispositivo transitionToOn() { return this; }
    @Override public EstadoDispositivo transitionToOff() { return new EstadoApagado(); }
    @Override public EstadoDispositivo transitionToLocked() { throw unsupported("lock"); }
    @Override public EstadoDispositivo transitionToUnlocked() { throw unsupported("unlock"); }
    @Override public boolean isPoweredOn() { return true; }
    @Override public boolean isPoweredOff() { return false; }
    @Override public boolean isLocked() { return false; }
    @Override public boolean isUnlocked() { return false; }
    @Override public String getStateName() { return "ON"; }

    private static IllegalStateException unsupported(String action) {
        return new IllegalStateException("A powered device cannot " + action + ".");
    }
}
