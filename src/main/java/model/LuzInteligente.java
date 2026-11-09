package model;

import patterns.state.EstadoDispositivo;

public class LuzInteligente extends Dispositivo {
    private int nivelBrillo;
    private String color;

    public LuzInteligente() {
    }

    public LuzInteligente(String id, String nombre, String marca, String modelo,
                          EstadoDispositivo estado, Habitacion habitacion, int nivelBrillo, String color) {
        super(id, nombre, marca, modelo, estado, habitacion);
        this.nivelBrillo = nivelBrillo;
        this.color = color;
    }

    public int getNivelBrillo() { return nivelBrillo; }
    public void setNivelBrillo(int nivelBrillo) { this.nivelBrillo = nivelBrillo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
