package model;

import patterns.state.EstadoDispositivo;

public class Termostato extends Dispositivo {

    public static final double MIN_TEMPERATURE = 5.0;
    public static final double MAX_TEMPERATURE = 35.0;

    private double temperaturaActual;
    private double temperaturaObjetivo;

    public Termostato() {
        super();
        this.temperaturaActual = 20.0;
        this.temperaturaObjetivo = 22.0;
    }

    public Termostato(
            String id,
            String nombre,
            String marca,
            String modelo,
            EstadoDispositivo estado,
            Habitacion habitacion,
            double temperaturaActual,
            double temperaturaObjetivo) {

        super(
                id,
                nombre,
                marca,
                modelo,
                estado,
                habitacion
        );

        setTemperaturaActual(temperaturaActual);
        setTemperaturaObjetivo(temperaturaObjetivo);
    }

    public boolean hasReachedTargetTemperature() {
        return Double.compare(
                temperaturaActual,
                temperaturaObjetivo
        ) >= 0;
    }

    public boolean isAboveTemperature(
            double temperature) {

        validateTemperature(
                temperature,
                "Temperature threshold"
        );

        return Double.compare(
                temperaturaActual,
                temperature
        ) > 0;
    }

    public boolean isAtOrAboveTemperature(
            double temperature) {

        validateTemperature(
                temperature,
                "Temperature threshold"
        );

        return Double.compare(
                temperaturaActual,
                temperature
        ) >= 0;
    }

    public boolean isBelowTemperature(
            double temperature) {

        validateTemperature(
                temperature,
                "Temperature threshold"
        );

        return Double.compare(
                temperaturaActual,
                temperature
        ) < 0;
    }

    public double getTemperatureDifference() {
        return temperaturaObjetivo
                - temperaturaActual;
    }

    public void increaseTargetTemperature(
            double amount) {

        validatePositiveAmount(amount);

        setTemperaturaObjetivo(
                temperaturaObjetivo + amount
        );
    }

    public void decreaseTargetTemperature(
            double amount) {

        validatePositiveAmount(amount);

        setTemperaturaObjetivo(
                temperaturaObjetivo - amount
        );
    }

    @Override
    public String getDeviceType() {
        return "THERMOSTAT";
    }

    @Override
    public String getStatusDescription() {
        return getEstado().getStateName()
                + " - Current: "
                + formatTemperature(temperaturaActual)
                + " °C - Target: "
                + formatTemperature(temperaturaObjetivo)
                + " °C";
    }

    public double getTemperaturaActual() {
        return temperaturaActual;
    }

    public void setTemperaturaActual(
            double temperaturaActual) {

        this.temperaturaActual =
                validateTemperature(
                        temperaturaActual,
                        "Current temperature"
                );
    }

    public double getTemperaturaObjetivo() {
        return temperaturaObjetivo;
    }

    public void setTemperaturaObjetivo(
            double temperaturaObjetivo) {

        this.temperaturaObjetivo =
                validateTemperature(
                        temperaturaObjetivo,
                        "Target temperature"
                );
    }

    private static double validateTemperature(
            double temperature,
            String fieldName) {

        if (!Double.isFinite(temperature)) {
            throw new IllegalArgumentException(
                    fieldName
                            + " must be a finite number."
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

    private static void validatePositiveAmount(
            double amount) {

        if (!Double.isFinite(amount)
                || amount <= 0.0) {

            throw new IllegalArgumentException(
                    "Temperature adjustment must be "
                            + "a positive finite number."
            );
        }
    }

    private static String formatTemperature(
            double temperature) {

        return String.format(
                java.util.Locale.US,
                "%.1f",
                temperature
        );
    }
}