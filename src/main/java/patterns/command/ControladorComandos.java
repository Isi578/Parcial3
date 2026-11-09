package patterns.command;

import java.util.ArrayList;
import java.util.List;

public class ControladorComandos {
    private final List<Comando> historialComandos = new ArrayList<>();

    public List<Comando> getHistorialComandos() { return historialComandos; }
    public void ejecutarComando(Comando comando) {
    }
    public void deshacerUltimoComando() {
    }
}
