package estrategia;

import sensor.Sensor;

/**
 * Interfaz que define el método base para las estrategias de simulación de
 * lecturas.
 * <p>
 * Esta interfaz forma parte del patrón Strategy, permitiendo que cada sensor
 * pueda tener un comportamiento distinto a la hora de generar o capturar datos.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
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
