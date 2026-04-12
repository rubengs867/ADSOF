package Practica4.src.excepcion;

import Practica4.src.sensor.Sensor;

/**
 * Excepción que se lanza cuando un sensor realiza una medición que excede
 * sus límites operativos permitidos.
 * <p>
 * Al producirse esta excepción, el sensor se marca automáticamente como
 * no calibrado para evitar futuras mediciones erróneas.
 * </p>
 */
public class SensorDescalibradoPorRangoException extends SensorDescalibradoException {

  /** Porcentaje de desviación con respecto al intervalo */
  private double desviacion;

  /**
   * Constructor que crea una nueva excepción por exceso de rango.
   * <p>
   * Además de generar el mensaje de error, establece el estado del sensor
   * como descalibrado.
   * </p>
   * 
   * @param sensor Sensor que ha generado un valor fuera de rango.
   */
  public SensorDescalibradoPorRangoException(Sensor sensor, double desviacion) {
    super(sensor, "Lectura fuera de rango en " + sensor.getId() + ": " + String.format("%.2f", desviacion) + "%");
    sensor.setCalibrado(false);
    this.desviacion = desviacion;
  }

  /**
   * Devuelve el pordentaje de desviación con respecto al intervalo.
   * Ejemplo: Si el rango es [10, 20] y el valor es 30: El exceso es 10. La
   * desviación es 10/10 = 100$ (se ha alejado una distancia igual a todo el
   * rango).
   * 
   * @return porcentaje de desviación
   */
  public double getDesviacion() {
    return desviacion;
  }

}