package model;

import util.enums.TipoNotificacion;

import java.time.LocalDateTime;

public class Notificacion {
    private String id;
    private TipoNotificacion tipo;
    private String descripcion;
    private LocalDateTime fecha;

    public Notificacion() {
    }

    public Notificacion(String id, TipoNotificacion tipo, String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public TipoNotificacion getTipo() { return tipo; }
    public void setTipo(TipoNotificacion tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
