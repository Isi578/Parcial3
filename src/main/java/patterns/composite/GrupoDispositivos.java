package patterns.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class GrupoDispositivos
        implements ComponenteHogar {

    private final String id;
    private String name;
    private final List<ComponenteHogar> components =
            new ArrayList<>();

    public GrupoDispositivos(String id, String name) {
        this.id = requireText(id, "Group id");
        this.name = requireText(name, "Group name");
    }

    public void addComponent(ComponenteHogar component) {
        Objects.requireNonNull(
                component,
                "Home component cannot be null."
        );

        if (component == this) {
            throw new IllegalArgumentException(
                    "A group cannot contain itself."
            );
        }

        if (findDirectComponentById(component.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "A component with id '"
                            + component.getId()
                            + "' already exists in group '"
                            + name
                            + "'."
            );
        }

        if (containsComponent(component, this)) {
            throw new IllegalArgumentException(
                    "Adding this component would create a circular group."
            );
        }

        components.add(component);
    }

    public ComponenteHogar removeComponent(String componentId) {
        ComponenteHogar component =
                findDirectComponentById(componentId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Component with id '"
                                                + componentId
                                                + "' was not found in group '"
                                                + name
                                                + "'."
                                )
                        );

        components.remove(component);
        return component;
    }

    public Optional<ComponenteHogar> findDirectComponentById(
            String componentId) {

        if (componentId == null || componentId.isBlank()) {
            return Optional.empty();
        }

        String normalizedId = componentId.trim();

        return components.stream()
                .filter(component ->
                        normalizedId.equals(component.getId()))
                .findFirst();
    }

    public Optional<ComponenteHogar> findComponentById(
            String componentId) {

        Optional<ComponenteHogar> directComponent =
                findDirectComponentById(componentId);

        if (directComponent.isPresent()) {
            return directComponent;
        }

        for (ComponenteHogar component : components) {
            if (component instanceof GrupoDispositivos group) {
                Optional<ComponenteHogar> nestedComponent =
                        group.findComponentById(componentId);

                if (nestedComponent.isPresent()) {
                    return nestedComponent;
                }
            }
        }

        return Optional.empty();
    }

    public List<ComponenteHogar> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public void turnOn() {
        components.forEach(ComponenteHogar::turnOn);
    }

    @Override
    public void turnOff() {
        components.forEach(ComponenteHogar::turnOff);
    }

    @Override
    public boolean isActive() {
        return !components.isEmpty()
                && components.stream()
                .allMatch(ComponenteHogar::isActive);
    }

    public boolean hasAnyActiveDevice() {
        return components.stream()
                .anyMatch(component ->
                        component.getActiveDeviceCount() > 0);
    }

    @Override
    public int getDeviceCount() {
        return components.stream()
                .mapToInt(ComponenteHogar::getDeviceCount)
                .sum();
    }

    @Override
    public int getActiveDeviceCount() {
        return components.stream()
                .mapToInt(
                        ComponenteHogar::getActiveDeviceCount
                )
                .sum();
    }

    @Override
    public String getStatusDescription() {
        return getActiveDeviceCount()
                + " of "
                + getDeviceCount()
                + " devices are active";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireText(name, "Group name");
    }

    private static boolean containsComponent(
            ComponenteHogar possibleParent,
            GrupoDispositivos expectedChild) {

        if (!(possibleParent instanceof GrupoDispositivos group)) {
            return false;
        }

        if (group == expectedChild) {
            return true;
        }

        for (ComponenteHogar nestedComponent :
                group.components) {

            if (containsComponent(
                    nestedComponent,
                    expectedChild)) {

                return true;
            }
        }

        return false;
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

    @Override
    public String toString() {
        return name + " - " + getStatusDescription();
    }
}
