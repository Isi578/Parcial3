package patterns.facade;

import model.Dispositivo;
import model.Habitacion;
import patterns.command.ApagarDispositivoCommand;
import patterns.command.Comando;
import patterns.command.ControladorComandos;
import patterns.command.EncenderDispositivoCommand;
import service.DispositivoService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class HogarFacade {

    private final DispositivoService deviceService;
    private final ControladorComandos commandController;

    public HogarFacade(
            DispositivoService deviceService,
            ControladorComandos commandController) {

        this.deviceService = Objects.requireNonNull(
                deviceService,
                "Device service cannot be null."
        );

        this.commandController = Objects.requireNonNull(
                commandController,
                "Command controller cannot be null."
        );
    }

    public Dispositivo registerDevice(
            Dispositivo device) {

        return deviceService.registerDevice(device);
    }

    public Dispositivo updateDevice(
            Dispositivo device) {

        return deviceService.updateDevice(device);
    }

    public boolean deleteDevice(String deviceId) {
        requireId(deviceId);
        return deviceService.deleteDevice(deviceId);
    }

    public List<Dispositivo> getAllDevices() {
        return deviceService.getAllDevices();
    }

    public Optional<Dispositivo> findDeviceById(
            String deviceId) {

        requireId(deviceId);
        return deviceService.getDeviceById(deviceId);
    }

    public Dispositivo getRequiredDevice(
            String deviceId) {

        return findDeviceById(deviceId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Device with id '"
                                        + deviceId.trim()
                                        + "' is not registered."
                        )
                );
    }

    public boolean turnOnDevice(String deviceId) {
        Dispositivo device =
                getRequiredDevice(deviceId);

        boolean previousState = device.isActivo();

        commandController.ejecutarComando(
                new EncenderDispositivoCommand(device)
        );

        return previousState != device.isActivo();
    }

    public boolean turnOffDevice(String deviceId) {
        Dispositivo device =
                getRequiredDevice(deviceId);

        boolean previousState = device.isActivo();

        commandController.ejecutarComando(
                new ApagarDispositivoCommand(device)
        );

        return previousState != device.isActivo();
    }

    public Dispositivo assignDeviceToRoom(
            String deviceId,
            Habitacion room) {

        requireId(deviceId);

        Objects.requireNonNull(
                room,
                "Room cannot be null."
        );

        return deviceService.assignDeviceToRoom(
                deviceId,
                room
        );
    }

    public Dispositivo removeDeviceFromRoom(
            String deviceId) {

        requireId(deviceId);

        return deviceService.removeDeviceFromRoom(
                deviceId
        );
    }

    public Optional<Comando> undoLastCommand() {
        return commandController
                .deshacerUltimoComando();
    }

    public Optional<String> getLastCommandDescription() {
        return commandController
                .getUltimoComando()
                .map(Comando::getDescription);
    }

    public boolean canUndo() {
        return commandController
                .hasUndoableCommands();
    }

    public int getUndoableCommandCount() {
        return commandController
                .getCommandCount();
    }

    public void clearCommandHistory() {
        commandController.clearHistory();
    }

    public long countDevices() {
        return deviceService.countDevices();
    }

    public long countActiveDevices() {
        return deviceService.countActiveDevices();
    }

    public String getDeviceSummary() {
        return countActiveDevices()
                + " of "
                + countDevices()
                + " devices are active";
    }

    public DispositivoService getDeviceService() {
        return deviceService;
    }

    public ControladorComandos getCommandController() {
        return commandController;
    }

    private static void requireId(String deviceId) {
        if (deviceId == null
                || deviceId.isBlank()) {

            throw new IllegalArgumentException(
                    "Device id cannot be empty."
            );
        }
    }
}