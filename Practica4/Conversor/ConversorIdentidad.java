package Practica4.Conversor;

import Practica4.sensor.Unidad;

public class ConversorIdentidad implements Conversor {

  // Guardamos la unidad para saber en qué estamos midiendo, aunque no la
  // cambiemos
  private Unidad unidad;

  /**
   * Constructor que recibe la unidad original del sensor.
   * 
   * @param unidad La unidad en la que está midiendo el sensor por defecto.
   */
  public ConversorIdentidad(Unidad unidad) {
    this.unidad = unidad;
  }

  @Override
  public Unidad getUnidadOrigen() {
    return this.unidad;
  }

  @Override
  public Unidad getUnidadDestino() {
    return this.unidad;
  }

  @Override
  public double convertir(double valor) {
    return valor;
  }
}