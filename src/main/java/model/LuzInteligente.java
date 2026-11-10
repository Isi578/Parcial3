package model;

import patterns.state.EstadoDispositivo;

public class LuzInteligente extends Dispositivo {

    public static final int MIN_BRIGHTNESS = 0;
    public static final int MAX_BRIGHTNESS = 100;
    public static final String DEFAULT_COLOR = "WHITE";

    private int nivelBrillo;
    private String color = DEFAULT_COLOR;

    public LuzInteligente() {
        super();
    }

    public LuzInteligente(
            String id,
            String nombre,
            String marca,
            String modelo,
            EstadoDispositivo estado,
            Habitacion habitacion,
            int nivelBrillo,
            String color) {

        super(id, nombre, marca, modelo, estado, habitacion);
        setNivelBrillo(nivelBrillo);
        setColor(color);
    }

    public void increaseBrightness(int amount) {
        validatePositiveAmount(amount);

        int newBrightness = Math.min(
                MAX_BRIGHTNESS,
                nivelBrillo + amount
        );

        setNivelBrillo(newBrightness);

        if (newBrightness > MIN_BRIGHTNESS && !isActivo()) {
            turnOn();
        }
    }

    public void decreaseBrightness(int amount) {
        validatePositiveAmount(amount);

        int newBrightness = Math.max(
                MIN_BRIGHTNESS,
                nivelBrillo - amount
        );

        setNivelBrillo(newBrightness);
    }

    @Override
    protected void onTurnedOn() {
        if (nivelBrillo == MIN_BRIGHTNESS) {
            nivelBrillo = MAX_BRIGHTNESS;
        }
    }

    @Override
    public String getDeviceType() {
        return "SMART_LIGHT";
    }

    @Override
    public String getStatusDescription() {
        if (!isActivo()) {
            return "OFF";
        }

        return "ON - Brightness: "
                + nivelBrillo
                + "% - Color: "
                + color;
    }

    public int getNivelBrillo() {
        return nivelBrillo;
    }

    public void setNivelBrillo(int nivelBrillo) {
        validateBrightness(nivelBrillo);
        this.nivelBrillo = nivelBrillo;

        if (nivelBrillo == MIN_BRIGHTNESS && isActivo()) {
            turnOff();
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null || color.isBlank()) {
            this.color = DEFAULT_COLOR;
            return;
        }

        this.color = color.trim().toUpperCase();
    }

    private static void validateBrightness(int brightness) {
        if (brightness < MIN_BRIGHTNESS
                || brightness > MAX_BRIGHTNESS) {

            throw new IllegalArgumentException(
                    "Brightness must be between "
                            + MIN_BRIGHTNESS
                            + " and "
                            + MAX_BRIGHTNESS
                            + "."
            );
        }
    }

    private static void validatePositiveAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Brightness adjustment must be greater than zero."
            );
        }
    }
}