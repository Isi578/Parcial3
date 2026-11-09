package patterns.mediator;

public abstract class ComponenteHogar {
    private MediadorHogar mediador;

    protected ComponenteHogar(MediadorHogar mediador) {
        this.mediador = mediador;
    }

    public MediadorHogar getMediador() { return mediador; }
    public void setMediador(MediadorHogar mediador) { this.mediador = mediador; }
    public abstract void recibirMensaje(String mensaje);
}
