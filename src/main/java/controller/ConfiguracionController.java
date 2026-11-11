package controller;

import app.ApplicationContext;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import patterns.singleton.ConfiguracionHogar;

public class ConfiguracionController {

    private static final String CELSIUS =
            "CELSIUS";

    private static final String FAHRENHEIT =
            "FAHRENHEIT";

    private final ApplicationContext applicationContext =
            ApplicationContext.getInstance();

    private final ConfiguracionHogar homeConfiguration =
            applicationContext.getHomeConfiguration();

    @FXML
    private TextField homeNameField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<String> temperatureUnitComboBox;

    @FXML
    private CheckBox notificationsEnabledCheckBox;

    @FXML
    private CheckBox energySavingEnabledCheckBox;

    @FXML
    private Label configurationSummaryLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private void initialize() {
        configureTemperatureUnits();
        loadConfiguration();

        showFeedback(
                "Shared home configuration was loaded.",
                false
        );
    }

    private void configureTemperatureUnits() {
        temperatureUnitComboBox.getItems().setAll(
                CELSIUS,
                FAHRENHEIT
        );
    }

    @FXML
    private void saveConfiguration() {
        try {
            String homeName =
                    requireText(
                            homeNameField.getText(),
                            "Home name"
                    );

            String address =
                    requireText(
                            addressField.getText(),
                            "Address"
                    );

            String temperatureUnit =
                    temperatureUnitComboBox
                            .getValue();

            if (temperatureUnit == null
                    || temperatureUnit.isBlank()) {

                throw new IllegalArgumentException(
                        "Select a temperature unit."
                );
            }

            homeConfiguration.updateHomeInformation(
                    homeName,
                    address
            );

            homeConfiguration.setTemperatureUnit(
                    temperatureUnit
            );

            homeConfiguration.setNotificationsEnabled(
                    notificationsEnabledCheckBox
                            .isSelected()
            );

            homeConfiguration.setEnergySavingEnabled(
                    energySavingEnabledCheckBox
                            .isSelected()
            );

            applicationContext
                    .synchronizeNotificationSettings();

            updateSummary();

            showFeedback(
                    "Configuration was saved successfully.",
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
    private void restoreDefaultConfiguration() {
        try {
            homeConfiguration.restoreDefaults();

            applicationContext
                    .synchronizeNotificationSettings();

            loadConfiguration();

            showFeedback(
                    "Default configuration was restored.",
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
    private void reloadConfiguration() {
        loadConfiguration();

        showFeedback(
                "Configuration was reloaded.",
                false
        );
    }

    private void loadConfiguration() {
        homeNameField.setText(
                homeConfiguration.getHomeName()
        );

        addressField.setText(
                homeConfiguration.getAddress()
        );

        temperatureUnitComboBox.setValue(
                homeConfiguration
                        .getTemperatureUnit()
        );

        notificationsEnabledCheckBox.setSelected(
                homeConfiguration
                        .isNotificationsEnabled()
        );

        energySavingEnabledCheckBox.setSelected(
                homeConfiguration
                        .isEnergySavingEnabled()
        );

        updateSummary();
    }

    private void updateSummary() {
        configurationSummaryLabel.setText(
                homeConfiguration
                        .getConfigurationSummary()
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