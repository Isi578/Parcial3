package patterns.command;

import model.CerraduraInteligente;

public class BloquearPuertaCommand implements Comando {
    private CerraduraInteligente cerradura;

    public BloquearPuertaCommand(CerraduraInteligente cerradura) {
        this.cerradura = cerradura;
    }

    public CerraduraInteligente getCerradura() { return cerradura; }
    public void setCerradura(CerraduraInteligente cerradura) { this.cerradura = cerradura; }

    @Override
    public void ejecutar() {
    }
}
