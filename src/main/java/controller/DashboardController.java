package controller;

import app.ApplicationContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Dispositivo;
import patterns.facade.HogarFacade;
import patterns.mediator.HogarMediator;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;
import patterns.singleton.ConfiguracionHogar;

import java.util.List;

public class DashboardController {

    private final ApplicationContext applicationContext =
            ApplicationContext.getInstance();

    private final HogarFacade homeFacade =
            applicationContext.getHogarFacade();

    private final HistorialObserver historyObserver =
            applicationContext.getHistoryObserver();

    private final NotificacionObserver notificationObserver =
            applicationContext.getNotificationObserver();

    private final HogarMediator homeMediator =
            applicationContext.getHogarMediator();

    private final ConfiguracionHogar homeConfiguration =
            applicationContext.getHomeConfiguration();

    @FXML
    private Label homeNameLabel;

    @FXML
    private Label homeAddressLabel;

    @FXML
    private Label totalDevicesLabel;

    @FXML
    private Label activeDevicesLabel;

    @FXML
    private Label eventCountLabel;

    @FXML
    private Label notificationCountLabel;

    @FXML
    private Label undoCountLabel;

    @FXML
    private Label automationCountLabel;

    @FXML
    private Label mediatorExecutionCountLabel;

    @FXML
    private Label latestEventLabel;

    @FXML
    private Label latestNotificationLabel;

    @FXML
    private Label deviceSummaryLabel;

    @FXML
    private Label patternStatusLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private void initialize() {
        refreshDashboard();

        showFeedback(
                "Dashboard information loaded.",
                false
        );
    }

    @FXML
    private void refreshDashboard() {
        try {
            applicationContext
                    .synchronizeNotificationSettings();

            updateHomeInformation();
            updateCounters();
            updateLatestActivity();
            updateDeviceSummary();
            updatePatternStatus();
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    @FXML
    private void clearSessionActivity() {
        try {
            applicationContext.clearSessionActivity();

            refreshDashboard();

            showFeedback(
                    "Command history, events and notifications "
                            + "were cleared.",
                    false
            );
        } catch (RuntimeException exception) {
            showFeedback(
                    getErrorMessage(exception),
                    true
            );
        }
    }

    private void updateHomeInformation() {
        homeNameLabel.setText(
                homeConfiguration.getHomeName()
        );

        homeAddressLabel.setText(
                "Address: "
                        + homeConfiguration.getAddress()
                        + " | Temperature unit: "
                        + homeConfiguration
                        .getTemperatureUnit()
        );
    }

    private void updateCounters() {
        totalDevicesLabel.setText(
                String.valueOf(
                        homeFacade.countDevices()
                )
        );

        activeDevicesLabel.setText(
                String.valueOf(
                        homeFacade.countActiveDevices()
                )
        );

        eventCountLabel.setText(
                String.valueOf(
                        historyObserver.getEventCount()
                )
        );

        notificationCountLabel.setText(
                String.valueOf(
                        notificationObserver
                                .getNotificationCount()
                )
        );

        undoCountLabel.setText(
                String.valueOf(
                        homeFacade
                                .getUndoableCommandCount()
                )
        );

        automationCountLabel.setText(
                String.valueOf(
                        homeMediator.getAutomationCount()
                )
        );

        mediatorExecutionCountLabel.setText(
                String.valueOf(
                        homeMediator
                                .getExecutedAutomationCount()
                )
        );
    }

    private void updateLatestActivity() {
        latestEventLabel.setText(
                historyObserver
                        .getLastEvent()
                        .map(event ->
                                "Latest event: "
                                        + normalizeText(
                                        event.getDescripcion(),
                                        "No description"
                                )
                                        + describeEventDevice(
                                        event.getDispositivo()
                                )
                        )
                        .orElse(
                                "Latest event: No events recorded."
                        )
        );

        latestNotificationLabel.setText(
                notificationObserver
                        .getLastNotification()
                        .map(notification ->
                                "Latest notification: "
                                        + notification
                        )
                        .orElse(
                                "Latest notification: "
                                        + "No notifications recorded."
                        )
        );
    }

    private void updateDeviceSummary() {
        List<Dispositivo> devices =
                homeFacade.getAllDevices();

        if (devices.isEmpty()) {
            deviceSummaryLabel.setText(
                    "No devices are registered. "
                            + "Open Devices to create "
                            + "the demonstration devices."
            );

            return;
        }

        StringBuilder summary =
                new StringBuilder();

        for (Dispositivo device : devices) {
            if (!summary.isEmpty()) {
                summary.append("\n");
            }

            summary.append("• ")
                    .append(device.getNombre())
                    .append(" | ")
                    .append(device.getDeviceType())
                    .append(" | ")
                    .append(
                            device.getStatusDescription()
                    );
        }

        deviceSummaryLabel.setText(
                summary.toString()
        );
    }

    private void updatePatternStatus() {
        int configuredPatterns = 0;

        configuredPatterns += 1;
        configuredPatterns += 1;
        configuredPatterns += 1;
        configuredPatterns += 1;

        if (homeFacade.countDevices() > 0) {
            configuredPatterns += 1;
            configuredPatterns += 1;
            configuredPatterns += 1;
        }

        if (homeMediator.getComponentCount() > 0) {
            configuredPatterns += 1;
        }

        if (homeMediator.getAutomationCount() > 0) {
            configuredPatterns += 1;
        }

        patternStatusLabel.setText(
                "Patterns available: "
                        + configuredPatterns
                        + " of 9"
                        + "\n"
                        + "Always available: Singleton, Facade, "
                        + "Command and State."
                        + "\n"
                        + "Device flow: Factory Method, "
                        + "Composite and Observer."
                        + "\n"
                        + "Automation flow: Strategy and Mediator."
        );
    }

    private static String describeEventDevice(
            Dispositivo device) {

        if (device == null) {
            return " | Source: HOME";
        }

        return " | Source: "
                + device.getNombre();
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