package repository;

import model.Dispositivo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryDispositivoRepository
        implements DispositivoRepository {

    private final Map<String, Dispositivo> dispositivos =
            new LinkedHashMap<>();

    @Override
    public List<Dispositivo> findAll() {
        return new ArrayList<>(dispositivos.values());
    }

    @Override
    public Optional<Dispositivo> findById(String id) {
        String normalizedId = normalizeId(id);
        return Optional.ofNullable(dispositivos.get(normalizedId));
    }

    @Override
    public boolean existsById(String id) {
        String normalizedId = normalizeId(id);
        return dispositivos.containsKey(normalizedId);
    }

    @Override
    public Dispositivo save(Dispositivo dispositivo) {
        Objects.requireNonNull(
                dispositivo,
                "Device cannot be null."
        );

        String normalizedId = normalizeId(dispositivo.getId());

        if (!normalizedId.equals(dispositivo.getId())) {
            dispositivo.setId(normalizedId);
        }

        dispositivos.put(normalizedId, dispositivo);
        return dispositivo;
    }

    @Override
    public boolean deleteById(String id) {
        String normalizedId = normalizeId(id);
        Dispositivo removedDevice =
                dispositivos.remove(normalizedId);

        if (removedDevice == null) {
            return false;
        }

        if (removedDevice.getHabitacion() != null) {
            removedDevice.setHabitacion(null);
        }

        return true;
    }

    @Override
    public long count() {
        return dispositivos.size();
    }

    @Override
    public void clear() {
        List<Dispositivo> registeredDevices =
                new ArrayList<>(dispositivos.values());

        for (Dispositivo dispositivo : registeredDevices) {
            if (dispositivo.getHabitacion() != null) {
                dispositivo.setHabitacion(null);
            }
        }

        dispositivos.clear();
    }

    private static String normalizeId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Device id cannot be empty."
            );
        }

        return id.trim();
    }
}