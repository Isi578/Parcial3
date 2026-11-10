package model;

import patterns.state.EstadoBloqueado;
import patterns.state.EstadoDesbloqueado;
import patterns.state.EstadoDispositivo;

public class CerraduraInteligente extends Dispositivo {

    public static final String DEFAULT_LOCK_TYPE =
            "ELECTRONIC";

    private String tipoCerradura;

    public CerraduraInteligente() {
        super();
        setEstado(new EstadoDesbloqueado());
        this.tipoCerradura = DEFAULT_LOCK_TYPE;
    }

    public CerraduraInteligente(
            String id,
            String nombre,
            String marca,
            String modelo,
            EstadoDispositivo estado,
            Habitacion habitacion,
            String tipoCerradura,
            boolean bloqueada) {

        super(
                id,
                nombre,
                marca,
                modelo,
                resolveInitialState(estado, bloqueada),
                habitacion
        );

        setTipoCerradura(tipoCerradura);
    }

    public boolean lock() {
        EstadoDispositivo previousState = getEstado();

        EstadoDispositivo nextState =
                previousState.transitionToLocked();

        if (nextState == previousState) {
            return false;
        }

        setEstado(nextState);
        return true;
    }

    public boolean unlock() {
        EstadoDispositivo previousState = getEstado();

        EstadoDispositivo nextState =
                previousState.transitionToUnlocked();

        if (nextState == previousState) {
            return false;
        }

        setEstado(nextState);
        return true;
    }

    public boolean isBloqueada() {
        return getEstado().isLocked();
    }

    public void setBloqueada(boolean bloqueada) {
        if (bloqueada) {
            lock();
        } else {
            unlock();
        }
    }

    @Override
    public String getDeviceType() {
        return "SMART_LOCK";
    }

    @Override
    public String getStatusDescription() {
        return getEstado().getStateName()
                + " - Lock type: "
                + tipoCerradura;
    }

    public String getTipoCerradura() {
        return tipoCerradura;
    }

    public void setTipoCerradura(
            String tipoCerradura) {

        if (tipoCerradura == null
                || tipoCerradura.isBlank()) {

            this.tipoCerradura =
                    DEFAULT_LOCK_TYPE;

            return;
        }

        this.tipoCerradura =
                tipoCerradura.trim().toUpperCase();
    }

    private static EstadoDispositivo resolveInitialState(
            EstadoDispositivo estado,
            boolean bloqueada) {

        if (estado != null) {
            if (estado.isLocked()
                    || estado.isUnlocked()) {

                return estado;
            }

            throw new IllegalArgumentException(
                    "A smart lock requires a locked "
                            + "or unlocked state."
            );
        }

        if (bloqueada) {
            return new EstadoBloqueado();
        }

        return new EstadoDesbloqueado();
    }
}