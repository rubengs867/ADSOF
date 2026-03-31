package Practica4.src.sensor;

/**
 * Clase concreta que representa un sensor de temperatura.
 * <p>
 * Esta clase extiende {@link Sensor} y define un sensor específico cuya
 * magnitud de medida es la temperatura.
 * </p>
 */
public class SensorTemperatura extends Sensor {

  /** Prefijo identificador para sensores de temperatura. */
  private static final String TIPO = "TEMP";

  /** Valor mínimo permitido (cero absoluto en °C). */
  private static final double MIN_VALOR = -273.15;

  /** Valor máximo permitido para el sensor (1000°C). */
  private static final double MAX_VALOR = 1000.0;

  /**
   * Constructor que crea una nueva instancia de sensor de temperatura
   * con una unidad específica.
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code TEMP}.
   * 2. Unidad de medida indicada.
   * 3. Rango operativo entre -273.15 y 1000.
   * </p>
   * Además, valida que la unidad proporcionada corresponda a una magnitud
   * de temperatura.
   *
   * @param offset Ajuste de calibración.
   * @param unidad Unidad de medida (debe ser una unidad de temperatura).
   *
   * @throws IllegalArgumentException si la unidad no es de tipo temperatura.
   */
  public SensorTemperatura(double offset, Unidad unidad) {
    super(TIPO, offset, unidad, MIN_VALOR, MAX_VALOR);

    if (!unidad.isTemperatura()) {
      throw new IllegalArgumentException(
          "La unidad " + unidad + " no es válida para temperatura.");
    }
  }

  /**
   * Constructor que crea un sensor de temperatura utilizando Celsius
   * como unidad por defecto.
   *
   * @param offset Ajuste de calibración.
   */
  public SensorTemperatura(double offset) {
    this(offset, Unidad.CELSIUS);
  }

  /**
   * Reprsentación textual de un sensor de temperatura.
   */
  @Override
  public String toString() {
    return "SensorTemperatura " + super.toString();
  }
}