package repository;

import model.Dispositivo;

import java.util.List;
import java.util.Optional;

public interface DispositivoRepository {

    List<Dispositivo> findAll();

    Optional<Dispositivo> findById(String id);

    boolean existsById(String id);

    Dispositivo save(Dispositivo dispositivo);

    boolean deleteById(String id);

    long count();

    void clear();
}