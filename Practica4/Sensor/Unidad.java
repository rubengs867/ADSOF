package Practica4.Sensor;

public enum Unidad {
  CELSIUS,
  FAHRENHEIT,
  KELVIN,
  PORCENTAJE,
  HPA;

  public boolean isTemperatura() {
    return this == CELSIUS || this == FAHRENHEIT || this == KELVIN;
  }

  public boolean isHumedad() {
    return this == PORCENTAJE;
  }

  public boolean isPresion() {
    return this == HPA;
  }
}