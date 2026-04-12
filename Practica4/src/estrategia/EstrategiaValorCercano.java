package Practica4.src.estrategia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Practica4.src.sensor.Sensor;

public class EstrategiaValorCercano implements EstrategiaLectura {

  /** Fracción de variación permitida  */
  private double variacionPermitida;

  public EstrategiaValorCercano(double variacionPermitida) {
    this.variacionPermitida = variacionPermitida;
  }

  @Override
  public double generarValor(Sensor s) {

    if (s.primeraLectura()) {
      return (s.getMinRango() + s.getMaxRango()) / 2.0;
    }


    double valorAnterior = s.getValorUltimaLectura();


    double delta = Math.abs(valorAnterior * variacionPermitida);

    double limiteInferior = valorAnterior - delta;
    double limiteSuperior = valorAnterior + delta;

    return limiteInferior + (Math.random() * (limiteSuperior - limiteInferior));
  }
}