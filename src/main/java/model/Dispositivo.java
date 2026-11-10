package model;

import patterns.state.EstadoApagado;
import patterns.state.EstadoDispositivo;

import java.util.Objects;

public abstract class Dispositivo {

    private String id;
    private String nombre;
    private String marca;
    private String modelo;
    private EstadoDispositivo estado;
    private Habitacion habitacion;

    protected Dispositivo() {
        this.estado = new EstadoApagado();
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

        this.estado = Objects.requireNonNullElseGet(
                estado,
                EstadoApagado::new
        );

        if (habitacion != null) {
            habitacion.addDevice(this);
        }
    }

    public final boolean turnOn() {
        EstadoDispositivo previousState = estado;
        EstadoDispositivo nextState =
                estado.transitionToOn();

        if (nextState == previousState) {
            return false;
        }

        estado = nextState;
        onTurnedOn();
        return true;
    }

    public final boolean turnOff() {
        EstadoDispositivo previousState = estado;
        EstadoDispositivo nextState =
                estado.transitionToOff();

        if (nextState == previousState) {
            return false;
        }

        estado = nextState;
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
        return estado.getStateName();
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
        this.nombre = requireText(
                nombre,
                "Device name"
        );
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = requireText(
                marca,
                "Device brand"
        );
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = requireText(
                modelo,
                "Device model"
        );
    }

    public EstadoDispositivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoDispositivo estado) {
        this.estado = Objects.requireNonNull(
                estado,
                "Device state cannot be null."
        );
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
                Habitacion previousRoom =
                        this.habitacion;

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
        return estado.isPoweredOn();
    }

    public void setActivo(boolean activo) {
        if (activo) {
            turnOn();
        } else {
            turnOff();
        }
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