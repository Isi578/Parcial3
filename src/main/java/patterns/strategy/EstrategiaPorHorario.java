package patterns.strategy;

import model.Automatizacion;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class EstrategiaPorHorario
        implements EstrategiaAutomatizacion {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    private LocalTime scheduledTime;

    public EstrategiaPorHorario() {
        this(LocalTime.now());
    }

    public EstrategiaPorHorario(String hora) {
        setHora(hora);
    }

    public EstrategiaPorHorario(
            LocalTime scheduledTime) {

        this.scheduledTime =
                Objects.requireNonNull(
                        scheduledTime,
                        "Scheduled time cannot be null."
                ).withSecond(0).withNano(0);
    }

    @Override
    public void ejecutar(
            Automatizacion automatizacion) {

        Objects.requireNonNull(
                automatizacion,
                "Automation cannot be null."
        );

        LocalTime currentTime =
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0);

        if (shouldExecuteAt(currentTime)) {
            int executedCommands =
                    automatizacion.executeCommands();

            automatizacion.setLastExecutionResult(
                    "Schedule matched at "
                            + getHora()
                            + ". "
                            + executedCommands
                            + " command(s) executed."
            );

            return;
        }

        automatizacion.setLastExecutionResult(
                "Schedule did not match. Expected "
                        + getHora()
                        + ", current time "
                        + currentTime.format(
                        TIME_FORMATTER
                )
                        + "."
        );
    }

    public boolean shouldExecuteAt(
            LocalTime currentTime) {

        LocalTime normalizedCurrentTime =
                Objects.requireNonNull(
                        currentTime,
                        "Current time cannot be null."
                ).withSecond(0).withNano(0);

        return scheduledTime.equals(
                normalizedCurrentTime
        );
    }

    public String getHora() {
        return scheduledTime.format(
                TIME_FORMATTER
        );
    }

    public void setHora(String hora) {
        if (hora == null || hora.isBlank()) {
            throw new IllegalArgumentException(
                    "Scheduled time cannot be empty."
            );
        }

        try {
            this.scheduledTime =
                    LocalTime.parse(
                            hora.trim(),
                            TIME_FORMATTER
                    );
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Scheduled time must use HH:mm format.",
                    exception
            );
        }
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(
            LocalTime scheduledTime) {

        this.scheduledTime =
                Objects.requireNonNull(
                        scheduledTime,
                        "Scheduled time cannot be null."
                ).withSecond(0).withNano(0);
    }
}