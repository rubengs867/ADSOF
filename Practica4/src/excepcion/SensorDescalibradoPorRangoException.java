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

  /**
   * Constructor que crea una nueva excepción por exceso de rango.
   * <p>
   * Además de generar el mensaje de error, establece el estado del sensor
   * como descalibrado.
   * </p>
   * 
   * @param sensor Sensor que ha generado un valor fuera de rango.
   */
  public SensorDescalibradoPorRangoException(Sensor sensor) {
    super(sensor, "Valor final medido excede los valores permitidos");
    sensor.setCalibrado(false);
  }

}