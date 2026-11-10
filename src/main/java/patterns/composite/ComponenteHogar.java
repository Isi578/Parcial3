package patterns.composite;

public interface ComponenteHogar {

    String getId();

    String getName();

    void turnOn();

    void turnOff();

    boolean isActive();

    int getDeviceCount();

    int getActiveDeviceCount();

    String getStatusDescription();
}
