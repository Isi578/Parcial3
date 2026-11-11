package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Habitacion;

import java.util.UUID;
import java.util.function.Consumer;

public class HabitacionFormController {
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private Label errorLabel;

    private Habitacion existingRoom;
    private Consumer<Habitacion> onSave;

    public void configure(Habitacion room) {
        existingRoom = room;
        if (room != null) {
            nameField.setText(room.getNombre());
            descriptionField.setText(room.getDescripcion());
        }
    }

    public void setOnSave(Consumer<Habitacion> onSave) {
        this.onSave = onSave;
    }

    public void showError(String message) {
        errorLabel.setText(message == null ? "The room could not be saved." : message);
    }

    @FXML
    private void guardar() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            showError("Room name cannot be empty.");
            return;
        }
        Habitacion room = existingRoom == null
                ? new Habitacion(UUID.randomUUID().toString(), name.trim(), descriptionField.getText())
                : existingRoom;
        if (existingRoom != null) {
            room.setNombre(name.trim());
            room.setDescripcion(descriptionField.getText());
        }
        if (onSave == null) {
            showError("The form is not connected to its screen.");
            return;
        }
        onSave.accept(room);
    }

    @FXML
    private void cancelar() {
        if (nameField.getScene() != null
                && nameField.getScene().getWindow() instanceof Stage stage) {
            stage.close();
        }
    }
}
