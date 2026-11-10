package patterns.command;

import model.Dispositivo;

import java.util.Objects;

public class EncenderDispositivoCommand
        implements Comando {

    private final Dispositivo dispositivo;

    private boolean executed;
    private boolean changedDeviceState;
    private boolean previousActiveState;

    public EncenderDispositivoCommand(
            Dispositivo dispositivo) {

        this.dispositivo = Objects.requireNonNull(
                dispositivo,
                "Device cannot be null."
        );
    }

    @Override
    public void ejecutar() {
        previousActiveState = dispositivo.isActivo();
        changedDeviceState = dispositivo.turnOn();
        executed = true;
    }

    @Override
    public boolean puedeDeshacer() {
        return executed && changedDeviceState;
    }

    @Override
    public void deshacer() {
        if (!executed) {
            throw new IllegalStateException(
                    "The command has not been executed."
            );
        }

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
        return "Turn on device: "
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
}
