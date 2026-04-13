package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Implementación de conversor para transformar grados Fahrenheit a escala
 * Kelvin.
 * Realiza la conversión intermedia a Celsius para obtener el resultado final en
 * Kelvin.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorFahrenheitKelvin implements Conversor {
  /**
   * Implementa la función de la interfaz
   * devuelve la unidad de origen, Fahrenheit
   * @return Unidad de origen, Fahrenheit
   */
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.FAHRENHEIT;
  }
  /**
   * Implementa la función de la interfaz
   * devuelve la unidad de destino, Kelvin
   * @return Unidad de destino, Kelvin
   */
  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.KELVIN;
  }
  /**
   * Convierte el valor de Fahrenheit a Kelvin utilizando la fórmula de conversión
   * estándar.
   * 
   * @return double el valor de Fahrenheit convertido en Kelvin
   */
  @Override
  public double convertir(double valor) {
    return (valor - 32) * 5 / 9 + 273.15;
  }
}