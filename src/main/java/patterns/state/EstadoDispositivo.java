package patterns.state;

public interface EstadoDispositivo {
    EstadoDispositivo transitionToOn();
    EstadoDispositivo transitionToOff();
    EstadoDispositivo transitionToLocked();
    EstadoDispositivo transitionToUnlocked();
    boolean isPoweredOn();
    boolean isPoweredOff();
    boolean isLocked();
    boolean isUnlocked();
    String getStateName();
}
