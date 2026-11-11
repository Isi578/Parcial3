package service;

import model.Notificacion;
import patterns.observer.NotificacionObserver;

import java.util.List;
import java.util.Objects;

public class NotificacionService {
    private final NotificacionObserver notificacionObserver;

    public NotificacionService(NotificacionObserver notificacionObserver) {
        this.notificacionObserver = Objects.requireNonNull(
                notificacionObserver,
                "Notification observer cannot be null."
        );
    }

    public List<Notificacion> obtenerNotificaciones() {
        return List.copyOf(notificacionObserver.getNotifications());
    }
}
