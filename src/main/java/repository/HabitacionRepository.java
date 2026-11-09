package repository;

import model.Habitacion;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository {
    List<Habitacion> buscarTodas();
    Optional<Habitacion> buscarPorId(String id);
    void guardar(Habitacion habitacion);
    void eliminar(String id);
}
