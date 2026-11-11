package controller;

import app.ApplicationContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Automatizacion;
import model.Dispositivo;
import model.SensorMovimiento;
import patterns.command.EncenderDispositivoCommand;
import patterns.facade.HogarFacade;
import patterns.factory.LuzFactory;
import patterns.factory.SensorFactory;
import patterns.mediator.ComponenteHogar;
import patterns.mediator.HogarMediator;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;
import patterns.strategy.EstrategiaPorMovimiento;

import java.util.List;

public class AutomatizacionesController {

    private static final String SENSOR_ID =
            "sensor-hall";

    private static final String LIGHT_ID =
            "light-living-room";

    private static final String COMPONENT_ID =
            "component-sensor-hall";

    private static final String AUTOMATION_ID =
            "automation-motion-light";

    private static final String MOTION_EVENT =
            "MOTION_DETECTED";

    private final ApplicationContext applicationContext =
            ApplicationContext.getInstance();

    private final HogarFacade homeFacade =
            applicationContext.getHogarFacade();

    private final HogarMediator homeMediator =
            applicationContext.getHogarMediator();

    private final HistorialObserver historyObserver =
            applicationContext.getHistoryObserver();

    private final NotificacionObserver notificationObserver =
            applicationContext.getNotificationObserver();

    private SensorMovimiento motionSensor;
    private Dispositivo targetLight;
    private ComponenteHogar sensorComponent;
    private Automatizacion motionAutomation;

    @FXML
    private Label automationStatusLabel;

    @FXML
    private Label sensorStatusLabel;

    @FXML
    private Label lightStatusLabel;

    @FXML
    private Label mediatorStatusLabel;

    @FXML
    private Label observerStatusLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button simulateMotionButton;

    @FXML
    private Button clearMotionButton;

    @FXML
    private Button resetLightButton;

