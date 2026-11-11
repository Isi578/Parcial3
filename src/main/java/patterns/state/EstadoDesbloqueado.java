package patterns.state;

public final class EstadoDesbloqueado implements EstadoDispositivo {

    @Override public EstadoDispositivo transitionToOn() { throw unsupported(); }
    @Override public EstadoDispositivo transitionToOff() { throw unsupported(); }
    @Override public EstadoDispositivo transitionToLocked() { return new EstadoBloqueado(); }
    @Override public EstadoDispositivo transitionToUnlocked() { return this; }
    @Override public boolean isPoweredOn() { return false; }
    @Override public boolean isPoweredOff() { return false; }
    @Override public boolean isLocked() { return false; }
    @Override public boolean isUnlocked() { return true; }
    @Override public String getStateName() { return "UNLOCKED"; }

    private static IllegalStateException unsupported() {
        return new IllegalStateException("A smart lock cannot be turned on or off.");
    }
}
