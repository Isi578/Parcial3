package patterns.facade;

import model.Dispositivo;
import model.Evento;
import model.Habitacion;
import util.enums.TipoEvento;
import patterns.command.ApagarDispositivoCommand;
import patterns.command.BloquearPuertaCommand;
import patterns.command.Comando;
import patterns.command.ControladorComandos;
import patterns.command.DesbloquearPuertaCommand;
import patterns.command.EncenderDispositivoCommand;
import patterns.observer.DispositivoObservable;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;
import service.DispositivoService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class HogarFacade {

    private final DispositivoService deviceService;
    private final ControladorComandos commandController;
    private final HistorialObserver historyObserver;
    private final NotificacionObserver notificationObserver;

    private final Map<String, DispositivoObservable>
            observableDevices =
            new LinkedHashMap<>();

    public HogarFacade(
            DispositivoService deviceService,
            ControladorComandos commandController,
            HistorialObserver historyObserver,
            NotificacionObserver notificationObserver) {

        this.deviceService = Objects.requireNonNull(
                deviceService,
                "Device service cannot be null."
        );

        this.commandController = Objects.requireNonNull(
                commandController,
                "Command controller cannot be null."
        );

        this.historyObserver = Objects.requireNonNull(
                historyObserver,
                "History observer cannot be null."
        );

        this.notificationObserver =
                Objects.requireNonNull(
                        notificationObserver,
                        "Notification observer cannot be null."
                );
    }

    public Dispositivo registerDevice(
            Dispositivo device) {

        Dispositivo registeredDevice =
                deviceService.registerDevice(device);

        registerObservableDevice(
                registeredDevice
        );

        publishDeviceEvent(
                registeredDevice,
                "Device registered."
        );

        return registeredDevice;
    }

    public Dispositivo updateDevice(
            Dispositivo device) {

        Dispositivo updatedDevice =
                deviceService.updateDevice(device);

        registerObservableDevice(
                updatedDevice
        );

        publishDeviceEvent(
                updatedDevice,
                "Device information updated."
        );

        return updatedDevice;
    }

    public boolean deleteDevice(String deviceId) {
        Dispositivo device =
                getRequiredDevice(deviceId);

        boolean deleted =
                deviceService.deleteDevice(deviceId);

        if (!deleted) {
            return false;
        }

        publishDeviceEvent(
                device,
                "Device deleted."
        );

        observableDevices.remove(
                normalizeId(deviceId)
        );

        return true;
    }

    public List<Dispositivo> getAllDevices() {
        return deviceService.getAllDevices();
    }

    public Optional<Dispositivo> findDeviceById(
            String deviceId) {

        return deviceService.getDeviceById(
                normalizeId(deviceId)
        );
    }

    public Dispositivo getRequiredDevice(
            String deviceId) {

        String normalizedId =
                normalizeId(deviceId);

        return findDeviceById(normalizedId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Device with id '"
                                        + normalizedId
                                        + "' is not registered."
                        )
                );
    }

    public boolean turnOnDevice(String deviceId) {
        Dispositivo device =
                getRequiredDevice(deviceId);

        boolean previousState =
                device.isActivo();

        commandController.ejecutarComando(
                new EncenderDispositivoCommand(device)
        );

        boolean changed =
                previousState != device.isActivo();

        if (changed) {
            publishDeviceEvent(
                    device,
                    TipoEvento.ENCENDIDO,
                    "Device turned on."
            );
        }

        return changed;
    }

    public boolean turnOffDevice(String deviceId) {
        Dispositivo device =
                getRequiredDevice(deviceId);

        boolean previousState =
                device.isActivo();

        commandController.ejecutarComando(
                new ApagarDispositivoCommand(device)
        );

        boolean changed =
                previousState != device.isActivo();

        if (changed) {
            publishDeviceEvent(
                    device,
                    TipoEvento.APAGADO,
                    "Device turned off."
            );
        }

        return changed;
    }

    public Optional<Comando> undoLastCommand() {
        Optional<Comando> undoneCommand =
                commandController
                        .deshacerUltimoComando();

        undoneCommand.ifPresent(
                command -> publishUndoEvent(command)
        );

        return undoneCommand;
    }

    public Dispositivo assignDeviceToRoom(
            String deviceId,
            Habitacion room) {

        Objects.requireNonNull(
                room,
                "Room cannot be null."
        );

        Dispositivo device =
                deviceService.assignDeviceToRoom(
                        normalizeId(deviceId),
                        room
                );

        publishDeviceEvent(
                device,
                "Device assigned to room: "
                        + room.getNombre()
                        + "."
        );

        return device;
    }

    public Dispositivo removeDeviceFromRoom(
            String deviceId) {

        Dispositivo device =
                getRequiredDevice(deviceId);

        String previousRoomName =
                device.getHabitacion() == null
                        ? "No room"
                        : device
                        .getHabitacion()
                        .getNombre();

        Dispositivo updatedDevice =
                deviceService.removeDeviceFromRoom(
                        normalizeId(deviceId)
                );

        publishDeviceEvent(
                updatedDevice,
                "Device removed from room: "
                        + previousRoomName
                        + "."
        );

        return updatedDevice;
    }

    public void publishDeviceEvent(
            Dispositivo device,
            String description) {

        publishDeviceEvent(
                device,
                TipoEvento.CAMBIO_ESTADO,
                description
        );
    }

    public void publishDeviceEvent(
            Dispositivo device,
            TipoEvento eventType,
            String description) {

        Objects.requireNonNull(
                device,
                "Device cannot be null."
        );
        Objects.requireNonNull(
                eventType,
                "Event type cannot be null."
        );

        String validDescription =
                requireText(
                        description,
                        "Event description"
                );

        DispositivoObservable observable =
                getOrCreateObservable(device);

        Evento event =
                new Evento(
                        UUID.randomUUID().toString(),
                        eventType,
                        validDescription,
                        LocalDateTime.now(),
                        device
                );

        observable.publishEvent(event);
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

    public int countObservedDevices() {
        return observableDevices.size();
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

    public HistorialObserver getHistoryObserver() {
        return historyObserver;
    }

    public NotificacionObserver getNotificationObserver() {
        return notificationObserver;
    }

    private void registerObservableDevice(
            Dispositivo device) {

        String deviceId =
                normalizeId(device.getId());

        DispositivoObservable existingObservable =
                observableDevices.get(deviceId);

        if (existingObservable != null
                && existingObservable.getDispositivo()
                == device) {

            return;
        }

        DispositivoObservable observable =
                new DispositivoObservable(device);

        observable.addObserver(
                historyObserver
        );

        observable.addObserver(
                notificationObserver
        );

        observableDevices.put(
                deviceId,
                observable
        );
    }

    private DispositivoObservable getOrCreateObservable(
            Dispositivo device) {

        String deviceId =
                normalizeId(device.getId());

        DispositivoObservable observable =
                observableDevices.get(deviceId);

        if (observable == null
                || observable.getDispositivo()
                != device) {

            registerObservableDevice(device);

            observable =
                    observableDevices.get(deviceId);
        }

        return observable;
    }

    private void publishUndoEvent(
            Comando command) {

        Dispositivo relatedDevice =
                resolveCommandDevice(command);

        if (relatedDevice == null) {
            return;
        }

        publishDeviceEvent(
                relatedDevice,
                "Command undone: "
                        + command.getDescription()
                        + ". Current status: "
                        + relatedDevice
                        .getStatusDescription()
                        + "."
        );
    }

    private static Dispositivo resolveCommandDevice(
            Comando command) {

        if (command
                instanceof EncenderDispositivoCommand
                turnOnCommand) {

            return turnOnCommand.getDispositivo();
        }

        if (command
                instanceof ApagarDispositivoCommand
                turnOffCommand) {

            return turnOffCommand.getDispositivo();
        }

        if (command instanceof BloquearPuertaCommand lockCommand) {
            return lockCommand.getCerradura();
        }

        if (command instanceof DesbloquearPuertaCommand unlockCommand) {
            return unlockCommand.getCerradura();
        }

        return null;
    }

    private static String normalizeId(
            String deviceId) {

        return requireText(
                deviceId,
                "Device id"
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
}
