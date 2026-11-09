package model;

import patterns.command.Comando;
import patterns.strategy.EstrategiaAutomatizacion;
import util.enums.EstadoAutomatizacion;

import java.util.ArrayList;
import java.util.List;

public class Automatizacion {
    private String id;
    private String nombre;
    private String descripcion;
    private EstadoAutomatizacion estado;
    private EstrategiaAutomatizacion estrategia;
    private List<Comando> comandos = new ArrayList<>();

    public Automatizacion() {
    }

    public Automatizacion(String id, String nombre, String descripcion, EstadoAutomatizacion estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EstadoAutomatizacion getEstado() { return estado; }
    public void setEstado(EstadoAutomatizacion estado) { this.estado = estado; }
    public EstrategiaAutomatizacion getEstrategia() { return estrategia; }
    public void setEstrategia(EstrategiaAutomatizacion estrategia) { this.estrategia = estrategia; }
    public List<Comando> getComandos() { return comandos; }
    public void setComandos(List<Comando> comandos) { this.comandos = comandos; }
}
