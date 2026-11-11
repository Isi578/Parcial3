package patterns.observer;

import model.Dispositivo;
import model.Evento;
import model.Notificacion;
import util.enums.TipoEvento;
import util.enums.TipoNotificacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class NotificacionObserver implements Observador {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            );

    private final List<String> notificationMessages =
            new ArrayList<>();

    private final List<Notificacion> notifications =
            new ArrayList<>();

    private boolean enabled = true;

    @Override
    public void actualizar(Evento evento) {
        Objects.requireNonNull(
                evento,
                "Event cannot be null."
        );

        if (!enabled) {
            return;
        }

        String message = buildNotificationMessage(evento);
        notificationMessages.add(message);
        notifications.add(new Notificacion(
                UUID.randomUUID().toString(),
                evento.getTipo() == TipoEvento.MOVIMIENTO
                        ? TipoNotificacion.ALERTA
                        : TipoNotificacion.INFORMACION,
                message,
                evento.getFecha() == null
                        ? LocalDateTime.now()
                        : evento.getFecha()
        ));
    }

    public List<String> getNotificationMessages() {
        return Collections.unmodifiableList(
                notificationMessages
        );
    }

    public List<Notificacion> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public Optional<String> getLastNotification() {
        if (notificationMessages.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                notificationMessages.get(
                        notificationMessages.size() - 1
                )
        );
    }

    public int getNotificationCount() {
        return notificationMessages.size();
    }

    public boolean hasNotifications() {
        return !notificationMessages.isEmpty();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void clearNotifications() {
        notificationMessages.clear();
        notifications.clear();
    }

    private static String buildNotificationMessage(
            Evento evento) {

        String timestamp = formatDate(
                evento.getFecha()
        );

        String eventType = evento.getTipo() == null
                ? "GENERAL"
                : evento.getTipo().name();

        String description =
                normalizeDescription(
                        evento.getDescripcion()
                );

        String deviceDescription =
                describeDevice(
                        evento.getDispositivo()
                );

        return "["
                + timestamp
                + "] "
                + eventType
                + " | "
                + deviceDescription
                + " | "
                + description;
    }

    private static String formatDate(
            LocalDateTime dateTime) {

        LocalDateTime effectiveDate =
                dateTime == null
                        ? LocalDateTime.now()
                        : dateTime;

        return effectiveDate.format(
                TIME_FORMATTER
        );
    }

    private static String describeDevice(
            Dispositivo device) {

        if (device == null) {
            return "HOME";
        }

        String deviceId =
                normalizeText(
                        device.getId(),
                        "NO_ID"
                );

        String deviceName =
                normalizeText(
                        device.getNombre(),
                        "Unnamed device"
                );

        return deviceName
                + " ("
                + deviceId
                + ")";
    }

    private static String normalizeDescription(
            String description) {

        return normalizeText(
                description,
                "No description"
        );
    }

    private static String normalizeText(
            String value,
            String defaultValue) {

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value.trim();
    }
}
