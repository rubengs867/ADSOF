package Practica4.src.excepcion;

import Practica4.src.sensor.Sensor;

/**
 * Excepción que se lanza cuando se intenta realizar una medición con un sensor
 * que no está correctamente calibrado.
 * Esta excepción proporciona acceso al sensor afectado para su posterior
 * gestión o recalibración por parte de la estación meteorológica.
 */
public class SensorDescalibradoException extends Exception {

  /** Sensor que ha provocado la alerta de descalibración. */
  private Sensor sensor;

  /**
   * Constructor que crea una nueva excepción de sensor descalibrado.
   *
   * @param sensor Sensor que presenta el problema de calibración.
   * @param motivo Descripción detallada del problema 
   */
  public SensorDescalibradoException(Sensor sensor, String motivo) {
    super("Alerta en sensor " + sensor.getId() + ": " + motivo);
    this.sensor = sensor;
  }

  /**
   * Devuelve el sensor que requiere calibración.
   *
   * @return Sensor descalibrado.
   */
  public Sensor getSensor() {
    return sensor;
  }

  public String getMessage(){
    return this.sensor.getId() + "error de calibracion";
  }
}