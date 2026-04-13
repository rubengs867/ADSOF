package estrategia;

import java.util.Collection;
import sensor.Sensor;

/**
 * Implementación de {@link EstrategiaLectura} basada en la media de datos
 * previos.
 * <p>
 * Esta estrategia calcula el promedio de todas las lecturas almacenadas en el
 * historial del sensor y genera un nuevo valor aplicando una variación
 * porcentual permitida sobre dicha media.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class EstrategiaHistorica implements EstrategiaLectura {

  /** Fracción de variación permitida respecto a la media */
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