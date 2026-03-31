package Practica4.src.conversor;

import Practica4.src.sensor.Unidad;

public class ConversorCompuesto implements Conversor {

  /**Guardamos el primer conversor */
  private Conversor primero;
  private Conversor segundo;

  public ConversorCompuesto(Conversor primero, Conversor segundo) {
    this.primero = primero;
    this.segundo = segundo;
  }

  @Override
  public Unidad getUnidadOrigen() {
    return primero.getUnidadOrigen();
  }

  @Override
  public Unidad getUnidadDestino() {
    return segundo.getUnidadDestino();
  }

  @Override
  public double convertir(double valor) {
    //Convertimos el valor del primer conversor
    double valorIntermedio = primero.convertir(valor);

    //repetimos con lo mismo pero con el valor intermedio
    return segundo.convertir(valorIntermedio);
  }
}