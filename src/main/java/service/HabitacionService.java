package service;

import model.Habitacion;
import repository.HabitacionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HabitacionService {
    private final HabitacionRepository habitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = Objects.requireNonNull(
                habitacionRepository,
                "Room repository cannot be null."
        );
    }

    public List<Habitacion> obtenerHabitaciones() {
        return List.copyOf(habitacionRepository.buscarTodas());
    }

    public Optional<Habitacion> obtenerHabitacion(String id) {
        return habitacionRepository.buscarPorId(requireText(id, "Room id"));
    }

    public void registrarHabitacion(Habitacion habitacion) {
        validate(habitacion);
        if (habitacionRepository.existePorId(habitacion.getId())) {
            throw new IllegalArgumentException(
                    "A room with id '" + habitacion.getId() + "' is already registered."
            );
        }
        habitacionRepository.guardar(habitacion);
    }

    public void actualizarHabitacion(Habitacion habitacion) {
        validate(habitacion);
        if (!habitacionRepository.existePorId(habitacion.getId())) {
            throw new IllegalArgumentException(
                    "Room with id '" + habitacion.getId() + "' is not registered."
            );
        }
        habitacionRepository.guardar(habitacion);
    }

    public void eliminarHabitacion(String id) {
        String validId = requireText(id, "Room id");
        Habitacion room = habitacionRepository.buscarPorId(validId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Room with id '" + validId + "' is not registered."
                ));
        if (!room.getDispositivos().isEmpty()) {
            throw new IllegalStateException(
                    "A room with assigned devices cannot be deleted."
            );
        }
        habitacionRepository.eliminar(validId);
    }

    private static void validate(Habitacion habitacion) {
        Objects.requireNonNull(habitacion, "Room cannot be null.");
        requireText(habitacion.getId(), "Room id");
        requireText(habitacion.getNombre(), "Room name");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        return value.trim();
    }
}
