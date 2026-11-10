package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Habitacion {

    private String id;
    private String nombre;
    private String descripcion;
    private final List<Dispositivo> dispositivos = new ArrayList<>();

    public Habitacion() {
    }

    public Habitacion(String id, String nombre, String descripcion) {
        this.id = requireText(id, "Room id");
        this.nombre = requireText(nombre, "Room name");
        this.descripcion = normalizeDescription(descripcion);
    }

    public void addDevice(Dispositivo dispositivo) {
        Objects.requireNonNull(dispositivo, "Device cannot be null.");

        Optional<Dispositivo> existingDevice =
                findDeviceById(dispositivo.getId());

        if (existingDevice.isPresent()
                && existingDevice.get() != dispositivo) {

            throw new IllegalArgumentException(
                    "A device with id '"
                            + dispositivo.getId()
                            + "' already exists in room '"
                            + nombre
                            + "'."
            );
        }

        if (dispositivos.contains(dispositivo)) {
            return;
        }

        Habitacion previousRoom = dispositivo.getHabitacion();

        if (previousRoom != null && previousRoom != this) {
            previousRoom.removeDeviceReference(dispositivo);
        }

        dispositivos.add(dispositivo);
        dispositivo.assignRoomReference(this);
    }

    public Dispositivo removeDevice(String deviceId) {
        Dispositivo dispositivo = findDeviceById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Device with id '"
                                + deviceId
                                + "' was not found in room '"
                                + nombre
                                + "'."
                ));

        removeDeviceReference(dispositivo);
        dispositivo.assignRoomReference(null);

        return dispositivo;
    }

    void removeDeviceReference(Dispositivo dispositivo) {
        dispositivos.remove(dispositivo);
    }

    public Optional<Dispositivo> findDeviceById(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            return Optional.empty();
        }

        String normalizedId = deviceId.trim();

        return dispositivos.stream()
                .filter(device -> normalizedId.equals(device.getId()))
                .findFirst();
    }

    public void turnOnAllDevices() {
        dispositivos.forEach(Dispositivo::turnOn);
    }

    public void turnOffAllDevices() {
        dispositivos.forEach(Dispositivo::turnOff);
    }

    public long countActiveDevices() {
        return dispositivos.stream()
                .filter(Dispositivo::isActivo)
                .count();
    }

    public String getStatusSummary() {
        return countActiveDevices()
                + " of "
                + dispositivos.size()
                + " devices are active";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = requireText(id, "Room id");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = requireText(nombre, "Room name");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = normalizeDescription(descripcion);
    }

    public List<Dispositivo> getDispositivos() {
        return Collections.unmodifiableList(dispositivos);
    }

    public void setDispositivos(List<Dispositivo> dispositivos) {
        List<Dispositivo> currentDevices =
                new ArrayList<>(this.dispositivos);

        for (Dispositivo currentDevice : currentDevices) {
            removeDevice(currentDevice.getId());
        }

        if (dispositivos == null) {
            return;
        }

        for (Dispositivo dispositivo : dispositivos) {
            addDevice(dispositivo);
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    private static String normalizeDescription(String description) {
        return description == null ? "" : description.trim();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Habitacion other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nombre + " - " + getStatusSummary();
    }
}
