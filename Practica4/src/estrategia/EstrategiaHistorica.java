package Practica4.src.estrategia;

import java.util.List;

import Practica4.src.sensor.Sensor;

/**
 * Estrategia que genera un valor aleatorio cercano a la media histórica
 * de todas las lecturas generadas por el sensor.
 */
public class EstrategiaHistorica implements EstrategiaLectura {

  // Fracción de variación permitida respecto a la media (ej. 0.05 para un ±5%)
  private double variacionPermitida;

  public EstrategiaHistorica(double variacionPermitida) {
    this.variacionPermitida = variacionPermitida;
  }

  @Override
  public double generarValor(Sensor s) {
    List<Double> historico = s.getHistoricoLecturas();

    // Si no hay histórico para calcular la media partimos del medio del rango
    if (s.primeraLectura()) {
      return (s.getMinRango() + s.getMaxRango()) / 2.0;
    }

    // Calcular la media de todos los valores
    double sumaTotal = 0.0;
    for (Double lectura : historico) {
      sumaTotal += lectura;
    }
    double media = sumaTotal / historico.size();

    // Calcular el margen de variación permitido
    double delta = Math.abs(media * variacionPermitida);

    double limiteInferior = media - delta;
    double limiteSuperior = media + delta;

    // Generamos el nuevo valor dentro de ese margen
    return limiteInferior + (Math.random() * (limiteSuperior - limiteInferior));
  }
}
