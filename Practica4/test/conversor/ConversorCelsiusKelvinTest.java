package test.conversor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import src.conversor.ConversorCelsiusKelvin;
import src.unidad.UnidadTemperatura;

public class ConversorCelsiusKelvinTest {

    private static final double DELTA = 0.001;

    @Test
    public void testUnidades() {
        ConversorCelsiusKelvin c = new ConversorCelsiusKelvin();
        assertEquals("El origen debe ser CELSIUS", UnidadTemperatura.CELSIUS, c.getUnidadOrigen());
        assertEquals("El destino debe ser KELVIN", UnidadTemperatura.KELVIN, c.getUnidadDestino());
    }

    @Test
    public void testConvertirMatematicas() {
        ConversorCelsiusKelvin c = new ConversorCelsiusKelvin();
        assertEquals("0ºC deben ser 273.15K", 273.15, c.convertir(0), DELTA);
        assertEquals("100ºC deben ser 373.15K", 373.15, c.convertir(100), DELTA);
        assertEquals("-273.15ºC (Cero absoluto) deben ser 0K", 0.0, c.convertir(-273.15), DELTA);
    }
}