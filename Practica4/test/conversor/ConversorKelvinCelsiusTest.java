package Practica4.test.conversor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Practica4.src.conversor.ConversorKelvinCelsius;
import Practica4.src.unidad.UnidadTemperatura;

public class ConversorKelvinCelsiusTest {

  private static final double DELTA = 0.001;

  @Test
  public void testUnidades() {
    ConversorKelvinCelsius c = new ConversorKelvinCelsius();
    assertEquals("El origen debe ser KELVIN", UnidadTemperatura.KELVIN, c.getUnidadOrigen());
    assertEquals("El destino debe ser CELSIUS", UnidadTemperatura.CELSIUS, c.getUnidadDestino());
  }

  @Test
  public void testConvertirMatematicas() {
    ConversorKelvinCelsius c = new ConversorKelvinCelsius();
    assertEquals("273.15K deben ser 0ºC", 0.0, c.convertir(273.15), DELTA);
    assertEquals("373.15K deben ser 100ºC", 100.0, c.convertir(373.15), DELTA);
    assertEquals("0K deben ser -273.15ºC (Cero absoluto)", -273.15, c.convertir(0.0), DELTA);
  }
}