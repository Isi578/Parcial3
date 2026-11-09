package repository;

import model.Dispositivo;

import java.util.List;
import java.util.Optional;

public interface DispositivoRepository {
    List<Dispositivo> buscarTodos();
    Optional<Dispositivo> buscarPorId(String id);
    void guardar(Dispositivo dispositivo);
    void eliminar(String id);
}
