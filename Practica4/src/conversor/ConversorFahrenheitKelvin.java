package Practica4.src.conversor;

import Practica4.src.unidad.Unidad;
import Practica4.src.unidad.UnidadTemperatura;

public class ConversorFahrenheitKelvin implements Conversor {
  @Override
  public Unidad getUnidadOrigen() {
    return UnidadTemperatura.FAHRENHEIT;
  }

  @Override
  public Unidad getUnidadDestino() {
    return UnidadTemperatura.KELVIN;
  }

  @Override
  public double convertir(double valor) {
    return (valor - 32) * 5 / 9 + 273.15;
  }
}