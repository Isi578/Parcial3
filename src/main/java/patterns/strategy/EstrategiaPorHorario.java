package patterns.strategy;

import model.Automatizacion;

public class EstrategiaPorHorario implements EstrategiaAutomatizacion {
    private String hora;

    public EstrategiaPorHorario() {
    }

    public EstrategiaPorHorario(String hora) {
        this.hora = hora;
    }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    @Override
    public void ejecutar(Automatizacion automatizacion) {
    }
}
