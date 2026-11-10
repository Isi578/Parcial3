package patterns.observer;

import model.Evento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HistorialObserver implements Observador {

    private final List<Evento> eventHistory =
            new ArrayList<>();

    @Override
    public void actualizar(Evento evento) {
        eventHistory.add(
                Objects.requireNonNull(
                        evento,
                        "Event cannot be null."
                )
        );
    }

    public List<Evento> getEventHistory() {
        return Collections.unmodifiableList(
                eventHistory
        );
    }

    public Optional<Evento> getLastEvent() {
        if (eventHistory.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                eventHistory.get(
                        eventHistory.size() - 1
                )
        );
    }

    public int getEventCount() {
        return eventHistory.size();
    }

    public boolean isEmpty() {
        return eventHistory.isEmpty();
    }

    public boolean containsEventId(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            return false;
        }

        String normalizedId = eventId.trim();

        return eventHistory.stream()
                .anyMatch(event ->
                        normalizedId.equals(
                                event.getId()
                        )
                );
    }

    public List<Evento> getEventsForDevice(
            String deviceId) {

        if (deviceId == null || deviceId.isBlank()) {
            return List.of();
        }

        String normalizedId = deviceId.trim();

        return eventHistory.stream()
                .filter(event ->
                        event.getDispositivo() != null)
                .filter(event ->
                        normalizedId.equals(
                                event.getDispositivo().getId()
                        )
                )
                .toList();
    }

    public void clearHistory() {
        eventHistory.clear();
    }
}
