package model;

import patterns.state.EstadoDispositivo;

public abstract class Dispositivo {
    private String id;
    private String nombre;
    private String marca;
    private String modelo;
    private EstadoDispositivo estado;
    private Habitacion habitacion;

    protected Dispositivo() {
    }

    protected Dispositivo(String id, String nombre, String marca, String modelo,
                          EstadoDispositivo estado, Habitacion habitacion) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
        this.habitacion = habitacion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public EstadoDispositivo getEstado() { return estado; }
    public void setEstado(EstadoDispositivo estado) { this.estado = estado; }
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
}
