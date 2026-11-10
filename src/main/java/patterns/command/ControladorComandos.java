package patterns.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ControladorComandos {

    private final List<Comando> historialComandos =
            new ArrayList<>();

    public void ejecutarComando(Comando comando) {
        Objects.requireNonNull(
                comando,
                "Command cannot be null."
        );

        comando.ejecutar();

        if (comando.puedeDeshacer()) {
            historialComandos.add(comando);
        }
    }

    public Optional<Comando> deshacerUltimoComando() {
        if (historialComandos.isEmpty()) {
            return Optional.empty();
        }

        int lastIndex = historialComandos.size() - 1;
        Comando lastCommand =
                historialComandos.remove(lastIndex);

        lastCommand.deshacer();
        return Optional.of(lastCommand);
    }

    public Optional<Comando> getUltimoComando() {
        if (historialComandos.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                historialComandos.get(
                        historialComandos.size() - 1
                )
        );
    }

    public List<Comando> getHistorialComandos() {
        return Collections.unmodifiableList(
                historialComandos
        );
    }

    public int getCommandCount() {
        return historialComandos.size();
    }

    public boolean hasUndoableCommands() {
        return !historialComandos.isEmpty();
    }

    public void clearHistory() {
        historialComandos.clear();
    }
}
