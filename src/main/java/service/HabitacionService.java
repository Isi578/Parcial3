package service;

import model.Habitacion;
import repository.HabitacionRepository;

import java.util.List;
import java.util.Optional;

public class HabitacionService {
    private final HabitacionRepository habitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    public List<Habitacion> obtenerHabitaciones() { return null; }
    public Optional<Habitacion> obtenerHabitacion(String id) { return null; }
    public void registrarHabitacion(Habitacion habitacion) { }
    public void actualizarHabitacion(Habitacion habitacion) { }
    public void eliminarHabitacion(String id) { }
}
