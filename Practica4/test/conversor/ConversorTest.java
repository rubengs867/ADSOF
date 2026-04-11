package Practica4.test.conversor;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import Practica4.src.conversor.Conversor;
import Practica4.src.conversor.ConversorIdentidad;
import Practica4.src.excepcion.ConversionErroneaException;
import Practica4.src.unidad.UnidadTemperatura;

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