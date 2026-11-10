package service;

import model.Dispositivo;
import model.Habitacion;
import repository.DispositivoRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;

    public DispositivoService(
            DispositivoRepository dispositivoRepository) {

        this.dispositivoRepository = Objects.requireNonNull(
                dispositivoRepository,
                "Device repository cannot be null."
        );
    }

    public List<Dispositivo> getAllDevices() {
        return dispositivoRepository.findAll();
    }

    public Optional<Dispositivo> getDeviceById(String id) {
        return dispositivoRepository.findById(id);
    }

    public Dispositivo registerDevice(Dispositivo dispositivo) {
        validateDevice(dispositivo);

        if (dispositivoRepository.existsById(
                dispositivo.getId())) {

            throw new IllegalArgumentException(
                    "A device with id '"
                            + dispositivo.getId()
                            + "' is already registered."
            );
        }

        return dispositivoRepository.save(dispositivo);
    }

    public Dispositivo updateDevice(Dispositivo dispositivo) {
        validateDevice(dispositivo);

        if (!dispositivoRepository.existsById(
                dispositivo.getId())) {

            throw new IllegalArgumentException(
                    "Device with id '"
                            + dispositivo.getId()
                            + "' is not registered."
            );
        }

        return dispositivoRepository.save(dispositivo);
    }

    public boolean deleteDevice(String id) {
        return dispositivoRepository.deleteById(id);
    }

    public Dispositivo assignDeviceToRoom(
            String deviceId,
            Habitacion habitacion) {

        Objects.requireNonNull(
                habitacion,
                "Room cannot be null."
        );

        Dispositivo dispositivo = dispositivoRepository
                .findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Device with id '"
                                + deviceId
                                + "' is not registered."
                ));

        habitacion.addDevice(dispositivo);
        return dispositivo;
    }

    public Dispositivo removeDeviceFromRoom(String deviceId) {
        Dispositivo dispositivo = dispositivoRepository
                .findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Device with id '"
                                + deviceId
                                + "' is not registered."
                ));

        dispositivo.setHabitacion(null);
        return dispositivo;
    }

    public long countDevices() {
        return dispositivoRepository.count();
    }

    public long countActiveDevices() {
        return dispositivoRepository.findAll()
                .stream()
                .filter(Dispositivo::isActivo)
                .count();
    }

    public void clearDevices() {
        dispositivoRepository.clear();
    }

    private static void validateDevice(
            Dispositivo dispositivo) {

        Objects.requireNonNull(
                dispositivo,
                "Device cannot be null."
        );

        requireText(dispositivo.getId(), "Device id");
        requireText(dispositivo.getNombre(), "Device name");
        requireText(dispositivo.getMarca(), "Device brand");
        requireText(dispositivo.getModelo(), "Device model");
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