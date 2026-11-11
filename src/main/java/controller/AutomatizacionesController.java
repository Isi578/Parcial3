package controller;

import app.ApplicationContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import util.enums.TipoEvento;

import java.util.List;
import java.io.IOException;

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

    private final ObservableList<Automatizacion> automations =
            FXCollections.observableArrayList();

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
    private ListView<Automatizacion> automationListView;

    @FXML
    private void initialize() {
        try {
            prepareDemonstrationDevices();
            prepareMediatorComponent();
            prepareMotionAutomation();
            configureAutomationList();
            refreshAutomationList();
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

    private void configureAutomationList() {
        automationListView.setItems(automations);
        automationListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Automatizacion automation, boolean empty) {
                super.updateItem(automation, empty);
                setText(empty || automation == null ? null
                        : automation.getNombre() + " | " + automation.getEstado());
            }
        });
        automationListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, previous, selected) -> {
                    if (selected != null) {
                        automationStatusLabel.setText(
                                "Selected rule: " + selected.getNombre()
                                        + "\nStatus: " + selected.getEstado()
                                        + "\nResult: " + selected.getLastExecutionResult()
                        );
                    }
                });
    }

    @FXML
    private void refreshAutomationList() {
        if (automationListView == null) {
            return;
        }
        String selectedId = automationListView.getSelectionModel().getSelectedItem() == null
                ? null
                : automationListView.getSelectionModel().getSelectedItem().getId();
        automations.setAll(applicationContext.getAutomatizacionService()
                .obtenerAutomatizaciones());
        automationListView.refresh();
        if (selectedId != null) {
            automations.stream()
                    .filter(automation -> selectedId.equals(automation.getId()))
                    .findFirst()
                    .ifPresent(automation -> automationListView.getSelectionModel().select(automation));
        }
    }

    @FXML
    private void addAutomation() {
        openAutomationForm(null);
    }

    @FXML
    private void editSelectedAutomation() {
        Automatizacion selected = automationListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFeedback("Select an automation first.", true);
        } else if (AUTOMATION_ID.equals(selected.getId())) {
            showFeedback("The built-in demonstration rule is read-only.", true);
        } else {
            openAutomationForm(selected);
        }
    }

    @FXML
    private void deleteSelectedAutomation() {
        Automatizacion selected = automationListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFeedback("Select an automation first.", true);
            return;
        }
        if (AUTOMATION_ID.equals(selected.getId())) {
            showFeedback("The built-in demonstration rule cannot be deleted.", true);
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete rule '" + selected.getNombre() + "'?",
                ButtonType.CANCEL,
                ButtonType.OK
        );
        confirmation.setHeaderText("Delete automation");
        confirmation.initOwner(automationListView.getScene().getWindow());
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            applicationContext.removeAutomation(MOTION_EVENT, selected.getId());
            refreshAutomationList();
            refreshView();
            showFeedback("Automation deleted.", false);
        } catch (RuntimeException exception) {
            showFeedback(getErrorMessage(exception), true);
        }
    }

    private void openAutomationForm(Automatizacion existing) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/automatizacion_form.fxml")
            );
            Parent root = loader.load();
            AutomatizacionFormController form = loader.getController();
            form.configure(existing);
            Stage stage = new Stage();
            stage.initOwner(automationListView.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(existing == null ? "Add automation" : "Edit automation");
            stage.setScene(new Scene(root));
            form.setOnSave(automation -> {
                try {
                    if (existing == null) {
                        applicationContext.registerAutomation(MOTION_EVENT, automation);
                    } else {
                        applicationContext.getAutomatizacionService()
                                .actualizarAutomatizacion(automation);
                    }
                    stage.close();
                    refreshAutomationList();
                    automationListView.getSelectionModel().select(automation);
                    refreshView();
                    showFeedback("Automation saved.", false);
                } catch (RuntimeException exception) {
                    form.showError(exception.getMessage());
                }
            });
            stage.showAndWait();
        } catch (IOException exception) {
            showFeedback("Could not open the automation form: " + exception.getMessage(), true);
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

        applicationContext.registerAutomation(
                MOTION_EVENT,
                automation
        );

        return automation;
    }

    @FXML
    private void simulateMotion() {
        try {
            prepareMotionEventForAllSensors(true);

            boolean lightWasActive =
                    targetLight.isActivo();

            int executedAutomations =
                    sensorComponent.reportEvent(
                            MOTION_EVENT
                    );

            boolean lightChanged =
                    !lightWasActive
                            && targetLight.isActivo();

            homeFacade.publishDeviceEvent(
                    motionSensor,
                    TipoEvento.MOVIMIENTO,
                    "Motion was detected by the hall sensor."
            );

            if (lightChanged) {
                homeFacade.publishDeviceEvent(
                        targetLight,
                        TipoEvento.ENCENDIDO,
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
            prepareMotionEventForAllSensors(false);

            homeFacade.publishDeviceEvent(
                    motionSensor,
                    TipoEvento.CAMBIO_ESTADO,
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

    private void prepareMotionEventForAllSensors(boolean detected) {
        List<SensorMovimiento> sensors = homeFacade.getAllDevices().stream()
                .filter(SensorMovimiento.class::isInstance)
                .map(SensorMovimiento.class::cast)
                .toList();
        for (SensorMovimiento sensor : sensors) {
            if (detected && !sensor.isActivo()) {
                homeFacade.turnOnDevice(sensor.getId());
            }
            sensor.setMovimientoDetectado(detected);
        }
    }

    private String buildExecutionFeedback(
            int executedAutomations,
            boolean lightChanged) {

        if (executedAutomations == 0) {
            return "Motion event processed. No active rule matched.";
        }
        return "Motion detected. The mediator executed "
                + executedAutomations
                + " automation(s). Demo light: "
                + (targetLight.isActivo() ? "ON" : "OFF")
                + (lightChanged ? " (turned on by this event)." : ".");
    }

    private void refreshView() {
        Automatizacion displayedAutomation =
                automationListView == null
                        ? null
                        : automationListView.getSelectionModel().getSelectedItem();
        if (displayedAutomation == null) {
            displayedAutomation = motionAutomation;
        }
        automationStatusLabel.setText(
                "Automation: "
                        + displayedAutomation.getNombre()
                        + "\nResult: "
                        + displayedAutomation
                        .getLastExecutionResult()
                        + "\nCommand executions: "
                        + displayedAutomation
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
