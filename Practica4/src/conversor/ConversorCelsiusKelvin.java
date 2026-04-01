package Practica4.src.conversor;

import Practica4.src.sensor.Unidad;

public class ConversorCelsiusKelvin implements Conversor {
  @Override
  public Unidad getUnidadOrigen() {
    return Unidad.CELSIUS;
  }

  @Override
  public Unidad getUnidadDestino() {
    return Unidad.KELVIN;
  }

  @Override
  public double convertir(double valor) {
    return valor + 273.15;
  }
}