package estrategia;

import sensor.Sensor;

/**
 * Implementación de {@link EstrategiaLectura} que genera valores dependientes
 * de la última medición.
 * <p>
 * Se basa únicamente en el valor inmediatamente anterior para calcular el
 * siguiente, aplicando un delta de variación.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class EstrategiaValorCercano implements EstrategiaLectura {

  /** Fracción de variación permitida */
  private double variacionPermitida;

  /**
   * Contructor base
   * 
   * @param variacionPermitida Variación que se aplica a la lectura inmediatamente
   *                           anterior para obtener la siguiente.
   */
  public EstrategiaValorCercano(double variacionPermitida) {
    this.variacionPermitida = variacionPermitida;
  }

  @Override
  public double generarValor(Sensor s) {

    if (s.primeraLectura()) {
      return (s.getMinRango() + s.getMaxRango()) / 2.0;
    }

    double valorAnterior = s.getValorUltimaLectura();

    double delta = Math.abs(valorAnterior * variacionPermitida);

    double limiteInferior = valorAnterior - delta;
    double limiteSuperior = valorAnterior + delta;

    return limiteInferior + (Math.random() * (limiteSuperior - limiteInferior));
  }
}