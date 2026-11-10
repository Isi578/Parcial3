package patterns.state;

public interface EstadoDispositivo {

    void encender();

    void apagar();

    void bloquear();

    void desbloquear();

    default EstadoDispositivo transitionToOn() {
        if (this instanceof EstadoEncendido) {
            return this;
        }

        if (this instanceof EstadoApagado) {
            return new EstadoEncendido();
        }

        throw new IllegalStateException(
                "This device state does not support turning on."
        );
    }

    default EstadoDispositivo transitionToOff() {
        if (this instanceof EstadoApagado) {
            return this;
        }

        if (this instanceof EstadoEncendido) {
            return new EstadoApagado();
        }

        throw new IllegalStateException(
                "This device state does not support turning off."
        );
    }

    default EstadoDispositivo transitionToLocked() {
        if (this instanceof EstadoBloqueado) {
            return this;
        }

        if (this instanceof EstadoDesbloqueado) {
            return new EstadoBloqueado();
        }

        throw new IllegalStateException(
                "This device state does not support locking."
        );
    }

    default EstadoDispositivo transitionToUnlocked() {
        if (this instanceof EstadoDesbloqueado) {
            return this;
        }

        if (this instanceof EstadoBloqueado) {
            return new EstadoDesbloqueado();
        }

        throw new IllegalStateException(
                "This device state does not support unlocking."
        );
    }

    default boolean isPoweredOn() {
        return this instanceof EstadoEncendido;
    }

    default boolean isPoweredOff() {
        return this instanceof EstadoApagado;
    }

    default boolean isLocked() {
        return this instanceof EstadoBloqueado;
    }

    default boolean isUnlocked() {
        return this instanceof EstadoDesbloqueado;
    }

    default String getStateName() {
        if (this instanceof EstadoEncendido) {
            return "ON";
        }

        if (this instanceof EstadoApagado) {
            return "OFF";
        }

        if (this instanceof EstadoBloqueado) {
            return "LOCKED";
        }

        if (this instanceof EstadoDesbloqueado) {
            return "UNLOCKED";
        }

        return getClass().getSimpleName();
    }
}