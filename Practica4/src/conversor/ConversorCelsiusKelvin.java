package Practica4.src.conversor;

import Practica4.src.sensor.Unidad;

/**
 * Conversor de Celsius a Kelvin.
 * La fórmula de conversión es: K = C + 273.15
 */
public class ConversorCelsiusKelvin implements Conversor {
  /**
   * Implementa la funcion de la interfaz
   * @return Unidad de origen, Celsius
   */
  @Override
  public Unidad getUnidadOrigen() {
    return Unidad.CELSIUS;
  }
  /**
   * Implementa la funcion de la interfaz
   * @return Unidad de destino, Kelvin
   */
  @Override
  public Unidad getUnidadDestino() {
    return Unidad.KELVIN;
  }

  /**
   * Convierte el valor de celsius a kelvin
   * @return double el valor de celsius convertido en kelvin
   */
  @Override
  public double convertir(double valor) {
    return valor + 273.15;
  }
}