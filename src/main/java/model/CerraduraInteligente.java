package model;

import patterns.state.EstadoDispositivo;

public class CerraduraInteligente extends Dispositivo {
    private String tipoCerradura;
    private boolean bloqueada;

    public CerraduraInteligente() {
    }

    public CerraduraInteligente(String id, String nombre, String marca, String modelo,
                                EstadoDispositivo estado, Habitacion habitacion,
                                String tipoCerradura, boolean bloqueada) {
        super(id, nombre, marca, modelo, estado, habitacion);
        this.tipoCerradura = tipoCerradura;
        this.bloqueada = bloqueada;
    }

    public String getTipoCerradura() { return tipoCerradura; }
    public void setTipoCerradura(String tipoCerradura) { this.tipoCerradura = tipoCerradura; }
    public boolean isBloqueada() { return bloqueada; }
    public void setBloqueada(boolean bloqueada) { this.bloqueada = bloqueada; }
}
