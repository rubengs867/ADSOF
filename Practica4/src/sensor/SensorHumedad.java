package Practica4.src.sensor;

/**
 * Clase concreta que representa un sensor de humedad relativa.
 * <p>
 * Esta clase extiende {@link Sensor} y define un sensor específico cuya
 * magnitud de medida es el porcentaje de humedad.
 * </p>
 */
public class SensorHumedad extends Sensor {

  /** Prefijo identificador para sensores de humedad. */
  private static final String TIPO = "HUM";

  /** Valor mínimo permitido para la humedad relativa (0%). */
  private static final double MIN_VALOR = 0.0;

  /** Valor máximo permitido para la humedad relativa (100%). */
  private static final double MAX_VALOR = 100.0;

  /**
   * Constructor que crea una nueva instancia de sensor de humedad. 
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code HUM}.
   * 2. Unidad de medida en porcentaje.
   * 3. Rango operativo entre 0 y 100.
   * </p>
   *
   * @param offset Ajuste de calibración.
   */
  public SensorHumedad(double offset) {
    super(TIPO, offset, Unidad.PORCENTAJE, MIN_VALOR, MAX_VALOR);
  }

  /**
   * Representación textual de un sensor de humedad.
   */
  @Override
  public String toString() {
    return "SensorHumedad " + super.toString();
  }
  
}