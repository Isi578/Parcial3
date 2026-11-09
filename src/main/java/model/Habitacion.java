package model;

import java.util.ArrayList;
import java.util.List;

public class Habitacion {
    private String id;
    private String nombre;
    private String descripcion;
    private List<Dispositivo> dispositivos = new ArrayList<>();

    public Habitacion() {
    }

    public Habitacion(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Dispositivo> getDispositivos() { return dispositivos; }
    public void setDispositivos(List<Dispositivo> dispositivos) { this.dispositivos = dispositivos; }
}
