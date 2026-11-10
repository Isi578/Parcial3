package patterns.factory;

import model.Dispositivo;
import model.Habitacion;

public abstract class DispositivoFactory {

    public final Dispositivo createDevice(
            String id,
            String name,
            String brand,
            String model,
            Habitacion room) {

        String validId = requireText(id, "Device id");
        String validName = requireText(name, "Device name");
        String validBrand = requireText(brand, "Device brand");
        String validModel = requireText(model, "Device model");

        return buildDevice(
                validId,
                validName,
                validBrand,
                validModel,
                room
        );
    }

    protected abstract Dispositivo buildDevice(
            String id,
            String name,
            String brand,
            String model,
            Habitacion room);

    public abstract String getSupportedType();

    protected static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    protected static int requireRange(
            int value,
            int minimum,
            int maximum,
            String fieldName) {

        if (value < minimum || value > maximum) {
            throw new IllegalArgumentException(
                    fieldName
                            + " must be between "
                            + minimum
                            + " and "
                            + maximum
                            + "."
            );
        }

        return value;
    }
}
