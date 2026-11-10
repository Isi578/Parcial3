package model;

import patterns.state.EstadoDispositivo;

import java.util.Objects;

public abstract class Dispositivo {

    private String id;
    private String nombre;
    private String marca;
    private String modelo;
    private EstadoDispositivo estado;
    private Habitacion habitacion;
    private boolean activo;

    protected Dispositivo() {
        this.activo = false;
    }

    protected Dispositivo(
            String id,
            String nombre,
            String marca,
            String modelo,
            EstadoDispositivo estado,
            Habitacion habitacion) {

        this.id = requireText(id, "Device id");
        this.nombre = requireText(nombre, "Device name");
        this.marca = requireText(marca, "Device brand");
        this.modelo = requireText(modelo, "Device model");
        this.estado = estado;
        this.activo = false;

        if (habitacion != null) {
            habitacion.addDevice(this);
        }
    }

    public final boolean turnOn() {
        if (activo) {
            return false;
        }

        activo = true;
        onTurnedOn();
        return true;
    }

    public final boolean turnOff() {
        if (!activo) {
            return false;
        }

        activo = false;
        onTurnedOff();
        return true;
    }

    protected void onTurnedOn() {
    }

    protected void onTurnedOff() {
    }

    public String getDeviceType() {
        return getClass().getSimpleName();
    }

    public String getStatusDescription() {
        return activo ? "ON" : "OFF";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = requireText(id, "Device id");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = requireText(nombre, "Device name");
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = requireText(marca, "Device brand");
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = requireText(modelo, "Device model");
    }

    public EstadoDispositivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoDispositivo estado) {
        this.estado = estado;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        if (this.habitacion == habitacion) {
            return;
        }

        if (habitacion == null) {
            if (this.habitacion != null) {
                Habitacion previousRoom = this.habitacion;
                this.habitacion = null;
                previousRoom.removeDeviceReference(this);
            }
            return;
        }

        habitacion.addDevice(this);
    }

    void assignRoomReference(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        if (activo) {
            turnOn();
        } else {
            turnOff();
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }

        return value.trim();
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Dispositivo other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nombre
                + " ["
                + getDeviceType()
                + "] - "
                + getStatusDescription();
    }
}