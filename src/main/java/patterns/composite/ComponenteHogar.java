package patterns.composite;

public interface ComponenteHogar {
    String getNombre();
    void agregar(ComponenteHogar componente);
    void eliminar(ComponenteHogar componente);
}
