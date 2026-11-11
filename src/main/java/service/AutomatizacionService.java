package service;

import model.Automatizacion;
import repository.AutomatizacionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AutomatizacionService {
    private final AutomatizacionRepository automatizacionRepository;

    public AutomatizacionService(AutomatizacionRepository automatizacionRepository) {
        this.automatizacionRepository = Objects.requireNonNull(
                automatizacionRepository,
                "Automation repository cannot be null."
        );
    }

    public List<Automatizacion> obtenerAutomatizaciones() {
        return List.copyOf(automatizacionRepository.buscarTodas());
    }

    public Optional<Automatizacion> obtenerAutomatizacion(String id) {
        return automatizacionRepository.buscarPorId(requireText(id));
    }

    public void registrarAutomatizacion(Automatizacion automatizacion) {
        validate(automatizacion);
        if (automatizacionRepository.existePorId(automatizacion.getId())) {
            throw new IllegalArgumentException(
                    "An automation with id '" + automatizacion.getId() + "' is already registered."
            );
        }
        automatizacionRepository.guardar(automatizacion);
    }

    public void actualizarAutomatizacion(Automatizacion automatizacion) {
        validate(automatizacion);
        if (!automatizacionRepository.existePorId(automatizacion.getId())) {
            throw new IllegalArgumentException(
                    "Automation with id '" + automatizacion.getId() + "' is not registered."
            );
        }
        automatizacionRepository.guardar(automatizacion);
    }

    public void eliminarAutomatizacion(String id) {
        String validId = requireText(id);
        if (!automatizacionRepository.existePorId(validId)) {
            throw new IllegalArgumentException(
                    "Automation with id '" + validId + "' is not registered."
            );
        }
        automatizacionRepository.eliminar(validId);
    }

    private static void validate(Automatizacion automatizacion) {
        Objects.requireNonNull(automatizacion, "Automation cannot be null.");
        requireText(automatizacion.getId());
        if (automatizacion.getNombre() == null || automatizacion.getNombre().isBlank()) {
            throw new IllegalArgumentException("Automation name cannot be empty.");
        }
        Objects.requireNonNull(
                automatizacion.getEstrategia(),
                "Automation strategy must be configured."
        );
    }

    private static String requireText(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Automation id cannot be empty.");
        }
        return id.trim();
    }
}
