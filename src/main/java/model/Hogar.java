package model;

import java.util.ArrayList;
import java.util.List;

public class Hogar {
    private String nombre;
    private String direccion;
    private List<Habitacion> habitaciones = new ArrayList<>();

    public Hogar() {
    }

    public Hogar(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public List<Habitacion> getHabitaciones() { return habitaciones; }
    public void setHabitaciones(List<Habitacion> habitaciones) { this.habitaciones = habitaciones; }
}
