package test.conversor;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import src.conversor.Conversor;
import src.conversor.ConversorIdentidad;
import src.excepcion.ConversionErroneaException;
import src.unidad.UnidadTemperatura;

public class ConversorTest {

  @Test
  public void testConcatenarCompatible() {
    // Preparamos dos conversores compatibles
    Conversor c1 = new ConversorIdentidad(UnidadTemperatura.CELSIUS);
    Conversor c2 = new ConversorIdentidad(UnidadTemperatura.CELSIUS);

    // Ejecutamos el método default de la interfaz
    Conversor compuesto = c1.concatenar(c2);

    assertNotNull("El conversor compuesto no debería ser null", compuesto);
  }

  @Test(expected = ConversionErroneaException.class)
  public void testConcatenarIncompatibleLanzaExcepcion() throws ConversionErroneaException {
    // Preparamos dos conversores INCOMPATIBLES
    Conversor cCelsius = new ConversorIdentidad(UnidadTemperatura.CELSIUS);
    Conversor cKelvin = new ConversorIdentidad(UnidadTemperatura.KELVIN);

    cCelsius.concatenar(cKelvin);
  }
}