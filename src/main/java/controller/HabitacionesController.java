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
import model.Habitacion;
import patterns.composite.ComponenteHogar;
import patterns.composite.DispositivoIndividual;
import patterns.composite.GrupoDispositivos;
import patterns.facade.HogarFacade;

import java.util.List;
import java.io.IOException;

public class HabitacionesController {

    private static final String LIVING_ROOM_GROUP_ID =
            "group-living-room";

    private static final String SECURITY_GROUP_ID =
            "group-security";

    private final ApplicationContext applicationContext =
            ApplicationContext.getInstance();

    private final HogarFacade homeFacade =
            applicationContext.getHogarFacade();

    private final ObservableList<GrupoDispositivos> groups =
            FXCollections.observableArrayList();

    private final ObservableList<Habitacion> rooms =
            FXCollections.observableArrayList();

    @FXML
    private ListView<GrupoDispositivos> groupsListView;

    @FXML
    private ListView<ComponenteHogar> componentsListView;

    @FXML
    private ListView<Habitacion> roomsListView;

    @FXML
    private Label selectedGroupLabel;

    @FXML
    private Label groupSummaryLabel;

    @FXML
    private Label totalSummaryLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button turnOnGroupButton;

    @FXML
    private Button turnOffGroupButton;

    @FXML
    private void initialize() {
        configureRoomList();
        refreshRooms();
        configureGroupList();
        configureComponentList();
        configureSelectionListener();
        rebuildGroups();

        if (!groups.isEmpty()) {
            groupsListView
                    .getSelectionModel()
                    .selectFirst();
        }
    }

