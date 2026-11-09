package patterns.composite;

import model.Dispositivo;

public class DispositivoIndividual implements ComponenteHogar {
    private Dispositivo dispositivo;

    public DispositivoIndividual(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }
    @Override public String getNombre() { return null; }
    @Override public void agregar(ComponenteHogar componente) { }
    @Override public void eliminar(ComponenteHogar componente) { }
}