    @FXML
    private void initialize() {
        try {
            prepareDemonstrationDevices();
            prepareMediatorComponent();
            prepareMotionAutomation();
            refreshView();

            showFeedback(
                    "Motion automation is ready.",
                    false
            );
        } catch (RuntimeException exception) {
            disableActions();

            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    private void prepareDemonstrationDevices() {
        targetLight =
                homeFacade.findDeviceById(LIGHT_ID)
                        .orElseGet(
                                this::createAndRegisterLight
                        );

        Dispositivo sensorDevice =
                homeFacade.findDeviceById(SENSOR_ID)
                        .orElseGet(
                                this::createAndRegisterSensor
                        );

        if (!(sensorDevice
                instanceof SensorMovimiento sensor)) {

            throw new IllegalStateException(
                    "The registered device with id '"
                            + SENSOR_ID
                            + "' is not a motion sensor."
            );
        }

        motionSensor = sensor;
    }

    private Dispositivo createAndRegisterLight() {
        Dispositivo light =
                new LuzFactory(
                        70,
                        "WARM WHITE"
                ).createDevice(
                        LIGHT_ID,
                        "Living Room Light",
                        "Philips",
                        "Hue",
                        null
                );

        return homeFacade.registerDevice(light);
    }

    private Dispositivo createAndRegisterSensor() {
        Dispositivo sensor =
                new SensorFactory(
                        5
                ).createDevice(
                        SENSOR_ID,
                        "Hall Motion Sensor",
                        "Aqara",
                        "P1",
                        null
                );

        return homeFacade.registerDevice(sensor);
    }

    private void prepareMediatorComponent() {
        sensorComponent =
                homeMediator
                        .findComponent(COMPONENT_ID)
                        .orElseGet(
                                this::createMediatorComponent
                        );

        if (sensorComponent.getDevice()
                != motionSensor) {

            throw new IllegalStateException(
                    "The mediator component is connected "
                            + "to a different sensor."
            );
        }
    }

    private ComponenteHogar createMediatorComponent() {
        ComponenteHogar component =
                new ComponenteHogar(
                        COMPONENT_ID,
                        motionSensor
                );

        component.connectToMediator(
                homeMediator
        );

        return component;
    }

    private void prepareMotionAutomation() {
        List<Automatizacion> registeredAutomations =
                homeMediator.getAutomations(
                        MOTION_EVENT
                );

        motionAutomation =
                registeredAutomations
                        .stream()
                        .filter(automation ->
                                AUTOMATION_ID.equals(
                                        automation.getId()
                                )
                        )
                        .findFirst()
                        .orElseGet(
                                this::createAndRegisterAutomation
                        );
    }

    private Automatizacion createAndRegisterAutomation() {
        Automatizacion automation =
                new Automatizacion();

        automation.setId(AUTOMATION_ID);

        automation.setNombre(
                "Hall motion light"
        );

        automation.setDescripcion(
                "Turns on the living room light "
                        + "when the hall sensor detects motion."
        );

        automation.addCommand(
                new EncenderDispositivoCommand(
                        targetLight
                )
        );

        automation.setEstrategia(
                new EstrategiaPorMovimiento(
                        motionSensor
                )
        );

        homeMediator.registerAutomation(
                MOTION_EVENT,
                automation
        );

        return automation;
    }

    @FXML
    private void simulateMotion() {
        try {
            ensureSensorIsActive();

            boolean lightWasActive =
                    targetLight.isActivo();

            motionSensor.setMovimientoDetectado(
                    true
            );

            int executedAutomations =
                    sensorComponent.reportEvent(
                            MOTION_EVENT
                    );

            boolean lightChanged =
                    !lightWasActive
                            && targetLight.isActivo();

            homeFacade.publishDeviceEvent(
                    motionSensor,
                    "Motion was detected by the hall sensor."
            );

            if (lightChanged) {
                homeFacade.publishDeviceEvent(
                        targetLight,
                        "Living room light was turned on "
                                + "by the motion automation."
                );
            }

            showFeedback(
                    buildExecutionFeedback(
                            executedAutomations,
                            lightChanged
                    ),
                    false
            );

            refreshView();
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void clearMotion() {
        try {
            motionSensor.setMovimientoDetectado(
                    false
            );

            homeFacade.publishDeviceEvent(
                    motionSensor,
                    "Motion detection was cleared."
            );

            showFeedback(
                    "Motion detection was cleared.",
                    false
            );

            refreshView();
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void resetLight() {
        try {
            boolean changed =
                    homeFacade.turnOffDevice(
                            targetLight.getId()
                    );

            showFeedback(
                    changed
                            ? "The demonstration light was turned off."
                            : "The demonstration light was already off.",
                    false
            );

            refreshView();
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void refreshAutomation() {
        refreshView();

        showFeedback(
                "Automation information was refreshed.",
                false
        );
    }

    private void ensureSensorIsActive() {
        if (motionSensor.isActivo()) {
            return;
        }

        boolean changed =
                homeFacade.turnOnDevice(
                        motionSensor.getId()
                );

        if (!changed
                && !motionSensor.isActivo()) {

            throw new IllegalStateException(
                    "The motion sensor could not be turned on."
            );
        }
    }

    private String buildExecutionFeedback(
            int executedAutomations,
            boolean lightChanged) {

        if (executedAutomations == 0) {
            return "The mediator did not execute "
                    + "an automation.";
        }

        if (lightChanged) {
            return "Motion detected. The mediator executed "
                    + executedAutomations
                    + " automation(s) and the light was turned on.";
        }

        return "Motion detected. The mediator executed "
                + executedAutomations
                + " automation(s), but the light was already on.";
    }

    private void refreshView() {
        automationStatusLabel.setText(
                "Automation: "
                        + motionAutomation.getNombre()
                        + "\nResult: "
                        + motionAutomation
                        .getLastExecutionResult()
                        + "\nCommand executions: "
                        + motionAutomation
                        .getExecutionCount()
        );

        sensorStatusLabel.setText(
                "Sensor: "
                        + motionSensor.getNombre()
                        + "\nPower state: "
                        + motionSensor
                        .getEstado()
                        .getStateName()
                        + "\nMotion detected: "
                        + yesNo(
                        motionSensor
                                .isMovimientoDetectado()
                )
                        + "\nSensitivity: "
                        + motionSensor.getSensibilidad()
        );

        lightStatusLabel.setText(
                "Target device: "
                        + targetLight.getNombre()
                        + "\nState: "
                        + targetLight.getStatusDescription()
        );

        mediatorStatusLabel.setText(
                "Mediator components: "
                        + homeMediator.getComponentCount()
                        + "\nRegistered automations: "
                        + homeMediator.getAutomationCount()
                        + "\nExecuted automations: "
                        + homeMediator
                        .getExecutedAutomationCount()
                        + "\nLast result: "
                        + homeMediator
                        .getLastCoordinationResult()
        );

        observerStatusLabel.setText(
                "Recorded events: "
                        + historyObserver.getEventCount()
                        + "\nNotifications: "
                        + notificationObserver
                        .getNotificationCount()
                        + "\nLast notification: "
                        + notificationObserver
                        .getLastNotification()
                        .orElse(
                                "No notifications recorded."
                        )
        );
    }

    private void disableActions() {
        if (simulateMotionButton != null) {
            simulateMotionButton.setDisable(true);
        }

        if (clearMotionButton != null) {
            clearMotionButton.setDisable(true);
        }

        if (resetLightButton != null) {
            resetLightButton.setDisable(true);
        }
    }

    private void showFeedback(
            String message,
            boolean error) {

        feedbackLabel.setText(message);

        feedbackLabel.setStyle(
                error
                        ? "-fx-text-fill: #b91c1c;"
                        : "-fx-text-fill: #047857;"
        );
    }

    private static String yesNo(boolean value) {
        return value ? "YES" : "NO";
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