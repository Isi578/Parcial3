package patterns.command;

import model.CerraduraInteligente;

import java.util.Objects;

public class DesbloquearPuertaCommand
        implements Comando {

    private final CerraduraInteligente cerradura;

    private boolean executed;
    private boolean changedLockState;
    private boolean previousLockedState;

    public DesbloquearPuertaCommand(
            CerraduraInteligente cerradura) {

        this.cerradura = Objects.requireNonNull(
                cerradura,
                "Smart lock cannot be null."
        );
    }

    @Override
    public void ejecutar() {
        previousLockedState =
                cerradura.isBloqueada();

        changedLockState =
                cerradura.unlock();

        executed = true;
    }

    @Override
    public boolean puedeDeshacer() {
        return executed
                && changedLockState;
    }

    @Override
    public void deshacer() {
        validateExecution();

        if (!changedLockState) {
            return;
        }

        restorePreviousState();
        changedLockState = false;
    }

    private void restorePreviousState() {
        if (previousLockedState) {
            cerradura.lock();
        } else {
            cerradura.unlock();
        }
    }

    @Override
    public String getDescription() {
        return "Unlock smart door: "
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
