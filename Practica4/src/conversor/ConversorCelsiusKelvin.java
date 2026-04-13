package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Implementación de conversor para transformar grados Celsius a escala Kelvin.
 * Aplica la fórmula estándar sumando la constante 273.15 al valor de entrada.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorCelsiusKelvin implements Conversor {
  /**
   * Implementa la funcion de la interfaz
   * 
   * @return Unidad de origen, Celsius
   */
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.CELSIUS;
  }

  /**
   * Implementa la funcion de la interfaz
   * 
   * @return Unidad de destino, Kelvin
   */
  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.KELVIN;
  }

  /**
   * Convierte el valor de celsius a kelvin
   * 
   * @return double el valor de celsius convertido en kelvin
   */
  @Override
  public double convertir(double valor) {
    return valor + 273.15;
  }
}