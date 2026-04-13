package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Implementación de conversor para transformar la escala Kelvin a grados
 * Celsius.
 * Aplica la operación inversa a la escala Celsius restando la constante 273.15.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorKelvinCelsius implements Conversor {
  /**
   * Implementa la función de la interfaz
   * devuelve la unidad de origen, Kelvin
   * @return Unidad de origen, Kelvin
   */
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.KELVIN;
  }

  /**
   * Implementa la función de la interfaz
   * devuelve la unidad de destino, Celsius
   * @return Unidad de destino, Celsius
   */
  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.CELSIUS;
  }
  /**
   * Convierte el valor de Kelvin a Celsius utilizando la fórmula de conversión
   * estándar.
   * 
   * @return double el valor de Kelvin convertido en Celsius
   */
  @Override
  public double convertir(double valor) {
    return valor - 273.15;
  }
}