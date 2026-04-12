package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

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
    return UnidadTemperatura.CELSIUS;
  }
  /**
   * Implementa la funcion de la interfaz
   * @return Unidad de destino, Kelvin
   */
  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.KELVIN;
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