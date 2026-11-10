package patterns.command;

import model.Dispositivo;

import java.util.Objects;

public class ApagarDispositivoCommand
        implements Comando {

    private final Dispositivo dispositivo;

    private boolean executed;
    private boolean changedDeviceState;
    private boolean previousActiveState;

    public ApagarDispositivoCommand(
            Dispositivo dispositivo) {

        this.dispositivo = Objects.requireNonNull(
                dispositivo,
                "Device cannot be null."
        );
    }

    @Override
    public void ejecutar() {
        previousActiveState = dispositivo.isActivo();
        changedDeviceState = dispositivo.turnOff();
        executed = true;
    }

    @Override
    public boolean puedeDeshacer() {
        return executed && changedDeviceState;
    }

    @Override
    public void deshacer() {
        validateExecution();

        if (!changedDeviceState) {
            return;
        }

        if (previousActiveState) {
            dispositivo.turnOn();
        } else {
            dispositivo.turnOff();
        }

        changedDeviceState = false;
    }

    @Override
    public String getDescription() {
        return "Turn off device: "
                + dispositivo.getNombre();
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public boolean wasExecuted() {
        return executed;
    }

    public boolean changedDeviceState() {
        return changedDeviceState;
    }

    private void validateExecution() {
        if (!executed) {
            throw new IllegalStateException(
                    "The command has not been executed."
            );
        }
    }
}
