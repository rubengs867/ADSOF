package Practica4.src.estrategia;

import java.util.List;

import Practica4.src.sensor.Sensor;

public class EstrategiaValorCercano implements EstrategiaLectura {

  // Fracción de variación permitida (ej. 0.10 para un ±10%)
  private double variacionPermitida;

  public EstrategiaValorCercano(double variacionPermitida) {
    this.variacionPermitida = variacionPermitida;
  }

  @Override
  public double generarValor(Sensor s) {
    List<Double> historico = s.getHistoricoLecturas();

    // Si es la primera vez que lee partimos del medio del rango
    if (s.primeraLectura()) {
      return (s.getMinRango() + s.getMaxRango()) / 2.0;
    }

    // Obtenemos el último valor del histórico
    double valorAnterior = historico.get(historico.size() - 1);

    // Calculamos cuánto puede variar hacia arriba o hacia abajo
    double delta = Math.abs(valorAnterior * variacionPermitida);

    double limiteInferior = valorAnterior - delta;
    double limiteSuperior = valorAnterior + delta;

    // Generamos el nuevo valor dentro de ese margen
    return limiteInferior + (Math.random() * (limiteSuperior - limiteInferior));
  }
}