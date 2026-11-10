package model;

import patterns.command.Comando;
import patterns.strategy.EstrategiaAutomatizacion;
import util.enums.EstadoAutomatizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Automatizacion {

    private String id;
    private String nombre;
    private String descripcion;
    private EstadoAutomatizacion estado;
    private EstrategiaAutomatizacion estrategia;

    private final List<Comando> comandos =
            new ArrayList<>();

    private int executionCount;

    private String lastExecutionResult =
            "Automation has not been executed.";

    public Automatizacion() {
    }

    public Automatizacion(
            String id,
            String nombre,
            String descripcion,
            EstadoAutomatizacion estado) {

        this.id = requireText(
                id,
                "Automation id"
        );

        this.nombre = requireText(
                nombre,
                "Automation name"
        );

        this.descripcion =
                normalizeDescription(descripcion);

        this.estado = estado;
    }

    public void executeStrategy() {
        EstrategiaAutomatizacion currentStrategy =
                Objects.requireNonNull(
                        estrategia,
                        "Automation strategy is not configured."
                );

        currentStrategy.ejecutar(this);
    }

    public int executeCommands() {
        if (comandos.isEmpty()) {
            lastExecutionResult =
                    "No commands are configured.";

            return 0;
        }

        int executedCommands = 0;

        for (Comando comando : comandos) {
            comando.ejecutar();
            executedCommands++;
        }

        executionCount++;

        lastExecutionResult =
                executedCommands
                        + " command(s) executed.";

        return executedCommands;
    }

    public void addCommand(Comando comando) {
        Objects.requireNonNull(
                comando,
                "Command cannot be null."
        );

        if (!comandos.contains(comando)) {
            comandos.add(comando);
        }
    }

    public boolean removeCommand(Comando comando) {
        if (comando == null) {
            return false;
        }

        return comandos.remove(comando);
    }

    public void clearCommands() {
        comandos.clear();
    }

    public boolean hasCommands() {
        return !comandos.isEmpty();
    }

    public int getCommandCount() {
        return comandos.size();
    }

    public List<Comando> getComandos() {
        return Collections.unmodifiableList(
                comandos
        );
    }

    public void setComandos(List<Comando> comandos) {
        this.comandos.clear();

        if (comandos == null) {
            return;
        }

        for (Comando comando : comandos) {
            addCommand(comando);
        }
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public String getLastExecutionResult() {
        return lastExecutionResult;
    }

    public void setLastExecutionResult(
            String lastExecutionResult) {

        this.lastExecutionResult =
                normalizeResult(lastExecutionResult);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = requireText(
                id,
                "Automation id"
        );
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = requireText(
                nombre,
                "Automation name"
        );
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion =
                normalizeDescription(descripcion);
    }

    public EstadoAutomatizacion getEstado() {
        return estado;
    }

    public void setEstado(
            EstadoAutomatizacion estado) {

        this.estado = estado;
    }

    public EstrategiaAutomatizacion getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(
            EstrategiaAutomatizacion estrategia) {

        this.estrategia = Objects.requireNonNull(
                estrategia,
                "Automation strategy cannot be null."
        );
    }

    private static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    private static String normalizeDescription(
            String description) {

        return description == null
                ? ""
                : description.trim();
    }

    private static String normalizeResult(
            String result) {

        if (result == null || result.isBlank()) {
            return "No execution result.";
        }

        return result.trim();
    }

    @Override
    public String toString() {
        String automationName =
                nombre == null || nombre.isBlank()
                        ? "Unnamed automation"
                        : nombre;

        return automationName
                + " - "
                + lastExecutionResult;
    }
}