package app;

import repository.DispositivoRepository;
import repository.InMemoryDispositivoRepository;
import service.DispositivoService;

public final class ApplicationContext {

    private final DispositivoRepository dispositivoRepository;
    private final DispositivoService dispositivoService;

    private ApplicationContext() {
        this.dispositivoRepository =
                new InMemoryDispositivoRepository();

        this.dispositivoService =
                new DispositivoService(dispositivoRepository);
    }

    private static final class InstanceHolder {
        private static final ApplicationContext INSTANCE =
                new ApplicationContext();
    }

    public static ApplicationContext getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public DispositivoService getDispositivoService() {
        return dispositivoService;
    }

    public DispositivoRepository getDispositivoRepository() {
        return dispositivoRepository;
    }
}