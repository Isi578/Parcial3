package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainController {

    private static final String DASHBOARD_VIEW =
            "/fxml/dashboard.fxml";

    private static final String DEVICES_VIEW =
            "/fxml/dispositivos.fxml";

    private static final String ROOMS_VIEW =
            "/fxml/habitaciones.fxml";

    private static final String AUTOMATIONS_VIEW =
            "/fxml/automatizaciones.fxml";

    private static final String NOTIFICATIONS_VIEW =
            "/fxml/notificaciones.fxml";

    private static final String SETTINGS_VIEW =
            "/fxml/configuracion.fxml";

    @FXML
    private BorderPane contentPane;

    @FXML
    private Label sectionTitleLabel;

    @FXML
    private void initialize() {
        showView(
                DASHBOARD_VIEW,
                "Dashboard"
        );
    }

    @FXML
    private void mostrarDashboard() {
        showView(
                DASHBOARD_VIEW,
                "Dashboard"
        );
    }

    @FXML
    private void mostrarDispositivos() {
        showView(
                DEVICES_VIEW,
                "Devices"
        );
    }

    @FXML
    private void mostrarHabitaciones() {
        showView(
                ROOMS_VIEW,
                "Rooms"
        );
    }

    @FXML
    private void mostrarAutomatizaciones() {
        showView(
                AUTOMATIONS_VIEW,
                "Automations"
        );
    }

    @FXML
    private void mostrarNotificaciones() {
        showView(
                NOTIFICATIONS_VIEW,
                "Notifications"
        );
    }

    @FXML
    private void mostrarConfiguracion() {
        showView(
                SETTINGS_VIEW,
                "Settings"
        );
    }

    private void showView(
            String resourcePath,
            String sectionTitle) {

        Objects.requireNonNull(
                resourcePath,
                "FXML resource path cannot be null."
        );

        Objects.requireNonNull(
                sectionTitle,
                "Section title cannot be null."
        );

        try {
            Node view = loadView(resourcePath);
            contentPane.setCenter(view);
            sectionTitleLabel.setText(sectionTitle);
        } catch (IOException exception) {
            showLoadingError(
                    sectionTitle,
                    resourcePath,
                    exception
            );
        }
    }

    private Node loadView(String resourcePath)
            throws IOException {

        URL resource = MainController.class.getResource(
                resourcePath
        );

        if (resource == null) {
            throw new IOException(
                    "FXML resource was not found: "
                            + resourcePath
            );
        }

        FXMLLoader loader = new FXMLLoader(resource);
        return loader.load();
    }

    private void showLoadingError(
            String sectionTitle,
            String resourcePath,
            IOException exception) {

        sectionTitleLabel.setText(
                sectionTitle + " - Loading error"
        );

        Label errorLabel = new Label(
                "The view could not be loaded.\n"
                        + "Resource: "
                        + resourcePath
                        + "\n"
                        + "Cause: "
                        + getErrorMessage(exception)
        );

        errorLabel.setWrapText(true);
        errorLabel.setStyle(
                "-fx-text-fill: #b91c1c;"
                        + "-fx-font-size: 14px;"
                        + "-fx-padding: 24px;"
        );

        contentPane.setCenter(errorLabel);
        exception.printStackTrace();
    }

    private static String getErrorMessage(
            IOException exception) {

        if (exception.getMessage() == null
                || exception.getMessage().isBlank()) {

            return exception.getClass().getSimpleName();
        }

        return exception.getMessage();
    }
}