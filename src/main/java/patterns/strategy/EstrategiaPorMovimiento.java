package patterns.strategy;

import model.Automatizacion;
import model.SensorMovimiento;

public class EstrategiaPorMovimiento implements EstrategiaAutomatizacion {
    private SensorMovimiento sensor;

    public EstrategiaPorMovimiento() {
    }

    public EstrategiaPorMovimiento(SensorMovimiento sensor) {
        this.sensor = sensor;
    }

    public SensorMovimiento getSensor() { return sensor; }
    public void setSensor(SensorMovimiento sensor) { this.sensor = sensor; }

    @Override
    public void ejecutar(Automatizacion automatizacion) {
    }
}