    private void configureRoomList() {
        roomsListView.setItems(rooms);
        roomsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Habitacion room, boolean empty) {
                super.updateItem(room, empty);
                setText(empty || room == null ? null
                        : room.getNombre() + " | "
                        + room.getDispositivos().size() + " device(s)");
            }
        });
    }

    @FXML
    private void addRoom() {
        openRoomForm(null);
    }

    @FXML
    private void editSelectedRoom() {
        Habitacion room = roomsListView.getSelectionModel().getSelectedItem();
        if (room == null) {
            showFeedback("Select a room first.", true);
            return;
        }
        openRoomForm(room);
    }

    @FXML
    private void deleteSelectedRoom() {
        Habitacion room = roomsListView.getSelectionModel().getSelectedItem();
        if (room == null) {
            showFeedback("Select a room first.", true);
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete room '" + room.getNombre() + "'?",
                ButtonType.CANCEL,
                ButtonType.OK
        );
        confirmation.setHeaderText("Delete room");
        confirmation.initOwner(roomsListView.getScene().getWindow());
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            applicationContext.getHabitacionService().eliminarHabitacion(room.getId());
            refreshRooms();
            showFeedback("Room deleted.", false);
        } catch (RuntimeException exception) {
            showFeedback(getErrorMessage(exception), true);
        }
    }

    @FXML
    private void refreshRooms() {
        rooms.setAll(applicationContext.getHabitacionService().obtenerHabitaciones());
        if (roomsListView != null) {
            roomsListView.refresh();
        }
    }

    private void openRoomForm(Habitacion existingRoom) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/habitacion_form.fxml")
            );
            Parent root = loader.load();
            HabitacionFormController form = loader.getController();
            form.configure(existingRoom);
            Stage stage = new Stage();
            stage.initOwner(roomsListView.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(existingRoom == null ? "Add room" : "Edit room");
            stage.setScene(new Scene(root));
            form.setOnSave(room -> {
                try {
                    if (existingRoom == null) {
                        applicationContext.getHabitacionService().registrarHabitacion(room);
                    } else {
                        applicationContext.getHabitacionService().actualizarHabitacion(room);
                    }
                    stage.close();
                    refreshRooms();
                    roomsListView.getSelectionModel().select(room);
                    showFeedback("Room saved.", false);
                } catch (RuntimeException exception) {
                    form.showError(exception.getMessage());
                }
            });
            stage.showAndWait();
        } catch (IOException exception) {
            showFeedback("Could not open the room form: " + exception.getMessage(), true);
        }
    }

    private void configureGroupList() {
        groupsListView.setItems(groups);

        groupsListView.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            GrupoDispositivos group,
                            boolean empty) {

                        super.updateItem(group, empty);

                        if (empty || group == null) {
                            setText(null);
                            return;
                        }

                        setText(
                                group.getName()
                                        + " | "
                                        + group.getStatusDescription()
                        );
                    }
                }
        );
    }

    private void configureComponentList() {
        componentsListView.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            ComponenteHogar component,
                            boolean empty) {

                        super.updateItem(
                                component,
                                empty
                        );

                        if (empty || component == null) {
                            setText(null);
                            return;
                        }

                        setText(
                                component.getName()
                                        + " | "
                                        + component
                                        .getStatusDescription()
                        );
                    }
                }
        );
    }

    private void configureSelectionListener() {
        groupsListView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         previousGroup,
                         selectedGroup) ->
                                showSelectedGroup(
                                        selectedGroup
                                )
                );
    }

    @FXML
    private void rebuildGroups() {
        String selectedGroupId =
                getSelectedGroupId();

        groups.clear();

        GrupoDispositivos livingRoomGroup =
                new GrupoDispositivos(
                        LIVING_ROOM_GROUP_ID,
                        "Living Room"
                );

        GrupoDispositivos securityGroup =
                new GrupoDispositivos(
                        SECURITY_GROUP_ID,
                        "Security"
                );

        List<Dispositivo> registeredDevices =
                homeFacade.getAllDevices();

        for (Dispositivo device :
                registeredDevices) {

            DispositivoIndividual component =
                    new DispositivoIndividual(
                            device
                    );

            if (device
                    instanceof CerraduraInteligente) {

                securityGroup.addComponent(
                        component
                );

                continue;
            }

            livingRoomGroup.addComponent(
                    component
            );
        }

        groups.add(livingRoomGroup);
        groups.add(securityGroup);

        restoreGroupSelection(
                selectedGroupId
        );

        updateTotalSummary();

        if (registeredDevices.isEmpty()) {
            showFeedback(
                    "No devices are registered. "
                            + "Open Devices first.",
                    true
            );
        } else {
            showFeedback(
                    "Composite groups were rebuilt "
                            + "from the shared devices.",
                    false
            );
        }
    }

    @FXML
    private void turnOnSelectedGroup() {
        GrupoDispositivos selectedGroup =
                getRequiredSelectedGroup();

        if (selectedGroup == null) {
            return;
        }

        try {
            if (containsOnlySmartLocks(
                    selectedGroup)) {

                showFeedback(
                        "The Security group contains smart locks. "
                                + "Use Lock and Unlock from Devices.",
                        true
                );

                return;
            }

            int activeBefore =
                    selectedGroup
                            .getActiveDeviceCount();

            selectedGroup.turnOn();

            int activeAfter =
                    selectedGroup
                            .getActiveDeviceCount();

            publishChangedDeviceEvents(
                    selectedGroup,
                    true
            );

            refreshCompositeView(
                    selectedGroup.getId()
            );

            showFeedback(
                    buildGroupActionMessage(
                            selectedGroup,
                            "turned on",
                            activeAfter - activeBefore
                    ),
                    false
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void turnOffSelectedGroup() {
        GrupoDispositivos selectedGroup =
                getRequiredSelectedGroup();

        if (selectedGroup == null) {
            return;
        }

        try {
            if (containsOnlySmartLocks(
                    selectedGroup)) {

                showFeedback(
                        "The Security group contains smart locks. "
                                + "Use Lock and Unlock from Devices.",
                        true
                );

                return;
            }

            int activeBefore =
                    selectedGroup
                            .getActiveDeviceCount();

            selectedGroup.turnOff();

            int activeAfter =
                    selectedGroup
                            .getActiveDeviceCount();

            publishChangedDeviceEvents(
                    selectedGroup,
                    false
            );

            refreshCompositeView(
                    selectedGroup.getId()
            );

            showFeedback(
                    buildGroupActionMessage(
                            selectedGroup,
                            "turned off",
                            activeBefore - activeAfter
                    ),
                    false
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void refreshGroups() {
        rebuildGroups();
    }

    private void publishChangedDeviceEvents(
            GrupoDispositivos group,
            boolean turnedOn) {

        for (ComponenteHogar component :
                group.getComponents()) {

            if (!(component
                    instanceof DispositivoIndividual
                    individualDevice)) {

                continue;
            }

            Dispositivo device =
                    individualDevice.getDevice();

            if (device
                    instanceof CerraduraInteligente) {

                continue;
            }

            boolean expectedState =
                    turnedOn;

            if (device.isActivo()
                    == expectedState) {

                homeFacade.publishDeviceEvent(
                        device,
                        turnedOn
                                ? "Device turned on through "
                                + "Composite group '"
                                + group.getName()
                                + "'."
                                : "Device turned off through "
                                + "Composite group '"
                                + group.getName()
                                + "'."
                );
            }
        }
    }

    private void refreshCompositeView(
            String selectedGroupId) {

        groupsListView.refresh();

        restoreGroupSelection(
                selectedGroupId
        );

        GrupoDispositivos selectedGroup =
                groupsListView
                        .getSelectionModel()
                        .getSelectedItem();

        showSelectedGroup(selectedGroup);
        updateTotalSummary();
    }

    private void showSelectedGroup(
            GrupoDispositivos group) {

        if (group == null) {
            selectedGroupLabel.setText(
                    "No group selected."
            );

            groupSummaryLabel.setText(
                    "Select a group to inspect its devices."
            );

            componentsListView.setItems(
                    FXCollections.observableArrayList()
            );

            updateActionAvailability(null);
            return;
        }

        selectedGroupLabel.setText(
                "Selected group: "
                        + group.getName()
        );

        groupSummaryLabel.setText(
                "Devices: "
                        + group.getDeviceCount()
                        + " | Active: "
                        + group.getActiveDeviceCount()
                        + " | All active: "
                        + yesNo(group.isActive())
                        + " | Any active: "
                        + yesNo(
                        group.hasAnyActiveDevice()
                )
        );

        componentsListView.setItems(
                FXCollections.observableArrayList(
                        group.getComponents()
                )
        );

        updateActionAvailability(group);
    }

    private void updateTotalSummary() {
        int totalDevices =
                groups.stream()
                        .mapToInt(
                                GrupoDispositivos::getDeviceCount
                        )
                        .sum();

        int activeDevices =
                groups.stream()
                        .mapToInt(
                                GrupoDispositivos
                                        ::getActiveDeviceCount
                        )
                        .sum();

        totalSummaryLabel.setText(
                "Composite summary: "
                        + activeDevices
                        + " of "
                        + totalDevices
                        + " devices are active in "
                        + groups.size()
                        + " groups."
        );
    }

    private void updateActionAvailability(
            GrupoDispositivos group) {

        boolean unavailable =
                group == null
                        || group.getDeviceCount() == 0
                        || containsOnlySmartLocks(group);

        turnOnGroupButton.setDisable(
                unavailable
        );

        turnOffGroupButton.setDisable(
                unavailable
        );
    }

    private static boolean containsOnlySmartLocks(
            GrupoDispositivos group) {

        if (group == null
                || group.getComponents().isEmpty()) {

            return false;
        }

        return group.getComponents()
                .stream()
                .allMatch(component ->
                        component
                                instanceof DispositivoIndividual
                                individualDevice
                                && individualDevice
                                .getDevice()
                                instanceof CerraduraInteligente
                );
    }

    private GrupoDispositivos
    getRequiredSelectedGroup() {

        GrupoDispositivos selectedGroup =
                groupsListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedGroup == null) {
            showFeedback(
                    "Select a group first.",
                    true
            );
        }

        return selectedGroup;
    }

    private String getSelectedGroupId() {
        GrupoDispositivos selectedGroup =
                groupsListView
                        .getSelectionModel()
                        .getSelectedItem();

        return selectedGroup == null
                ? null
                : selectedGroup.getId();
    }

    private void restoreGroupSelection(
            String groupId) {

        if (groups.isEmpty()) {
            groupsListView
                    .getSelectionModel()
                    .clearSelection();

            showSelectedGroup(null);
            return;
        }

        if (groupId != null
                && !groupId.isBlank()) {

            for (GrupoDispositivos group :
                    groups) {

                if (groupId.equals(group.getId())) {
                    groupsListView
                            .getSelectionModel()
                            .select(group);

                    return;
                }
            }
        }

        groupsListView
                .getSelectionModel()
                .selectFirst();
    }

    private static String buildGroupActionMessage(
            GrupoDispositivos group,
            String action,
            int changedDevices) {

        return "Group '"
                + group.getName()
                + "' was "
                + action
                + ". Changed devices: "
                + Math.max(changedDevices, 0)
                + ".";
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
