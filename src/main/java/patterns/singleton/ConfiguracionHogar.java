package patterns.singleton;

import java.util.Objects;

public final class ConfiguracionHogar {

    public static final String DEFAULT_HOME_NAME =
            "My Smart Home";

    public static final String DEFAULT_ADDRESS =
            "Not configured";

    public static final String DEFAULT_TEMPERATURE_UNIT =
            "CELSIUS";

    private String homeName;
    private String address;
    private String temperatureUnit;
    private boolean notificationsEnabled;
    private boolean energySavingEnabled;

    private ConfiguracionHogar() {
        restoreDefaults();
    }

    private static final class InstanceHolder {

        private static final ConfiguracionHogar INSTANCE =
                new ConfiguracionHogar();

        private InstanceHolder() {
        }
    }

    public static ConfiguracionHogar getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void updateHomeInformation(
            String homeName,
            String address) {

        setHomeName(homeName);
        setAddress(address);
    }

    public void restoreDefaults() {
        this.homeName = DEFAULT_HOME_NAME;
        this.address = DEFAULT_ADDRESS;
        this.temperatureUnit =
                DEFAULT_TEMPERATURE_UNIT;

        this.notificationsEnabled = true;
        this.energySavingEnabled = false;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = requireText(
                homeName,
                "Home name"
        );
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = requireText(
                address,
                "Home address"
        );
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(
            String temperatureUnit) {

        String normalizedUnit = requireText(
                temperatureUnit,
                "Temperature unit"
        ).toUpperCase();

        if (!normalizedUnit.equals("CELSIUS")
                && !normalizedUnit.equals("FAHRENHEIT")) {

            throw new IllegalArgumentException(
                    "Temperature unit must be "
                            + "CELSIUS or FAHRENHEIT."
            );
        }

        this.temperatureUnit = normalizedUnit;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(
            boolean notificationsEnabled) {

        this.notificationsEnabled =
                notificationsEnabled;
    }

    public boolean isEnergySavingEnabled() {
        return energySavingEnabled;
    }

    public void setEnergySavingEnabled(
            boolean energySavingEnabled) {

        this.energySavingEnabled =
                energySavingEnabled;
    }

    public String getConfigurationSummary() {
        return "Home: "
                + homeName
                + " | Address: "
                + address
                + " | Temperature: "
                + temperatureUnit
                + " | Notifications: "
                + enabledLabel(notificationsEnabled)
                + " | Energy saving: "
                + enabledLabel(energySavingEnabled);
    }

    private static String enabledLabel(
            boolean enabled) {

        return enabled ? "ENABLED" : "DISABLED";
    }

    private static String requireText(
            String value,
            String fieldName) {

        Objects.requireNonNull(
                value,
                fieldName + " cannot be null."
        );

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }
}
