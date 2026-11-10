package app;

import patterns.command.ControladorComandos;
import patterns.facade.HogarFacade;
import patterns.singleton.ConfiguracionHogar;
import repository.DispositivoRepository;
import repository.InMemoryDispositivoRepository;
import service.DispositivoService;

public final class ApplicationContext {

    private final DispositivoRepository dispositivoRepository;
    private final DispositivoService dispositivoService;
    private final ControladorComandos commandController;
    private final HogarFacade hogarFacade;
    private final ConfiguracionHogar homeConfiguration;

    private ApplicationContext() {
        this.dispositivoRepository =
                new InMemoryDispositivoRepository();

        this.dispositivoService =
                new DispositivoService(
                        dispositivoRepository
                );

        this.commandController =
                new ControladorComandos();

        this.hogarFacade =
                new HogarFacade(
                        dispositivoService,
                        commandController
                );

        this.homeConfiguration =
                ConfiguracionHogar.getInstance();
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
}