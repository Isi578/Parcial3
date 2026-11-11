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
import model.CerraduraInteligente;
import model.Dispositivo;
import patterns.command.BloquearPuertaCommand;
import patterns.command.Comando;
import patterns.command.ControladorComandos;
import patterns.command.DesbloquearPuertaCommand;
import patterns.facade.HogarFacade;
import patterns.factory.CerraduraFactory;
import patterns.factory.LuzFactory;
import patterns.factory.SensorFactory;
import patterns.factory.TermostatoFactory;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;

import java.util.Optional;
import java.io.IOException;

public class DispositivosController {

    private final ApplicationContext applicationContext =
            ApplicationContext.getInstance();

    private final HogarFacade homeFacade =
            applicationContext.getHogarFacade();

    private final ControladorComandos commandController =
            applicationContext.getCommandController();

    private final HistorialObserver historyObserver =
            applicationContext.getHistoryObserver();

    private final NotificacionObserver notificationObserver =
            applicationContext.getNotificationObserver();

    private final ObservableList<Dispositivo> devices =
            FXCollections.observableArrayList();

    @FXML
    private ListView<Dispositivo> devicesListView;

    @FXML
    private Label selectedDeviceLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label notificationLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button turnOnButton;

    @FXML
    private Button turnOffButton;

    @FXML
    private Button lockButton;

    @FXML
    private Button unlockButton;

    @FXML
    private Button undoButton;

    @FXML
    private void initialize() {
        configureDeviceList();
        configureSelectionListener();
        registerDemonstrationDevicesIfRequired();
        refreshView();

        devicesListView
                .getSelectionModel()
                .selectFirst();
    }

    @FXML
    private void addDevice() {
        openDeviceForm(null);
    }

    @FXML
    private void editSelectedDevice() {
        Dispositivo selected = getRequiredSelectedDevice();
        if (selected != null) {
            openDeviceForm(selected);
        }
    }

