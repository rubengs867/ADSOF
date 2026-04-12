package src.estrategia;

import src.sensor.Sensor;

public class EstrategiaAleatoria implements EstrategiaLectura {
  /** Probabilidad de generar un valor fuera del rango permitido por el sensor */
  private double probFueraRango;

  public EstrategiaAleatoria(double probFueraRango) {
    this.probFueraRango = probFueraRango;
  }

  @Override
  public double generarValor(Sensor s) {
    double min = s.getMinRango();
    double max = s.getMaxRango();

    // Vemos si forzamos un error de calibración
    if (Math.random() < probFueraRango) {
        // Genera un valor aleatorio por encima del máximo permitido (descalibrado)
        return max + 1.0 + (Math.random() * 50.0); 
    }

    // Valor aleatorio entre min y max
    return min + (Math.random() * (max - min));
}
}
