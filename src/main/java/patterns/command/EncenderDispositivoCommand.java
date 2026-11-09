package patterns.command;

import model.Dispositivo;

public class EncenderDispositivoCommand implements Comando {
    private Dispositivo dispositivo;

    public EncenderDispositivoCommand(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }

    @Override
    public void ejecutar() {
    }
}
