package patterns.factory;

import model.CerraduraInteligente;
import model.Dispositivo;
import model.Habitacion;
import patterns.state.EstadoDesbloqueado;

public class CerraduraFactory extends DispositivoFactory {

    public static final String DEFAULT_LOCK_TYPE = "ELECTRONIC";

    private String defaultLockType;

    public CerraduraFactory() {
        this(DEFAULT_LOCK_TYPE);
    }

    public CerraduraFactory(String defaultLockType) {
        this.defaultLockType = normalizeLockType(defaultLockType);
    }

    @Override
    protected Dispositivo buildDevice(
            String id,
            String name,
            String brand,
            String model,
            Habitacion room) {

        return new CerraduraInteligente(
                id,
                name,
                brand,
                model,
                new EstadoDesbloqueado(),
                room,
                defaultLockType,
                false
        );
    }

    @Override
    public String getSupportedType() {
        return "SMART_LOCK";
    }

    public String getDefaultLockType() {
        return defaultLockType;
    }

    public void setDefaultLockType(String defaultLockType) {
        this.defaultLockType = normalizeLockType(defaultLockType);
    }

    private static String normalizeLockType(String lockType) {
        if (lockType == null || lockType.isBlank()) {
            return DEFAULT_LOCK_TYPE;
        }

        return lockType.trim().toUpperCase();
    }
}
