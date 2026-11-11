package patterns.state;

public final class EstadoBloqueado implements EstadoDispositivo {

    @Override public EstadoDispositivo transitionToOn() { throw unsupported(); }
    @Override public EstadoDispositivo transitionToOff() { throw unsupported(); }
    @Override public EstadoDispositivo transitionToLocked() { return this; }
    @Override public EstadoDispositivo transitionToUnlocked() { return new EstadoDesbloqueado(); }
    @Override public boolean isPoweredOn() { return false; }
    @Override public boolean isPoweredOff() { return false; }
    @Override public boolean isLocked() { return true; }
    @Override public boolean isUnlocked() { return false; }
    @Override public String getStateName() { return "LOCKED"; }

    private static IllegalStateException unsupported() {
        return new IllegalStateException("A smart lock cannot be turned on or off.");
    }
}
