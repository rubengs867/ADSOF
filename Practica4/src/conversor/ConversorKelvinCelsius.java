package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Implementación de conversor para transformar la escala Kelvin a grados
 * Celsius.
 * <p>
 * Aplica la operación inversa a la escala Celsius restando la constante 273.15.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorKelvinCelsius implements Conversor {
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.KELVIN;
  }

  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.CELSIUS;
  }

  @Override
  public double convertir(double valor) {
    return valor - 273.15;
  }
}