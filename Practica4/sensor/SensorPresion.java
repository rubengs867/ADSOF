package Practica4.sensor;

/**
 * Clase concreta que representa un sensor de temperatura.
 * <p>
 * Esta clase extiende {@link Sensor} y define un sensor específico cuya
 * magnitud de medida son los hectopascales (hPa).
 * </p>
 */
public class SensorPresion extends Sensor {

  /** Prefijo identificador para sensores de temperatura. */
  private static final String TIPO = "PRES";

  /** Valor mínimo permitido para el sensor. */
  private static final double MIN_VALOR = 300.0;

  /** Valor máximo permitido para el sensor. */
  private static final double MAX_VALOR = 1100.0;

  /**
   * Constructor que crea una nueva instancia de sensor de presion. 
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code PRES}.
   * 2. Unidad de medida en hectopascales.
   * 3. Rango operativo entre 300 y 1100.
   * </p>
   *
   * @param offset Ajuste de calibración.
   */
  public SensorPresion(double offset) {
    super(TIPO, offset, Unidad.HPA, MIN_VALOR, MAX_VALOR);
  }

  /**
   * Reprsentación textual de un sensor de presión.
   */
  @Override
  public String toString() {
    return "Sensor Presión " + super.toString();
  }
}