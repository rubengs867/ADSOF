package Practica4.src.excepcion;

import Practica4.src.sensor.Sensor;

/**
 * Excepción que se lanza cuando un sensor registra una lectura válida, pero
 * que presenta un cambio muy brusco, un aumento del X% respecto a la lectura anterior
 */
public class CambioBruscoException extends Exception {

  /** Sensor que ha registrado el cambio brusco. */
  private Sensor sensor;

  /** Valor de la lectura inmediatamente anterior. */
  private double valorAnterior;

  /** Nuevo valor registrado que ha disparado la alerta. */
  private double valorActual;

  /**
   * Constructor que crea una nueva alerta por cambio brusco.
   *
   * @param sensor        Sensor que ha registrado la variación.
   * @param valorAnterior Lectura previa del sensor.
   * @param valorActual   Lectura actual que excede el umbral permitido.
   */
  public CambioBruscoException(Sensor sensor, double valorAnterior, double valorActual) {
    super("Cambio brusco en sensor " + sensor.getId() + ": la lectura salto de " + 
          String.format("%.2f", valorAnterior) + " a " + String.format("%.2f", valorActual));
    this.sensor = sensor;
    this.valorAnterior = valorAnterior;
    this.valorActual = valorActual;
  }

  /**
   * Devuelve el sensor que ha generado la alerta.
   *
   * @return Sensor afectado.
   */
  public Sensor getSensor() {
    return sensor;
  }

  /**
   * Devuelve el valor de la lectura anterior al salto.
   *
   * @return Valor numérico anterior.
   */
  public double getValorAnterior() {
    return valorAnterior;
  }

  /**
   * Devuelve el valor de la nueva lectura que provocó la alerta.
   *
   * @return Valor numérico actual.
   */
  public double getValorActual() {
    return valorActual;
  }
}