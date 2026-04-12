package Practica4.src.excepcion;

import Practica4.src.sensor.Sensor;

/**
 * Excepción que se lanza cuando un sensor ha superado su periodo de validez
 * de calibración.
 */
public class SensorDescalibradoPorCaducidadException extends SensorDescalibradoException {

  /**
   * Constructor que crea una nueva excepción por caducidad de calibración.
   * 
   * @param sensor Sensor que ha alcanzado la fecha límite de calibración.
   */
  public SensorDescalibradoPorCaducidadException(Sensor sensor) {
    super(sensor, "Fecha de caducidad de calibracion alcanzada");
  }

}