package patterns.strategy;

import model.Automatizacion;

public class EstrategiaPorTemperatura implements EstrategiaAutomatizacion {
    private double temperaturaUmbral;

    public EstrategiaPorTemperatura() {
    }

    public EstrategiaPorTemperatura(double temperaturaUmbral) {
        this.temperaturaUmbral = temperaturaUmbral;
    }

    public double getTemperaturaUmbral() { return temperaturaUmbral; }
    public void setTemperaturaUmbral(double temperaturaUmbral) { this.temperaturaUmbral = temperaturaUmbral; }

    @Override
    public void ejecutar(Automatizacion automatizacion) {
    }
}
