package patterns.factory;

import model.Dispositivo;
import model.Habitacion;
import model.LuzInteligente;
import patterns.state.EstadoApagado;

public class LuzFactory extends DispositivoFactory {

    private int defaultBrightness;
    private String defaultColor;

    public LuzFactory() {
        this(50, LuzInteligente.DEFAULT_COLOR);
    }

    public LuzFactory(
            int defaultBrightness,
            String defaultColor) {

        this.defaultBrightness = requireRange(
                defaultBrightness,
                LuzInteligente.MIN_BRIGHTNESS,
                LuzInteligente.MAX_BRIGHTNESS,
                "Default brightness"
        );

        this.defaultColor = normalizeColor(defaultColor);
    }

    @Override
    protected Dispositivo buildDevice(
            String id,
            String name,
            String brand,
            String model,
            Habitacion room) {

        return new LuzInteligente(
                id,
                name,
                brand,
                model,
                new EstadoApagado(),
                room,
                defaultBrightness,
                defaultColor
        );
    }

    @Override
    public String getSupportedType() {
        return "SMART_LIGHT";
    }

    public int getDefaultBrightness() {
        return defaultBrightness;
    }

    public void setDefaultBrightness(
            int defaultBrightness) {

        this.defaultBrightness = requireRange(
                defaultBrightness,
                LuzInteligente.MIN_BRIGHTNESS,
                LuzInteligente.MAX_BRIGHTNESS,
                "Default brightness"
        );
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(String defaultColor) {
        this.defaultColor = normalizeColor(defaultColor);
    }

    private static String normalizeColor(String color) {
        if (color == null || color.isBlank()) {
            return LuzInteligente.DEFAULT_COLOR;
        }

        return color.trim().toUpperCase();
    }
}