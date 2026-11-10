package patterns.mediator;

import model.Automatizacion;

import java.util.List;
import java.util.Optional;

public interface MediadorHogar {

    void registerComponent(
            ComponenteHogar component);

    boolean removeComponent(
            String componentId);

    Optional<ComponenteHogar> findComponent(
            String componentId);

    List<ComponenteHogar> getComponents();

    void registerAutomation(
            String eventType,
            Automatizacion automation);

    boolean removeAutomation(
            String eventType,
            String automationId);

    List<Automatizacion> getAutomations(
            String eventType);

    int notifyEvent(
            ComponenteHogar source,
            String eventType);

    int getExecutedAutomationCount();

    String getLastCoordinationResult();
}