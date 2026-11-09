package service;

import model.Automatizacion;
import repository.AutomatizacionRepository;

import java.util.List;
import java.util.Optional;

public class AutomatizacionService {
    private final AutomatizacionRepository automatizacionRepository;

    public AutomatizacionService(AutomatizacionRepository automatizacionRepository) {
        this.automatizacionRepository = automatizacionRepository;
    }

    public List<Automatizacion> obtenerAutomatizaciones() { return null; }
    public Optional<Automatizacion> obtenerAutomatizacion(String id) { return null; }
    public void registrarAutomatizacion(Automatizacion automatizacion) { }
    public void actualizarAutomatizacion(Automatizacion automatizacion) { }
    public void eliminarAutomatizacion(String id) { }
}
