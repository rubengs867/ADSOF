package conversor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import unidad.UnidadTemperatura;

/**
 * Banco de pruebas completo para la clase ConversorFahrenheitKelvin.
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorFahrenheitKelvinTest {

  private static final double DELTA = 0.001;

  @Test
  public void testUnidades() {
    ConversorFahrenheitKelvin c = new ConversorFahrenheitKelvin();
    assertEquals("El origen debe ser FAHRENHEIT", UnidadTemperatura.FAHRENHEIT, c.getUnidadOrigen());
    assertEquals("El destino debe ser KELVIN", UnidadTemperatura.KELVIN, c.getUnidadDestino());
  }

  @Test
  public void testConvertirMatematicas() {
    ConversorFahrenheitKelvin c = new ConversorFahrenheitKelvin();
    assertEquals("32ºF deben ser 273.15K", 273.15, c.convertir(32.0), DELTA);
    assertEquals("212ºF deben ser 373.15K", 373.15, c.convertir(212.0), DELTA);
    assertEquals("-459.67ºF deben ser 0K (Cero absoluto)", 0.0, c.convertir(-459.67), DELTA);
  }
}