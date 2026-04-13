package conversor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import unidad.UnidadTemperatura;

public class ConversorCompuestoTest {

  private static final double DELTA = 0.001;

  @Test
  public void testUnidadesExtremos() {

    ConversorKelvinCelsius c1 = new ConversorKelvinCelsius();
    ConversorCelsiusKelvin c2 = new ConversorCelsiusKelvin();

    ConversorCompuesto compuesto = new ConversorCompuesto(c1, c2);

    assertEquals("El origen debe ser KELVIN", UnidadTemperatura.KELVIN, compuesto.getUnidadOrigen());

    assertEquals("El destino debe ser KELVIN", UnidadTemperatura.KELVIN, compuesto.getUnidadDestino());
  }

  @Test
  public void testConversionEncadenada() {

    ConversorCelsiusKelvin c1 = new ConversorCelsiusKelvin();
    ConversorKelvinCelsius c2 = new ConversorKelvinCelsius();

    ConversorCompuesto compuesto = new ConversorCompuesto(c1, c2);


    assertEquals("La composición inversa debe devolver el valor original", 100.0, compuesto.convertir(100.0), DELTA);
    assertEquals("0ºC convertido y desconvertido debe ser 0", 0.0, compuesto.convertir(0.0), DELTA);
  }

  @Test
  public void testConcatenarDefault() {

    ConversorCelsiusKelvin c1 = new ConversorCelsiusKelvin();
    ConversorKelvinCelsius c2 = new ConversorKelvinCelsius();


    var compuesto = c1.concatenar(c2);

    assertNotNull("El método concatenar no debe devolver null", compuesto);
    assertEquals("Debe funcionar la conversión encadenada", 25.0, compuesto.convertir(25.0), DELTA);
  }
}