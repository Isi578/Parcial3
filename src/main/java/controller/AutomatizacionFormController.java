package controller;

import app.ApplicationContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Automatizacion;
import model.Dispositivo;
import model.SensorMovimiento;
import patterns.command.ApagarDispositivoCommand;
import patterns.command.Comando;
import patterns.command.EncenderDispositivoCommand;
import patterns.strategy.EstrategiaPorMovimiento;
import util.enums.EstadoAutomatizacion;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AutomatizacionFormController {
    private static final String TURN_ON = "Turn on";
    private static final String TURN_OFF = "Turn off";

    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<SensorMovimiento> sensorComboBox;
    @FXML private ComboBox<Dispositivo> targetDeviceComboBox;
    @FXML private ComboBox<String> actionComboBox;
    @FXML private ComboBox<EstadoAutomatizacion> stateComboBox;
    @FXML private Label errorLabel;

    private Automatizacion existingAutomation;
    private Consumer<Automatizacion> onSave;

    @FXML
    private void initialize() {
        var context = ApplicationContext.getInstance();
        List<Dispositivo> devices = context.getHogarFacade().getAllDevices();
        sensorComboBox.setItems(FXCollections.observableArrayList(
                devices.stream()
                        .filter(SensorMovimiento.class::isInstance)
                        .map(SensorMovimiento.class::cast)
                        .toList()
        ));
        targetDeviceComboBox.setItems(FXCollections.observableArrayList(
                devices.stream()
                        .filter(device -> !(device instanceof model.CerraduraInteligente))
                        .toList()
        ));
        actionComboBox.setItems(FXCollections.observableArrayList(TURN_ON, TURN_OFF));
        stateComboBox.setItems(FXCollections.observableArrayList(
                EstadoAutomatizacion.ACTIVA,
                EstadoAutomatizacion.INACTIVA
        ));
        sensorComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(SensorMovimiento sensor) {
                return sensor == null ? "" : sensor.getNombre();
            }
            @Override public SensorMovimiento fromString(String value) { return null; }
        });
        targetDeviceComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(Dispositivo device) {
                return device == null ? "" : device.getNombre();
            }
            @Override public Dispositivo fromString(String value) { return null; }
        });
        stateComboBox.getSelectionModel().select(EstadoAutomatizacion.ACTIVA);
        actionComboBox.getSelectionModel().select(TURN_ON);
    }

    public void configure(Automatizacion automation) {
        existingAutomation = automation;
        if (automation == null) {
            return;
        }
        nameField.setText(automation.getNombre());
        descriptionField.setText(automation.getDescripcion());
        stateComboBox.setValue(automation.getEstado());
        if (automation.getEstrategia() instanceof EstrategiaPorMovimiento strategy) {
            sensorComboBox.setValue(strategy.getSensor());
        }
        if (!automation.getComandos().isEmpty()) {
            Comando command = automation.getComandos().getFirst();
            if (command instanceof EncenderDispositivoCommand turnOn) {
                targetDeviceComboBox.setValue(turnOn.getDispositivo());
                actionComboBox.setValue(TURN_ON);
            } else if (command instanceof ApagarDispositivoCommand turnOff) {
                targetDeviceComboBox.setValue(turnOff.getDispositivo());
                actionComboBox.setValue(TURN_OFF);
            }
        }
    }

    public void setOnSave(Consumer<Automatizacion> onSave) {
        this.onSave = onSave;
    }

    public void showError(String message) {
        errorLabel.setText(message == null
                ? "The automation could not be saved."
                : message);
    }

    @FXML
    private void guardar() {
        try {
            if (onSave == null) {
                throw new IllegalStateException("The form is not connected to its screen.");
            }
            onSave.accept(buildAutomation());
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        if (nameField.getScene() != null
                && nameField.getScene().getWindow() instanceof Stage stage) {
            stage.close();
        }
    }

    private Automatizacion buildAutomation() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Automation name cannot be empty.");
        }
        SensorMovimiento sensor = sensorComboBox.getValue();
        Dispositivo target = targetDeviceComboBox.getValue();
        if (sensor == null) {
            throw new IllegalArgumentException("Select a motion sensor.");
        }
        if (target == null) {
            throw new IllegalArgumentException("Select a target device.");
        }
        if (actionComboBox.getValue() == null || stateComboBox.getValue() == null) {
            throw new IllegalArgumentException("Select an action and status.");
        }

        Automatizacion automation = existingAutomation == null
                ? new Automatizacion(
                        UUID.randomUUID().toString(),
                        name.trim(),
                        descriptionField.getText(),
                        stateComboBox.getValue()
                )
                : existingAutomation;
        automation.setNombre(name.trim());
        automation.setDescripcion(descriptionField.getText());
        automation.setEstado(stateComboBox.getValue());
        automation.setEstrategia(new EstrategiaPorMovimiento(sensor));
        automation.clearCommands();
        automation.addCommand(TURN_ON.equals(actionComboBox.getValue())
                ? new EncenderDispositivoCommand(target)
                : new ApagarDispositivoCommand(target));
        return automation;
    }
}
