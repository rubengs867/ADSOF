package conversor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import unidad.UnidadTemperatura;

public class ConversorKelvinFahrenheitTest {

    private static final double DELTA = 0.001;

    @Test
    public void testUnidades() {
        ConversorKelvinFahrenheit c = new ConversorKelvinFahrenheit();
        assertEquals("El origen debe ser KELVIN", UnidadTemperatura.KELVIN, c.getUnidadOrigen());
        assertEquals("El destino debe ser FAHRENHEIT", UnidadTemperatura.FAHRENHEIT, c.getUnidadDestino());
    }

    @Test
    public void testConvertirMatematicas() {
        //el delta es el error permtido
        ConversorKelvinFahrenheit c = new ConversorKelvinFahrenheit();
        assertEquals("273.15K deben ser 32ºF", 32.0, c.convertir(273.15), DELTA);
        assertEquals("373.15K deben ser 212ºF", 212.0, c.convertir(373.15), DELTA);
        assertEquals("0K deben ser -459.67ºF", -459.67, c.convertir(0), DELTA);
    }
}