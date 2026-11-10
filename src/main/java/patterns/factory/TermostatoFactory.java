package patterns.factory;

import model.Dispositivo;
import model.Habitacion;
import model.Termostato;
import patterns.state.EstadoApagado;

public class TermostatoFactory extends DispositivoFactory {

    public static final double MIN_TEMPERATURE = 5.0;
    public static final double MAX_TEMPERATURE = 35.0;
    public static final double DEFAULT_CURRENT_TEMPERATURE = 20.0;
    public static final double DEFAULT_TARGET_TEMPERATURE = 22.0;

    private double defaultCurrentTemperature;
    private double defaultTargetTemperature;

    public TermostatoFactory() {
        this(
                DEFAULT_CURRENT_TEMPERATURE,
                DEFAULT_TARGET_TEMPERATURE
        );
    }

    public TermostatoFactory(
            double defaultCurrentTemperature,
            double defaultTargetTemperature) {

        this.defaultCurrentTemperature =
                validateTemperature(
                        defaultCurrentTemperature,
                        "Default current temperature"
                );

        this.defaultTargetTemperature =
                validateTemperature(
                        defaultTargetTemperature,
                        "Default target temperature"
                );
    }

    @Override
    protected Dispositivo buildDevice(
            String id,
            String name,
            String brand,
            String model,
            Habitacion room) {

        return new Termostato(
                id,
                name,
                brand,
                model,
                new EstadoApagado(),
                room,
                defaultCurrentTemperature,
                defaultTargetTemperature
        );
    }

    @Override
    public String getSupportedType() {
        return "THERMOSTAT";
    }

    public double getDefaultCurrentTemperature() {
        return defaultCurrentTemperature;
    }

    public void setDefaultCurrentTemperature(
            double defaultCurrentTemperature) {

        this.defaultCurrentTemperature =
                validateTemperature(
                        defaultCurrentTemperature,
                        "Default current temperature"
                );
    }

    public double getDefaultTargetTemperature() {
        return defaultTargetTemperature;
    }

    public void setDefaultTargetTemperature(
            double defaultTargetTemperature) {

        this.defaultTargetTemperature =
                validateTemperature(
                        defaultTargetTemperature,
                        "Default target temperature"
                );
    }

    private static double validateTemperature(
            double temperature,
            String fieldName) {

        if (!Double.isFinite(temperature)) {
            throw new IllegalArgumentException(
                    fieldName + " must be a finite number."
            );
        }

        if (temperature < MIN_TEMPERATURE
                || temperature > MAX_TEMPERATURE) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be between "
                            + MIN_TEMPERATURE
                            + " and "
                            + MAX_TEMPERATURE
                            + " degrees Celsius."
            );
        }

        return temperature;
    }
}