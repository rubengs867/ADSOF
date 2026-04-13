package sensor;

import estrategia.EstrategiaLectura;
import unidad.Unidad;
import unidad.UnidadPresion;
import estrategia.EstrategiaAleatoria;

/**
 * Clase concreta que representa un sensor de temperatura.
 * <p>
 * Esta clase extiende {@link Sensor} y define un sensor específico cuya
 * magnitud de medida son los hectopascales (hPa).
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class SensorPresion extends Sensor {

  /** Prefijo identificador para sensores de temperatura. */
  private static final String TIPO = "PRES";

  /** Valor mínimo permitido para el sensor. */
  private static final double MIN_VALOR = 300.0;

  /** Valor máximo permitido para el sensor. */
  private static final double MAX_VALOR = 1100.0;

  /**
   * Constructor que crea una nueva instancia de sensor de presion especificando
   * el tipo de estrategia a seguir para la lectura.
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code PRES}.
   * 2. Unidad de medida en hectopascales.
   * </p>
   *
   * @param offset     Ajuste de calibración.
   * @param unidad     Unidad de medida (debe ser una unidad de presión).
   * @param estrategia Estrategia de lectura a utilizar.
   */
  public SensorPresion(double offset, Unidad unidad, EstrategiaLectura estrategia) {
    super(TIPO, offset, UnidadPresion.HPA, MIN_VALOR, MAX_VALOR, estrategia);
  }

  /**
   * Constructor que crea una nueva instancia de sensor de presion.
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code PRES}.
   * 2. Unidad de medida en hectopascales.
   * 3. Estrategia aleatoria por defecto con una probabilidad del 5%.
   * </p>
   *
   * @param offset Ajuste de calibración.
   */
  public SensorPresion(double offset) {
    this(offset, UnidadPresion.HPA, new EstrategiaAleatoria(0.05));
  }

  /**
   * Modifica la unidad de medida.
   * Solo puede sor modificada por otra unidad que mida la presión, en caso
   * contrario se lanzará una excepción {@link IllegalArgumentException}
   * 
   * @param u Unidad de medida a establecer
   */
  @Override
  public void setUnidad(Unidad u) {
    if (!u.isPresion()) {
      throw new IllegalArgumentException(
          "La unidad " + u + " no es válida para presión.");
    }
    this.unidad = u;
  }

  /**
   * Reprsentación textual de un sensor de presión.
   */
  @Override
  public String toString() {
    return String.format("Sensor Presión (%.2f %s) última lectura: %s",
        getValorUltimaLectura(),
        getUnidad().getTexto(),
        getFechaUltimaLectura());
  }
}