package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

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