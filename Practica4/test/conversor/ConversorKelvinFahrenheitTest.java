package Practica4.test.conversor;

import org.junit.Test;
import static org.junit.Assert.*;

import Practica4.src.conversor.ConversorKelvinFahrenheit;
import Practica4.src.sensor.Unidad;

public class ConversorKelvinFahrenheitTest {

    private static final double DELTA = 0.001;

    @Test
    public void testUnidades() {
        ConversorKelvinFahrenheit c = new ConversorKelvinFahrenheit();
        assertEquals("El origen debe ser KELVIN", Unidad.KELVIN, c.getUnidadOrigen());
        assertEquals("El destino debe ser FAHRENHEIT", Unidad.FAHRENHEIT, c.getUnidadDestino());
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