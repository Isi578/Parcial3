package patterns.strategy;

import model.Automatizacion;
import model.SensorMovimiento;

import java.util.Objects;

public class EstrategiaPorMovimiento
        implements EstrategiaAutomatizacion {

    private SensorMovimiento sensor;
    private boolean executeOnlyWhenSensorIsActive;

    public EstrategiaPorMovimiento() {
    }

    public EstrategiaPorMovimiento(
            SensorMovimiento sensor) {

        this(sensor, true);
    }

    public EstrategiaPorMovimiento(
            SensorMovimiento sensor,
            boolean executeOnlyWhenSensorIsActive) {

        this.sensor = Objects.requireNonNull(
                sensor,
                "Motion sensor cannot be null."
        );

        this.executeOnlyWhenSensorIsActive =
                executeOnlyWhenSensorIsActive;
    }

    @Override
    public void ejecutar(
            Automatizacion automatizacion) {

        Objects.requireNonNull(
                automatizacion,
                "Automation cannot be null."
        );

        SensorMovimiento currentSensor =
                Objects.requireNonNull(
                        sensor,
                        "Motion sensor is not configured."
                );

        if (executeOnlyWhenSensorIsActive
                && !currentSensor.isActivo()) {

            automatizacion.setLastExecutionResult(
                    "Motion strategy was not executed "
                            + "because sensor '"
                            + currentSensor.getNombre()
                            + "' is turned off."
            );

            return;
        }

        if (!currentSensor.isMovimientoDetectado()) {
            automatizacion.setLastExecutionResult(
                    "No motion was detected by sensor '"
                            + currentSensor.getNombre()
                            + "'."
            );

            return;
        }

        int executedCommands =
                automatizacion.executeCommands();

        automatizacion.setLastExecutionResult(
                "Motion detected by sensor '"
                        + currentSensor.getNombre()
                        + "'. "
                        + executedCommands
                        + " command(s) executed."
        );
    }

    public SensorMovimiento getSensor() {
        return sensor;
    }

    public void setSensor(
            SensorMovimiento sensor) {

        this.sensor = Objects.requireNonNull(
                sensor,
                "Motion sensor cannot be null."
        );
    }

    public boolean isExecuteOnlyWhenSensorIsActive() {
        return executeOnlyWhenSensorIsActive;
    }

    public void setExecuteOnlyWhenSensorIsActive(
            boolean executeOnlyWhenSensorIsActive) {

        this.executeOnlyWhenSensorIsActive =
                executeOnlyWhenSensorIsActive;
    }
}
