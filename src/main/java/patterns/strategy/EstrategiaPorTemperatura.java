package patterns.strategy;

import model.Automatizacion;
import model.Termostato;

import java.util.Locale;
import java.util.Objects;

public class EstrategiaPorTemperatura
        implements EstrategiaAutomatizacion {

    private Termostato thermostat;
    private double temperaturaUmbral;

    private boolean executeWhenAtOrAboveThreshold =
            true;

    private boolean executeOnlyWhenThermostatIsActive =
            true;

    public EstrategiaPorTemperatura() {
        this.temperaturaUmbral = 25.0;
    }

    public EstrategiaPorTemperatura(
            double temperaturaUmbral) {

        setTemperaturaUmbral(
                temperaturaUmbral
        );
    }

    public EstrategiaPorTemperatura(
            Termostato thermostat,
            double temperaturaUmbral) {

        this(
                thermostat,
                temperaturaUmbral,
                true,
                true
        );
    }

    public EstrategiaPorTemperatura(
            Termostato thermostat,
            double temperaturaUmbral,
            boolean executeWhenAtOrAboveThreshold,
            boolean executeOnlyWhenThermostatIsActive) {

        setThermostat(thermostat);

        setTemperaturaUmbral(
                temperaturaUmbral
        );

        this.executeWhenAtOrAboveThreshold =
                executeWhenAtOrAboveThreshold;

        this.executeOnlyWhenThermostatIsActive =
                executeOnlyWhenThermostatIsActive;
    }

    @Override
    public void ejecutar(
            Automatizacion automatizacion) {

        Objects.requireNonNull(
                automatizacion,
                "Automation cannot be null."
        );

        Termostato currentThermostat =
                Objects.requireNonNull(
                        thermostat,
                        "Thermostat is not configured."
                );

        if (executeOnlyWhenThermostatIsActive
                && !currentThermostat.isActivo()) {

            automatizacion.setLastExecutionResult(
                    "Temperature strategy was not executed "
                            + "because thermostat '"
                            + currentThermostat.getNombre()
                            + "' is turned off."
            );

            return;
        }

        double currentTemperature =
                currentThermostat
                        .getTemperaturaActual();

        boolean thresholdMatched =
                shouldExecuteAt(
                        currentTemperature
                );

        if (!thresholdMatched) {
            automatizacion.setLastExecutionResult(
                    buildNoMatchResult(
                            currentThermostat,
                            currentTemperature
                    )
            );

            return;
        }

        int executedCommands =
                automatizacion.executeCommands();

        automatizacion.setLastExecutionResult(
                "Temperature threshold matched for thermostat '"
                        + currentThermostat.getNombre()
                        + "'. Current temperature: "
                        + formatTemperature(
                        currentTemperature
                )
                        + " °C, threshold: "
                        + formatTemperature(
                        temperaturaUmbral
                )
                        + " °C. "
                        + executedCommands
                        + " command(s) executed."
        );
    }

    public boolean shouldExecuteAt(
            double currentTemperature) {

        validateTemperature(
                currentTemperature,
                "Current temperature"
        );

        if (executeWhenAtOrAboveThreshold) {
            return Double.compare(
                    currentTemperature,
                    temperaturaUmbral
            ) >= 0;
        }

        return Double.compare(
                currentTemperature,
                temperaturaUmbral
        ) <= 0;
    }

    private String buildNoMatchResult(
            Termostato currentThermostat,
            double currentTemperature) {

        String expectedCondition =
                executeWhenAtOrAboveThreshold
                        ? "at or above"
                        : "at or below";

        return "Temperature strategy did not execute for thermostat '"
                + currentThermostat.getNombre()
                + "'. Current temperature: "
                + formatTemperature(
                currentTemperature
        )
                + " °C. Expected "
                + expectedCondition
                + " "
                + formatTemperature(
                temperaturaUmbral
        )
                + " °C.";
    }

    public Termostato getThermostat() {
        return thermostat;
    }

    public void setThermostat(
            Termostato thermostat) {

        this.thermostat =
                Objects.requireNonNull(
                        thermostat,
                        "Thermostat cannot be null."
                );
    }

    public double getTemperaturaUmbral() {
        return temperaturaUmbral;
    }

    public void setTemperaturaUmbral(
            double temperaturaUmbral) {

        this.temperaturaUmbral =
                validateTemperature(
                        temperaturaUmbral,
                        "Temperature threshold"
                );
    }

    public boolean isExecuteWhenAtOrAboveThreshold() {
        return executeWhenAtOrAboveThreshold;
    }

    public void setExecuteWhenAtOrAboveThreshold(
            boolean executeWhenAtOrAboveThreshold) {

        this.executeWhenAtOrAboveThreshold =
                executeWhenAtOrAboveThreshold;
    }

    public boolean isExecuteOnlyWhenThermostatIsActive() {
        return executeOnlyWhenThermostatIsActive;
    }

    public void setExecuteOnlyWhenThermostatIsActive(
            boolean executeOnlyWhenThermostatIsActive) {

        this.executeOnlyWhenThermostatIsActive =
                executeOnlyWhenThermostatIsActive;
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

        if (temperature < Termostato.MIN_TEMPERATURE
                || temperature
                > Termostato.MAX_TEMPERATURE) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be between "
                            + Termostato.MIN_TEMPERATURE
                            + " and "
                            + Termostato.MAX_TEMPERATURE
                            + " degrees Celsius."
            );
        }

        return temperature;
    }

    private static String formatTemperature(
            double temperature) {

        return String.format(
                Locale.US,
                "%.1f",
                temperature
        );
    }
}
