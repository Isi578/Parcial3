package patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class GrupoDispositivos implements ComponenteHogar {
    private String nombre;
    private final List<ComponenteHogar> componentes = new ArrayList<>();

    public GrupoDispositivos(String nombre) {
        this.nombre = nombre;
    }

    @Override public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<ComponenteHogar> getComponentes() { return componentes; }
    @Override public void agregar(ComponenteHogar componente) { }
    @Override public void eliminar(ComponenteHogar componente) { }
}
