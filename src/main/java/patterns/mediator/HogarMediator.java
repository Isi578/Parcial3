package patterns.mediator;

import model.Automatizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HogarMediator
        implements MediadorHogar {

    private final Map<String, ComponenteHogar> components =
            new LinkedHashMap<>();

    private final Map<String, List<Automatizacion>>
            automationsByEvent =
            new LinkedHashMap<>();

    private int executedAutomationCount;

    private String lastCoordinationResult =
            "No event has been coordinated.";

    @Override
    public void registerComponent(
            ComponenteHogar component) {

        Objects.requireNonNull(
                component,
                "Home component cannot be null."
        );

        ComponenteHogar existingComponent =
                components.get(
                        component.getId()
                );

        if (existingComponent != null
                && existingComponent != component) {

            throw new IllegalArgumentException(
                    "A component with id '"
                            + component.getId()
                            + "' is already registered."
            );
        }

        components.put(
                component.getId(),
                component
        );
    }

    @Override
    public boolean removeComponent(
            String componentId) {

        String normalizedId =
                requireText(
                        componentId,
                        "Component id"
                );

        return components.remove(
                normalizedId
        ) != null;
    }

    @Override
    public Optional<ComponenteHogar> findComponent(
            String componentId) {

        String normalizedId =
                requireText(
                        componentId,
                        "Component id"
                );

        return Optional.ofNullable(
                components.get(normalizedId)
        );
    }

    @Override
    public List<ComponenteHogar> getComponents() {
        return List.copyOf(
                components.values()
        );
    }

    @Override
    public void registerAutomation(
            String eventType,
            Automatizacion automation) {

        String normalizedEventType =
                normalizeEventType(eventType);

        Automatizacion validAutomation =
                Objects.requireNonNull(
                        automation,
                        "Automation cannot be null."
                );

        requireAutomationId(validAutomation);

        List<Automatizacion> eventAutomations =
                automationsByEvent.computeIfAbsent(
                        normalizedEventType,
                        key -> new ArrayList<>()
                );

        boolean alreadyRegistered =
                eventAutomations.stream()
                        .anyMatch(currentAutomation ->
                                currentAutomation
                                        .getId()
                                        .equals(
                                                validAutomation
                                                        .getId()
                                        )
                        );

        if (alreadyRegistered) {
            throw new IllegalArgumentException(
                    "Automation with id '"
                            + validAutomation.getId()
                            + "' is already registered for event '"
                            + normalizedEventType
                            + "'."
            );
        }

        eventAutomations.add(validAutomation);
    }

    @Override
    public boolean removeAutomation(
            String eventType,
            String automationId) {

        String normalizedEventType =
                normalizeEventType(eventType);

        String normalizedAutomationId =
                requireText(
                        automationId,
                        "Automation id"
                );

        List<Automatizacion> eventAutomations =
                automationsByEvent.get(
                        normalizedEventType
                );

        if (eventAutomations == null) {
            return false;
        }

        boolean removed =
                eventAutomations.removeIf(
                        automation ->
                                normalizedAutomationId.equals(
                                        automation.getId()
                                )
                );

        if (eventAutomations.isEmpty()) {
            automationsByEvent.remove(
                    normalizedEventType
            );
        }

        return removed;
    }

    @Override
    public List<Automatizacion> getAutomations(
            String eventType) {

        String normalizedEventType =
                normalizeEventType(eventType);

        List<Automatizacion> eventAutomations =
                automationsByEvent.get(
                        normalizedEventType
                );

        if (eventAutomations == null) {
            return List.of();
        }

        return Collections.unmodifiableList(
                new ArrayList<>(eventAutomations)
        );
    }

    @Override
    public int notifyEvent(
            ComponenteHogar source,
            String eventType) {

        ComponenteHogar validSource =
                Objects.requireNonNull(
                        source,
                        "Event source cannot be null."
                );

        validateRegisteredSource(
                validSource
        );

        String normalizedEventType =
                normalizeEventType(eventType);

        List<Automatizacion> eventAutomations =
                getAutomations(
                        normalizedEventType
                );

        if (eventAutomations.isEmpty()) {
            lastCoordinationResult =
                    "No automation is registered for event '"
                            + normalizedEventType
                            + "' reported by component '"
                            + validSource.getId()
                            + "'.";

            return 0;
        }

        int executedForCurrentEvent = 0;
        List<String> failures =
                new ArrayList<>();

        for (Automatizacion automation :
                eventAutomations) {

            try {
                int previousExecutionCount =
                        automation.getExecutionCount();
                automation.executeStrategy();
                if (automation.getExecutionCount()
                        > previousExecutionCount) {
                    executedForCurrentEvent++;
                }
            } catch (RuntimeException exception) {
                failures.add(
                        automation.getId()
                                + ": "
                                + getErrorMessage(
                                exception
                        )
                );
            }
        }

        executedAutomationCount +=
                executedForCurrentEvent;

        lastCoordinationResult =
                buildCoordinationResult(
                        validSource,
                        normalizedEventType,
                        executedForCurrentEvent,
                        failures
                );

        return executedForCurrentEvent;
    }

    @Override
    public int getExecutedAutomationCount() {
        return executedAutomationCount;
    }

    @Override
    public String getLastCoordinationResult() {
        return lastCoordinationResult;
    }

    public int getComponentCount() {
        return components.size();
    }

    public int getRegisteredEventCount() {
        return automationsByEvent.size();
    }

    public int getAutomationCount() {
        return automationsByEvent
                .values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    public boolean hasAutomationsForEvent(
            String eventType) {

        return !getAutomations(
                eventType
        ).isEmpty();
    }

    public void clearAutomations() {
        automationsByEvent.clear();
    }

    public void clearComponents() {
        List<ComponenteHogar> registeredComponents =
                new ArrayList<>(
                        components.values()
                );

        components.clear();

        for (ComponenteHogar component :
                registeredComponents) {

            if (component.getMediator() == this) {
                component.disconnectFromMediator();
            }
        }
    }

    private void validateRegisteredSource(
            ComponenteHogar source) {

        ComponenteHogar registeredComponent =
                components.get(
                        source.getId()
                );

        if (registeredComponent != source) {
            throw new IllegalArgumentException(
                    "The event source is not registered "
                            + "in this mediator."
            );
        }
    }

    private static String buildCoordinationResult(
            ComponenteHogar source,
            String eventType,
            int executedAutomationCount,
            List<String> failures) {

        StringBuilder result =
                new StringBuilder();

        result.append("Event '")
                .append(eventType)
                .append("' from component '")
                .append(source.getId())
                .append("' executed ")
                .append(executedAutomationCount)
                .append(" automation(s).");

        if (!failures.isEmpty()) {
            result.append(" Failures: ")
                    .append(
                            String.join(
                                    " | ",
                                    failures
                            )
                    );
        }

        return result.toString();
    }

    private static void requireAutomationId(
            Automatizacion automation) {

        requireText(
                automation.getId(),
                "Automation id"
        );
    }

    private static String normalizeEventType(
            String eventType) {

        return requireText(
                eventType,
                "Event type"
        ).toUpperCase();
    }

    private static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    private static String getErrorMessage(
            RuntimeException exception) {

        if (exception.getMessage() == null
                || exception.getMessage().isBlank()) {

            return exception
                    .getClass()
                    .getSimpleName();
        }

        return exception.getMessage();
    }
}
