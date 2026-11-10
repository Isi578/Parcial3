package patterns.factory;

import model.Dispositivo;
import model.Habitacion;
import model.SensorMovimiento;
import patterns.state.EstadoApagado;

public class SensorFactory extends DispositivoFactory {

    public static final int MIN_SENSITIVITY = 1;
    public static final int MAX_SENSITIVITY = 10;

    private int defaultSensitivity;

    public SensorFactory() {
        this(5);
    }

    public SensorFactory(int defaultSensitivity) {
        this.defaultSensitivity = requireRange(
                defaultSensitivity,
                MIN_SENSITIVITY,
                MAX_SENSITIVITY,
                "Default sensitivity"
        );
    }

    @Override
    protected Dispositivo buildDevice(
            String id,
            String name,
            String brand,
            String model,
            Habitacion room) {

        return new SensorMovimiento(
                id,
                name,
                brand,
                model,
                new EstadoApagado(),
                room,
                false,
                defaultSensitivity
        );
    }

    @Override
    public String getSupportedType() {
        return "MOTION_SENSOR";
    }

    public int getDefaultSensitivity() {
        return defaultSensitivity;
    }

    public void setDefaultSensitivity(
            int defaultSensitivity) {

        this.defaultSensitivity = requireRange(
                defaultSensitivity,
                MIN_SENSITIVITY,
                MAX_SENSITIVITY,
                "Default sensitivity"
        );
    }
}
