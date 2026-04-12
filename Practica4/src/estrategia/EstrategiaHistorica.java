package Practica4.src.estrategia;

import java.util.Collection; 
import Practica4.src.sensor.Sensor;

/**
 * Estrategia que genera un valor aleatorio cercano a la media histórica
 * de todas las lecturas generadas por el sensor.
 */
public class EstrategiaHistorica implements EstrategiaLectura {

  /** Fracción de variación permitida respecto a la media  */
  private double variacionPermitida;

  public EstrategiaHistorica(double variacionPermitida) {
    this.variacionPermitida = variacionPermitida;
  }

  @Override
  public double generarValor(Sensor s) {

    Collection<Double> historico = s.getHistoricoLecturas();


    if (s.primeraLectura() || historico.isEmpty()) {
      return (s.getMinRango() + s.getMaxRango()) / 2.0;
    }


    double sumaTotal = 0.0;
    for (Double lectura : historico) {
      sumaTotal += lectura;
    }
    

    double media = sumaTotal / historico.size();

    double delta = Math.abs(media * variacionPermitida);

    double limiteInferior = media - delta;
    double limiteSuperior = media + delta;

 
    return limiteInferior + (Math.random() * (limiteSuperior - limiteInferior));
  }
}