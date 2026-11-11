package controller;

import app.ApplicationContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.CerraduraInteligente;
import model.Dispositivo;
import model.Habitacion;
import model.LuzInteligente;
import model.SensorMovimiento;
import model.Termostato;
import patterns.factory.CerraduraFactory;
import patterns.factory.LuzFactory;
import patterns.factory.SensorFactory;
import patterns.factory.TermostatoFactory;
import patterns.state.EstadoApagado;
import patterns.state.EstadoDesbloqueado;
import patterns.state.EstadoEncendido;
import java.util.UUID;
import java.util.function.Consumer;

public class DispositivoFormController {
    private static final String LIGHT = "Smart light";
    private static final String SENSOR = "Motion sensor";
    private static final String THERMOSTAT = "Thermostat";
    private static final String LOCK = "Smart lock";

    @FXML private TextField nameField;
    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<Habitacion> roomComboBox;
    @FXML private ComboBox<String> stateComboBox;
    @FXML private Label errorLabel;

    private Dispositivo existingDevice;
    private Consumer<Dispositivo> onSave;

    @FXML
    private void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(
                LIGHT, SENSOR, THERMOSTAT, LOCK
        ));
        roomComboBox.setItems(FXCollections.observableArrayList(
                ApplicationContext.getInstance()
                        .getHabitacionService().obtenerHabitaciones()
        ));
        roomComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(Habitacion room) {
                return room == null ? "No room" : room.getNombre();
            }
            @Override public Habitacion fromString(String value) { return null; }
        });
        typeComboBox.valueProperty().addListener(
                (observable, oldType, newType) -> refreshStateOptions(newType)
        );
        typeComboBox.getSelectionModel().selectFirst();
        roomComboBox.getSelectionModel().select(null);
    }

    public void configure(Dispositivo device) {
        existingDevice = device;
        if (device == null) {
            return;
        }
        nameField.setText(device.getNombre());
        brandField.setText(device.getMarca());
        modelField.setText(device.getModelo());
        typeComboBox.setValue(typeName(device));
        typeComboBox.setDisable(true);
        roomComboBox.setValue(device.getHabitacion());
        stateComboBox.setValue(device instanceof CerraduraInteligente
                ? (device.getEstado().isLocked() ? "Locked" : "Unlocked")
                : (device.isActivo() ? "On" : "Off"));
    }

    public void setOnSave(Consumer<Dispositivo> onSave) {
        this.onSave = onSave;
    }

    public void showError(String message) {
        errorLabel.setText(message == null
                ? "The device could not be saved."
                : message);
    }

    @FXML
    private void guardar() {
        try {
            if (onSave == null) {
                throw new IllegalStateException("The form is not connected to its screen.");
            }
            onSave.accept(buildDevice());
        } catch (RuntimeException exception) {
            errorLabel.setText(exception.getMessage() == null
                    ? "The device could not be saved."
                    : exception.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        closeWindow();
    }

    private Dispositivo buildDevice() {
        String name = requireText(nameField.getText(), "Device name");
        String brand = requireText(brandField.getText(), "Brand");
        String model = requireText(modelField.getText(), "Model");
        Habitacion room = roomComboBox.getValue();
        String selectedType = typeComboBox.getValue();
        if (selectedType == null) {
            throw new IllegalArgumentException("Select a device type.");
        }

        Dispositivo device = existingDevice;
        if (device == null) {
            String id = UUID.randomUUID().toString();
            device = switch (selectedType) {
                case LIGHT -> new LuzFactory().createDevice(id, name, brand, model, room);
                case SENSOR -> new SensorFactory().createDevice(id, name, brand, model, room);
                case THERMOSTAT -> new TermostatoFactory().createDevice(id, name, brand, model, room);
                case LOCK -> new CerraduraFactory().createDevice(id, name, brand, model, room);
                default -> throw new IllegalArgumentException("Unsupported device type.");
            };
        } else {
            device.setNombre(name);
            device.setMarca(brand);
            device.setModelo(model);
            device.setHabitacion(room);
        }

        String selectedState = stateComboBox.getValue();
        if (device instanceof CerraduraInteligente lock) {
            lock.setBloqueada("Locked".equals(selectedState));
        } else if (selectedState != null) {
            device.setActivo("On".equals(selectedState));
        }
        return device;
    }

    private void refreshStateOptions(String type) {
        if (stateComboBox == null) {
            return;
        }
        stateComboBox.setItems(FXCollections.observableArrayList(
                LOCK.equals(type) ? "Unlocked" : "Off",
                LOCK.equals(type) ? "Locked" : "On"
        ));
        if (stateComboBox.getValue() == null) {
            stateComboBox.getSelectionModel().selectFirst();
        }
    }

    private static String typeName(Dispositivo device) {
        if (device instanceof LuzInteligente) return LIGHT;
        if (device instanceof SensorMovimiento) return SENSOR;
        if (device instanceof Termostato) return THERMOSTAT;
        if (device instanceof CerraduraInteligente) return LOCK;
        throw new IllegalArgumentException("Unsupported device type.");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        return value.trim();
    }

    private void closeWindow() {
        if (nameField.getScene() != null
                && nameField.getScene().getWindow() instanceof Stage stage) {
            stage.close();
        }
    }
}
