package repository;

import model.Automatizacion;

import java.util.List;
import java.util.Optional;

public interface AutomatizacionRepository {
    List<Automatizacion> buscarTodas();
    Optional<Automatizacion> buscarPorId(String id);
    void guardar(Automatizacion automatizacion);
    void eliminar(String id);
}
