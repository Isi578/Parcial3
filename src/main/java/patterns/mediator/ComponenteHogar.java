package patterns.mediator;

import model.Dispositivo;

import java.util.Objects;

public final class ComponenteHogar {

    private final String id;
    private final Dispositivo device;
    private MediadorHogar mediator;

    public ComponenteHogar(
            String id,
            Dispositivo device) {

        this.id = requireText(
                id,
                "Component id"
        );

        this.device = Objects.requireNonNull(
                device,
                "Device cannot be null."
        );
    }

    public void connectToMediator(
            MediadorHogar mediator) {

        MediadorHogar newMediator =
                Objects.requireNonNull(
                        mediator,
                        "Mediator cannot be null."
                );

        if (this.mediator == newMediator) {
            return;
        }

        if (this.mediator != null) {
            this.mediator.removeComponent(id);
        }

        this.mediator = newMediator;
        newMediator.registerComponent(this);
    }

    public void disconnectFromMediator() {
        if (mediator == null) {
            return;
        }

        MediadorHogar currentMediator =
                mediator;

        mediator = null;
        currentMediator.removeComponent(id);
    }

    public int reportEvent(String eventType) {
        MediadorHogar currentMediator =
                Objects.requireNonNull(
                        mediator,
                        "The component is not connected "
                                + "to a mediator."
                );

        return currentMediator.notifyEvent(
                this,
                normalizeEventType(eventType)
        );
    }

    public boolean turnOn() {
        return device.turnOn();
    }

    public boolean turnOff() {
        return device.turnOff();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return device.getNombre();
    }

    public Dispositivo getDevice() {
        return device;
    }

    public MediadorHogar getMediator() {
        return mediator;
    }

    public boolean isConnected() {
        return mediator != null;
    }

    public String getStatusDescription() {
        return device.getStatusDescription();
    }

    private static String normalizeEventType(
            String eventType) {

        return requireText(
                eventType,
                "Event type"
        ).toUpperCase();
    }

    private static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    @Override
    public String toString() {
        return id
                + " - "
                + getName()
                + " - "
                + getStatusDescription();
    }
}