    @FXML
    private void deleteSelectedDevice() {
        Dispositivo selected = getRequiredSelectedDevice();
        if (selected == null) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete '" + selected.getNombre() + "'?",
                ButtonType.CANCEL,
                ButtonType.OK
        );
        confirmation.setHeaderText("Delete device");
        confirmation.initOwner(devicesListView.getScene().getWindow());
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            homeFacade.deleteDevice(selected.getId());
            refreshViewPreservingSelection(null);
            showFeedback("Device deleted.", false);
        } catch (RuntimeException exception) {
            showFeedback(getErrorMessage(exception), true);
        }
    }

    private void openDeviceForm(Dispositivo existingDevice) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/dispositivo_form.fxml")
            );
            Parent root = loader.load();
            DispositivoFormController form = loader.getController();
            form.configure(existingDevice);

            Stage stage = new Stage();
            stage.initOwner(devicesListView.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(existingDevice == null ? "Add device" : "Edit device");
            stage.setScene(new Scene(root));
            form.setOnSave(device -> {
                try {
                    if (existingDevice == null) {
                        homeFacade.registerDevice(device);
                    } else {
                        homeFacade.updateDevice(device);
                    }
                    stage.close();
                    refreshViewPreservingSelection(device.getId());
                    showFeedback("Device saved.", false);
                } catch (RuntimeException exception) {
                    form.showError(exception.getMessage());
                }
            });
            stage.showAndWait();
        } catch (IOException exception) {
            showFeedback("Could not open the device form: " + exception.getMessage(), true);
        }
    }

    private void configureDeviceList() {
        devicesListView.setItems(devices);

        devicesListView.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Dispositivo device,
                            boolean empty) {

                        super.updateItem(device, empty);

                        if (empty || device == null) {
                            setText(null);
                            return;
                        }

                        setText(
                                device.getNombre()
                                        + " | "
                                        + device.getDeviceType()
                                        + " | "
                                        + device.getStatusDescription()
                        );
                    }
                }
        );
    }

    private void configureSelectionListener() {
        devicesListView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         previousDevice,
                         selectedDevice) ->
                                updateSelectedDeviceInformation(
                                        selectedDevice
                                )
                );
    }

    private void registerDemonstrationDevicesIfRequired() {
        if (homeFacade.countDevices() > 0) {
            return;
        }

        homeFacade.registerDevice(
                new LuzFactory(
                        70,
                        "WARM WHITE"
                ).createDevice(
                        "light-living-room",
                        "Living Room Light",
                        "Philips",
                        "Hue",
                        null
                )
        );

        homeFacade.registerDevice(
                new SensorFactory(
                        5
                ).createDevice(
                        "sensor-hall",
                        "Hall Motion Sensor",
                        "Aqara",
                        "P1",
                        null
                )
        );

        homeFacade.registerDevice(
                new TermostatoFactory(
                        21.0,
                        23.0
                ).createDevice(
                        "thermostat-living-room",
                        "Living Room Thermostat",
                        "Google",
                        "Nest",
                        null
                )
        );

        homeFacade.registerDevice(
                new CerraduraFactory(
                        "ELECTRONIC"
                ).createDevice(
                        "lock-main-door",
                        "Main Door Lock",
                        "Yale",
                        "Assure",
                        null
                )
        );

        showFeedback(
                "Demonstration devices were registered.",
                false
        );
    }

    @FXML
    private void turnOnSelectedDevice() {
        Dispositivo selectedDevice =
                getRequiredSelectedDevice();

        if (selectedDevice == null) {
            return;
        }

        if (selectedDevice
                instanceof CerraduraInteligente) {

            showFeedback(
                    "A smart lock cannot be turned on. "
                            + "Use Lock or Unlock.",
                    true
            );

            return;
        }

        try {
            boolean changed =
                    homeFacade.turnOnDevice(
                            selectedDevice.getId()
                    );

            showFeedback(
                    changed
                            ? selectedDevice.getNombre()
                            + " was turned on."
                            : selectedDevice.getNombre()
                            + " was already on.",
                    false
            );

            refreshViewPreservingSelection(
                    selectedDevice.getId()
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void turnOffSelectedDevice() {
        Dispositivo selectedDevice =
                getRequiredSelectedDevice();

        if (selectedDevice == null) {
            return;
        }

        if (selectedDevice
                instanceof CerraduraInteligente) {

            showFeedback(
                    "A smart lock cannot be turned off. "
                            + "Use Lock or Unlock.",
                    true
            );

            return;
        }

        try {
            boolean changed =
                    homeFacade.turnOffDevice(
                            selectedDevice.getId()
                    );

            showFeedback(
                    changed
                            ? selectedDevice.getNombre()
                            + " was turned off."
                            : selectedDevice.getNombre()
                            + " was already off.",
                    false
            );

            refreshViewPreservingSelection(
                    selectedDevice.getId()
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void lockSelectedDevice() {
        CerraduraInteligente smartLock =
                getRequiredSelectedLock();

        if (smartLock == null) {
            return;
        }

        try {
            boolean previousLockedState =
                    smartLock.isBloqueada();

            commandController.ejecutarComando(
                    new BloquearPuertaCommand(
                            smartLock
                    )
            );

            boolean changed =
                    previousLockedState
                            != smartLock.isBloqueada();

            if (changed) {
                homeFacade.publishDeviceEvent(
                        smartLock,
                        "Smart lock was locked."
                );
            }

            showFeedback(
                    changed
                            ? smartLock.getNombre()
                            + " was locked."
                            : smartLock.getNombre()
                            + " was already locked.",
                    false
            );

            refreshViewPreservingSelection(
                    smartLock.getId()
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void unlockSelectedDevice() {
        CerraduraInteligente smartLock =
                getRequiredSelectedLock();

        if (smartLock == null) {
            return;
        }

        try {
            boolean previousLockedState =
                    smartLock.isBloqueada();

            commandController.ejecutarComando(
                    new DesbloquearPuertaCommand(
                            smartLock
                    )
            );

            boolean changed =
                    previousLockedState
                            != smartLock.isBloqueada();

            if (changed) {
                homeFacade.publishDeviceEvent(
                        smartLock,
                        "Smart lock was unlocked."
                );
            }

            showFeedback(
                    changed
                            ? smartLock.getNombre()
                            + " was unlocked."
                            : smartLock.getNombre()
                            + " was already unlocked.",
                    false
            );

            refreshViewPreservingSelection(
                    smartLock.getId()
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void undoLastAction() {
        try {
            Optional<Comando> undoneCommand =
                    homeFacade.undoLastCommand();

            if (undoneCommand.isEmpty()) {
                showFeedback(
                        "There is no action available to undo.",
                        false
                );

                refreshView();
                return;
            }

            Comando command =
                    undoneCommand.orElseThrow();

            publishLockUndoEventIfRequired(
                    command
            );

            showFeedback(
                    "Action undone: "
                            + command.getDescription(),
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

    private void publishLockUndoEventIfRequired(
            Comando command) {

        if (command
                instanceof BloquearPuertaCommand
                lockCommand) {

            homeFacade.publishDeviceEvent(
                    lockCommand.getCerradura(),
                    "Lock command was undone. "
                            + "Current state: "
                            + lockCommand
                            .getCerradura()
                            .getEstado()
                            .getStateName()
                            + "."
            );

            return;
        }

        if (command
                instanceof DesbloquearPuertaCommand
                unlockCommand) {

            homeFacade.publishDeviceEvent(
                    unlockCommand.getCerradura(),
                    "Unlock command was undone. "
                            + "Current state: "
                            + unlockCommand
                            .getCerradura()
                            .getEstado()
                            .getStateName()
                            + "."
            );
        }
    }

    @FXML
    private void refreshDevices() {
        String selectedDeviceId =
                getSelectedDeviceId();

        refreshViewPreservingSelection(
                selectedDeviceId
        );

        showFeedback(
                "Device information was refreshed.",
                false
        );
    }

    private void refreshView() {
        devices.setAll(
                homeFacade.getAllDevices()
        );

        devicesListView.refresh();

        summaryLabel.setText(
                homeFacade.getDeviceSummary()
                        + " | Undo actions: "
                        + homeFacade
                        .getUndoableCommandCount()
        );

        updateActivityInformation();

        Dispositivo selectedDevice =
                devicesListView
                        .getSelectionModel()
                        .getSelectedItem();

        updateSelectedDeviceInformation(
                selectedDevice
        );
    }

    private void refreshViewPreservingSelection(
            String selectedDeviceId) {

        refreshView();

        if (selectedDeviceId == null
                || selectedDeviceId.isBlank()) {

            devicesListView
                    .getSelectionModel()
                    .selectFirst();

            return;
        }

        for (Dispositivo device : devices) {
            if (selectedDeviceId.equals(
                    device.getId())) {

                devicesListView
                        .getSelectionModel()
                        .select(device);

                return;
            }
        }

        devicesListView
                .getSelectionModel()
                .selectFirst();
    }

    private void updateActivityInformation() {
        eventLabel.setText(
                historyObserver
                        .getLastEvent()
                        .map(event ->
                                "Last event: "
                                        + event.getDescripcion()
                        )
                        .orElse(
                                "Last event: No events recorded."
                        )
        );

        notificationLabel.setText(
                notificationObserver
                        .getLastNotification()
                        .map(notification ->
                                "Last notification: "
                                        + notification
                        )
                        .orElse(
                                "Last notification: "
                                        + "No notifications recorded."
                        )
        );
    }

    private void updateSelectedDeviceInformation(
            Dispositivo device) {

        if (device == null) {
            selectedDeviceLabel.setText(
                    "No device selected."
            );

            updateActionAvailability();
            return;
        }

        selectedDeviceLabel.setText(
                "Selected: "
                        + device.getNombre()
                        + " | Type: "
                        + device.getDeviceType()
                        + " | Status: "
                        + device.getStatusDescription()
        );

        updateActionAvailability();
    }

    private void updateActionAvailability() {
        Dispositivo selectedDevice =
                devicesListView == null
                        ? null
                        : devicesListView
                        .getSelectionModel()
                        .getSelectedItem();

        boolean noSelection =
                selectedDevice == null;

        boolean smartLockSelected =
                selectedDevice
                        instanceof CerraduraInteligente;

        turnOnButton.setDisable(
                noSelection || smartLockSelected
        );

        turnOffButton.setDisable(
                noSelection || smartLockSelected
        );

        lockButton.setDisable(
                !smartLockSelected
        );

        unlockButton.setDisable(
                !smartLockSelected
        );

        undoButton.setDisable(
                !homeFacade.canUndo()
        );
    }

    private Dispositivo getRequiredSelectedDevice() {
        Dispositivo selectedDevice =
                devicesListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedDevice == null) {
            showFeedback(
                    "Select a device first.",
                    true
            );
        }

        return selectedDevice;
    }

    private CerraduraInteligente getRequiredSelectedLock() {
        Dispositivo selectedDevice =
                getRequiredSelectedDevice();

        if (selectedDevice == null) {
            return null;
        }

        if (!(selectedDevice
                instanceof CerraduraInteligente smartLock)) {

            showFeedback(
                    "Select a smart lock first.",
                    true
            );

            return null;
        }

        return smartLock;
    }

    private String getSelectedDeviceId() {
        Dispositivo selectedDevice =
                devicesListView
                        .getSelectionModel()
                        .getSelectedItem();

        return selectedDevice == null
                ? null
                : selectedDevice.getId();
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
