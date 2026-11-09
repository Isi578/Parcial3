package patterns.command;

import model.CerraduraInteligente;

public class DesbloquearPuertaCommand implements Comando {
    private CerraduraInteligente cerradura;

    public DesbloquearPuertaCommand(CerraduraInteligente cerradura) {
        this.cerradura = cerradura;
    }

    public CerraduraInteligente getCerradura() { return cerradura; }
    public void setCerradura(CerraduraInteligente cerradura) { this.cerradura = cerradura; }

    @Override
    public void ejecutar() {
    }
}
