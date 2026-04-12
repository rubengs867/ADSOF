package Practica4.src.conversor;

import Practica4.src.unidad.Unidad;
import Practica4.src.unidad.UnidadTemperatura;

public class ConversorKelvinCelsius implements Conversor {
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.KELVIN;
  }

  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.CELSIUS;
  }

  @Override
  public double convertir(double valor) {
    return valor - 273.15;
  }
}