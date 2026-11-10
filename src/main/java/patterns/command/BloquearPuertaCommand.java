package patterns.command;

import model.CerraduraInteligente;

import java.util.Objects;

public class BloquearPuertaCommand
        implements Comando {

    private final CerraduraInteligente cerradura;

    private boolean executed;
    private boolean changedLockState;
    private boolean previousLockedState;

    public BloquearPuertaCommand(
            CerraduraInteligente cerradura) {

        this.cerradura = Objects.requireNonNull(
                cerradura,
                "Smart lock cannot be null."
        );
    }

    @Override
    public void ejecutar() {
        previousLockedState = cerradura.isBloqueada();

        if (previousLockedState) {
            changedLockState = false;
        } else {
            cerradura.setBloqueada(true);
            changedLockState = true;
        }

        executed = true;
    }

    @Override
    public boolean puedeDeshacer() {
        return executed && changedLockState;
    }

    @Override
    public void deshacer() {
        validateExecution();

        if (!changedLockState) {
            return;
        }

        cerradura.setBloqueada(
                previousLockedState
        );

        changedLockState = false;
    }

    @Override
    public String getDescription() {
        return "Lock smart door: "
                + cerradura.getNombre();
    }

    public CerraduraInteligente getCerradura() {
        return cerradura;
    }

    public boolean wasExecuted() {
        return executed;
    }

    public boolean changedLockState() {
        return changedLockState;
    }

    private void validateExecution() {
        if (!executed) {
            throw new IllegalStateException(
                    "The command has not been executed."
            );
        }
    }
}