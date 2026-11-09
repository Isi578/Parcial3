package model;

import patterns.state.EstadoDispositivo;

public class SensorMovimiento extends Dispositivo {
    private boolean movimientoDetectado;
    private int sensibilidad;

    public SensorMovimiento() {
    }

    public SensorMovimiento(String id, String nombre, String marca, String modelo,
                            EstadoDispositivo estado, Habitacion habitacion,
                            boolean movimientoDetectado, int sensibilidad) {
        super(id, nombre, marca, modelo, estado, habitacion);
        this.movimientoDetectado = movimientoDetectado;
        this.sensibilidad = sensibilidad;
    }

    public boolean isMovimientoDetectado() { return movimientoDetectado; }
    public void setMovimientoDetectado(boolean movimientoDetectado) { this.movimientoDetectado = movimientoDetectado; }
    public int getSensibilidad() { return sensibilidad; }
    public void setSensibilidad(int sensibilidad) { this.sensibilidad = sensibilidad; }
}
