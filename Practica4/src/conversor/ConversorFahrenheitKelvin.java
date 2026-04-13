package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Implementación de conversor para transformar grados Fahrenheit a escala
 * Kelvin.
 * <p>
 * Realiza la conversión intermedia a Celsius para obtener el resultado final en
 * Kelvin.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorFahrenheitKelvin implements Conversor {
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.FAHRENHEIT;
  }

  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.KELVIN;
  }

  @Override
  public double convertir(double valor) {
    return (valor - 32) * 5 / 9 + 273.15;
  }
}