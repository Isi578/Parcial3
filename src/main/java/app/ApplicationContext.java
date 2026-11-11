package app;

import model.Automatizacion;
import patterns.command.ControladorComandos;
import patterns.facade.HogarFacade;
import patterns.mediator.HogarMediator;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;
import patterns.singleton.ConfiguracionHogar;
import repository.DispositivoRepository;
import repository.AutomatizacionRepository;
import repository.HabitacionRepository;
import repository.InMemoryAutomatizacionRepository;
import repository.InMemoryDispositivoRepository;
import repository.InMemoryHabitacionRepository;
import service.AutomatizacionService;
import service.DispositivoService;
import service.EventoService;
import service.HabitacionService;
import service.NotificacionService;

public final class ApplicationContext {

    private final DispositivoRepository dispositivoRepository;
    private final HabitacionRepository habitacionRepository;
    private final AutomatizacionRepository automatizacionRepository;
    private final DispositivoService dispositivoService;
    private final HabitacionService habitacionService;
    private final AutomatizacionService automatizacionService;
    private final EventoService eventoService;
    private final NotificacionService notificacionService;
    private final ControladorComandos commandController;
    private final ConfiguracionHogar homeConfiguration;
    private final HistorialObserver historyObserver;
    private final NotificacionObserver notificationObserver;
    private final HogarFacade hogarFacade;
    private final HogarMediator hogarMediator;

    private ApplicationContext() {
        this.dispositivoRepository =
                new InMemoryDispositivoRepository();

        this.habitacionRepository =
                new InMemoryHabitacionRepository();

        this.automatizacionRepository =
                new InMemoryAutomatizacionRepository();

        this.dispositivoService =
                new DispositivoService(
                        dispositivoRepository
                );

        this.habitacionService =
                new HabitacionService(habitacionRepository);

        this.automatizacionService =
                new AutomatizacionService(automatizacionRepository);

        this.commandController =
                new ControladorComandos();

        this.homeConfiguration =
                ConfiguracionHogar.getInstance();

        this.historyObserver =
                new HistorialObserver();

        this.notificationObserver =
                new NotificacionObserver();

        this.eventoService = new EventoService(historyObserver);
        this.notificacionService =
                new NotificacionService(notificationObserver);

        synchronizeNotificationSettings();

        this.hogarFacade =
                new HogarFacade(
                        dispositivoService,
                        commandController,
                        historyObserver,
                        notificationObserver
                );

        this.hogarMediator =
                new HogarMediator();
    }

    private static final class InstanceHolder {

        private static final ApplicationContext INSTANCE =
                new ApplicationContext();

        private InstanceHolder() {
        }
    }

    public static ApplicationContext getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void synchronizeNotificationSettings() {
        notificationObserver.setEnabled(
                homeConfiguration
                        .isNotificationsEnabled()
        );
    }

    public void clearSessionActivity() {
        historyObserver.clearHistory();
        notificationObserver.clearNotifications();
        commandController.clearHistory();
    }

    public DispositivoRepository getDispositivoRepository() {
        return dispositivoRepository;
    }

    public HabitacionRepository getHabitacionRepository() {
        return habitacionRepository;
    }

    public AutomatizacionRepository getAutomatizacionRepository() {
        return automatizacionRepository;
    }

    public DispositivoService getDispositivoService() {
        return dispositivoService;
    }

    public HabitacionService getHabitacionService() {
        return habitacionService;
    }

    public AutomatizacionService getAutomatizacionService() {
        return automatizacionService;
    }

    public EventoService getEventoService() {
        return eventoService;
    }

    public NotificacionService getNotificacionService() {
        return notificacionService;
    }

    public ControladorComandos getCommandController() {
        return commandController;
    }

    public HogarFacade getHogarFacade() {
        return hogarFacade;
    }

    public ConfiguracionHogar getHomeConfiguration() {
        return homeConfiguration;
    }

    public HistorialObserver getHistoryObserver() {
        return historyObserver;
    }

    public NotificacionObserver getNotificationObserver() {
        return notificationObserver;
    }

    public HogarMediator getHogarMediator() {
        return hogarMediator;
    }

    public void registerAutomation(
            String eventType,
            Automatizacion automation) {

        automatizacionService.registrarAutomatizacion(automation);
        try {
            hogarMediator.registerAutomation(eventType, automation);
        } catch (RuntimeException exception) {
            automatizacionService.eliminarAutomatizacion(
                    automation.getId()
            );
            throw exception;
        }
    }

    public boolean removeAutomation(
            String eventType,
            String automationId) {

        boolean removedFromMediator =
                hogarMediator.removeAutomation(
                        eventType,
                        automationId
                );

        if (automatizacionService
                .obtenerAutomatizacion(automationId)
                .isPresent()) {
            automatizacionService.eliminarAutomatizacion(automationId);
        }

        return removedFromMediator;
    }
}
