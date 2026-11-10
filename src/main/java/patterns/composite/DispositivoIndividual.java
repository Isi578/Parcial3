package patterns.composite;

import model.Dispositivo;

import java.util.Objects;

public final class DispositivoIndividual
        implements ComponenteHogar {

    private final Dispositivo device;

    public DispositivoIndividual(Dispositivo device) {
        this.device = Objects.requireNonNull(
                device,
                "Device cannot be null."
        );
    }

    @Override
    public String getId() {
        return device.getId();
    }

    @Override
    public String getName() {
        return device.getNombre();
    }

    @Override
    public void turnOn() {
        device.turnOn();
    }

    @Override
    public void turnOff() {
        device.turnOff();
    }

    @Override
    public boolean isActive() {
        return device.isActivo();
    }

    @Override
    public int getDeviceCount() {
        return 1;
    }

    @Override
    public int getActiveDeviceCount() {
        return device.isActivo() ? 1 : 0;
    }

    @Override
    public String getStatusDescription() {
        return device.getStatusDescription();
    }

    public Dispositivo getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return getName()
                + " - "
                + getStatusDescription();
    }
}

