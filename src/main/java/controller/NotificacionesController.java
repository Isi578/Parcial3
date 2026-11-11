package controller;

import app.ApplicationContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Evento;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;
import patterns.singleton.ConfiguracionHogar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacionesController {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            );

    private final ApplicationContext applicationContext =
            ApplicationContext.getInstance();

    private final HistorialObserver historyObserver =
            applicationContext.getHistoryObserver();

    private final NotificacionObserver notificationObserver =
            applicationContext.getNotificationObserver();

    private final ConfiguracionHogar homeConfiguration =
            applicationContext.getHomeConfiguration();

    private final ObservableList<Evento> events =
            FXCollections.observableArrayList();

    private final ObservableList<String> notifications =
            FXCollections.observableArrayList();

    @FXML
    private ListView<Evento> eventsListView;

    @FXML
    private ListView<String> notificationsListView;

    @FXML
    private Label eventCountLabel;

    @FXML
    private Label notificationCountLabel;

    @FXML
    private Label latestActivityLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private CheckBox notificationsEnabledCheckBox;

    @FXML
    private void initialize() {
        configureEventList();
        configureNotificationList();

        notificationsEnabledCheckBox.setSelected(
                homeConfiguration
                        .isNotificationsEnabled()
        );

        applicationContext
                .synchronizeNotificationSettings();

        refreshActivity();

        showFeedback(
                "Notification activity was loaded.",
                false
        );
    }

    private void configureEventList() {
        eventsListView.setItems(events);

        eventsListView.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Evento event,
                            boolean empty) {

                        super.updateItem(event, empty);

                        if (empty || event == null) {
                            setText(null);
                            return;
                        }

                        setText(
                                formatEvent(event)
                        );
                    }
                }
        );
    }

    private void configureNotificationList() {
        notificationsListView.setItems(
                notifications
        );

        notificationsListView.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            String notification,
                            boolean empty) {

                        super.updateItem(
                                notification,
                                empty
                        );

                        if (empty
                                || notification == null) {

                            setText(null);
                            return;
                        }

                        setText(notification);
                        setWrapText(true);
                    }
                }
        );
    }

    @FXML
    private void refreshActivity() {
        events.setAll(
                historyObserver.getEventHistory()
        );

        notifications.setAll(
                notificationObserver
                        .getNotificationMessages()
        );

        eventCountLabel.setText(
                "Recorded events: "
                        + events.size()
        );

        notificationCountLabel.setText(
                "Notifications: "
                        + notifications.size()
        );

        latestActivityLabel.setText(
                buildLatestActivityDescription()
        );

        eventsListView.refresh();
        notificationsListView.refresh();
    }

    @FXML
    private void updateNotificationPreference() {
        boolean enabled =
                notificationsEnabledCheckBox
                        .isSelected();

        homeConfiguration
                .setNotificationsEnabled(enabled);

        applicationContext
                .synchronizeNotificationSettings();

        showFeedback(
                enabled
                        ? "Notifications were enabled."
                        : "Notifications were disabled. "
                        + "Events will continue to be recorded.",
                false
        );

        refreshActivity();
    }

    @FXML
    private void clearNotifications() {
        notificationObserver
                .clearNotifications();

        refreshActivity();

        showFeedback(
                "Notification messages were cleared.",
                false
        );
    }

    @FXML
    private void clearEventHistory() {
        historyObserver.clearHistory();

        refreshActivity();

        showFeedback(
                "Event history was cleared.",
                false
        );
    }

    @FXML
    private void clearAllActivity() {
        applicationContext.clearSessionActivity();

        refreshActivity();

        showFeedback(
                "Events, notifications and command history "
                        + "were cleared.",
                false
        );
    }

    private String buildLatestActivityDescription() {
        String latestEvent =
                historyObserver
                        .getLastEvent()
                        .map(event ->
                                normalizeText(
                                        event.getDescripcion(),
                                        "No description"
                                )
                        )
                        .orElse(
                                "No events recorded"
                        );

        String latestNotification =
                notificationObserver
                        .getLastNotification()
                        .orElse(
                                "No notifications recorded"
                        );

        return "Latest event: "
                + latestEvent
                + "\nLatest notification: "
                + latestNotification;
    }

    private static String formatEvent(
            Evento event) {

        String eventId =
                normalizeText(
                        event.getId(),
                        "NO_ID"
                );

        String eventType =
                event.getTipo() == null
                        ? "GENERAL"
                        : event.getTipo().name();

        String deviceName =
                event.getDispositivo() == null
                        ? "HOME"
                        : normalizeText(
                        event
                                .getDispositivo()
                                .getNombre(),
                        "Unnamed device"
                );

        String description =
                normalizeText(
                        event.getDescripcion(),
                        "No description"
                );

        return "["
                + formatDate(event.getFecha())
                + "] "
                + eventType
                + " | "
                + deviceName
                + " | "
                + description
                + " | ID: "
                + eventId;
    }

    private static String formatDate(
            LocalDateTime dateTime) {

        if (dateTime == null) {
            return "NO_DATE";
        }

        return dateTime.format(
                DATE_FORMATTER
        );
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

    private static String normalizeText(
            String value,
            String defaultValue) {

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value.trim();
    }
}