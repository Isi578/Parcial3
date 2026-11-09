package service;

import model.Dispositivo;
import repository.DispositivoRepository;

import java.util.List;
import java.util.Optional;

public class DispositivoService {
    private final DispositivoRepository dispositivoRepository;

    public DispositivoService(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    public List<Dispositivo> obtenerDispositivos() { return null; }
    public Optional<Dispositivo> obtenerDispositivo(String id) { return null; }
    public void registrarDispositivo(Dispositivo dispositivo) { }
    public void actualizarDispositivo(Dispositivo dispositivo) { }
    public void eliminarDispositivo(String id) { }
}
