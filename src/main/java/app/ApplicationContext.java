package app;

import patterns.command.ControladorComandos;
import patterns.facade.HogarFacade;
import patterns.mediator.HogarMediator;
import patterns.observer.HistorialObserver;
import patterns.observer.NotificacionObserver;
import patterns.singleton.ConfiguracionHogar;
import repository.DispositivoRepository;
import repository.InMemoryDispositivoRepository;
import service.DispositivoService;

public final class ApplicationContext {

    private final DispositivoRepository dispositivoRepository;
    private final DispositivoService dispositivoService;
    private final ControladorComandos commandController;
    private final ConfiguracionHogar homeConfiguration;
    private final HistorialObserver historyObserver;
    private final NotificacionObserver notificationObserver;
    private final HogarFacade hogarFacade;
    private final HogarMediator hogarMediator;

    private ApplicationContext() {
        this.dispositivoRepository =
                new InMemoryDispositivoRepository();

        this.dispositivoService =
                new DispositivoService(
                        dispositivoRepository
                );

        this.commandController =
                new ControladorComandos();

        this.homeConfiguration =
                ConfiguracionHogar.getInstance();

        this.historyObserver =
                new HistorialObserver();

        this.notificationObserver =
                new NotificacionObserver();

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

    public DispositivoService getDispositivoService() {
        return dispositivoService;
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
}