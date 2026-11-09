package model;

import patterns.state.EstadoDispositivo;

public class Termostato extends Dispositivo {
    private double temperaturaActual;
    private double temperaturaObjetivo;

    public Termostato() {
    }

    public Termostato(String id, String nombre, String marca, String modelo,
                      EstadoDispositivo estado, Habitacion habitacion,
                      double temperaturaActual, double temperaturaObjetivo) {
        super(id, nombre, marca, modelo, estado, habitacion);
        this.temperaturaActual = temperaturaActual;
        this.temperaturaObjetivo = temperaturaObjetivo;
    }

    public double getTemperaturaActual() { return temperaturaActual; }
    public void setTemperaturaActual(double temperaturaActual) { this.temperaturaActual = temperaturaActual; }
    public double getTemperaturaObjetivo() { return temperaturaObjetivo; }
    public void setTemperaturaObjetivo(double temperaturaObjetivo) { this.temperaturaObjetivo = temperaturaObjetivo; }
}
