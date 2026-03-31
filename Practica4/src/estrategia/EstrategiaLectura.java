package Practica4.src.estrategia;

import Practica4.src.sensor.Sensor;

/**
 * Interfaz que define el método para las estrategias de lectura.
 */
public interface EstrategiaLectura {
  /**
   * Genera un valor simulado para un sensor específico.
   * 
   * @param s El sensor para el cual se genera la lectura.
   * @return El valor simulado generado.
   */
  double generarValor(Sensor s);
}
