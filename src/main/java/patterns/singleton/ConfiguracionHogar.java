package patterns.singleton;

public class ConfiguracionHogar {
    private static ConfiguracionHogar instancia;
    private String nombreHogar;
    private String direccion;

    private ConfiguracionHogar() {
    }

    public static ConfiguracionHogar getInstancia() {
        return null;
    }

    public String getNombreHogar() { return nombreHogar; }
    public void setNombreHogar(String nombreHogar) { this.nombreHogar = nombreHogar; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
