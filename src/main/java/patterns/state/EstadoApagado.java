package patterns.state;

public final class EstadoApagado implements EstadoDispositivo {

    @Override public EstadoDispositivo transitionToOn() { return new EstadoEncendido(); }
    @Override public EstadoDispositivo transitionToOff() { return this; }
    @Override public EstadoDispositivo transitionToLocked() { throw unsupported("lock"); }
    @Override public EstadoDispositivo transitionToUnlocked() { throw unsupported("unlock"); }
    @Override public boolean isPoweredOn() { return false; }
    @Override public boolean isPoweredOff() { return true; }
    @Override public boolean isLocked() { return false; }
    @Override public boolean isUnlocked() { return false; }
    @Override public String getStateName() { return "OFF"; }

    private static IllegalStateException unsupported(String action) {
        return new IllegalStateException("An unpowered device cannot " + action + ".");
    }
}
