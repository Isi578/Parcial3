package patterns.mediator;

import java.util.ArrayList;
import java.util.List;

public class HogarMediator implements MediadorHogar {
    private final List<ComponenteHogar> componentes = new ArrayList<>();

    public List<ComponenteHogar> getComponentes() { return componentes; }

    @Override
    public void registrarComponente(ComponenteHogar componente) {
    }

    @Override
    public void enviarMensaje(String mensaje, ComponenteHogar emisor) {
    }
}
