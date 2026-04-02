package Practica4.test.conversor;

import org.junit.Test;
import static org.junit.Assert.*;

import Practica4.src.conversor.*;
import Practica4.src.excepcion.ConversionErroneaException;
import Practica4.src.sensor.Unidad;

public class ConversorTest {

  @Test
  public void testConcatenarCompatible() {
    // Preparamos dos conversores compatibles
    Conversor c1 = new ConversorIdentidad(Unidad.CELSIUS);
    Conversor c2 = new ConversorIdentidad(Unidad.CELSIUS);

    // Ejecutamos el método default de la interfaz
    Conversor compuesto = c1.concatenar(c2);

    assertNotNull("El conversor compuesto no debería ser null", compuesto);
  }

  @Test(expected = ConversionErroneaException.class)
  public void testConcatenarIncompatibleLanzaExcepcion() throws ConversionErroneaException {
    // Preparamos dos conversores INCOMPATIBLES
    Conversor cCelsius = new ConversorIdentidad(Unidad.CELSIUS);
    Conversor cKelvin = new ConversorIdentidad(Unidad.KELVIN);

    cCelsius.concatenar(cKelvin);
  }
}