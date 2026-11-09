package model;

import util.enums.TipoEvento;

import java.time.LocalDateTime;

public class Evento {
    private String id;
    private TipoEvento tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private Dispositivo dispositivo;

    public Evento() {
    }

    public Evento(String id, TipoEvento tipo, String descripcion, LocalDateTime fecha, Dispositivo dispositivo) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.dispositivo = dispositivo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public TipoEvento getTipo() { return tipo; }
    public void setTipo(TipoEvento tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }
}
