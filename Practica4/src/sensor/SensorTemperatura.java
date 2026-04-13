package sensor;

import estrategia.EstrategiaAleatoria;
import estrategia.EstrategiaLectura;
import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Clase concreta que representa un sensor de temperatura.
 * <p>
 * Esta clase extiende {@link Sensor} y define un sensor específico cuya
 * magnitud de medida es la temperatura.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
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
   * con una unidad específica y una estrategia específica para la lectura.
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code TEMP}.
   * 2. Unidad de medida indicada.
   * </p>
   * Además, valida que la unidad proporcionada corresponda a una magnitud
   * de temperatura.
   *
   * @param offset     Ajuste de calibración.
   * @param unidad     Unidad de medida (debe ser una unidad de temperatura).
   * @param estrategia Estrategia de lectura a utilizar.
   *
   * @throws IllegalArgumentException si la unidad no es de tipo temperatura.
   */
  public SensorTemperatura(double offset, Unidad unidad, EstrategiaLectura estrategia) {
    super(TIPO, offset, unidad, MIN_VALOR, MAX_VALOR, estrategia);

    if (!unidad.isTemperatura()) {
      throw new IllegalArgumentException(
          "La unidad " + unidad + " no es válida para temperatura.");
    }
  }

  /**
   * Constructor que crea una nueva instancia de sensor de temperatura
   * con una unidad específica.
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code TEMP}.
   * 2. Unidad de medida indicada.
   * 3. Estrategia aleatoria por defecto con una probabilidad del 5%.
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
    this(offset, unidad, new EstrategiaAleatoria(0.05));
  }

  /**
   * Constructor que crea un sensor de temperatura utilizando Celsius
   * como unidad por defecto.
   *
   * @param offset Ajuste de calibración.
   */
  public SensorTemperatura(double offset) {
    this(offset, UnidadTemperatura.CELSIUS);
  }

  /**
   * Modifica la unidad de medida.
   * Solo puede sor modificada por otra unidad que mida la temperatura, en caso
   * contrario se lanzará una excepción {@link IllegalArgumentException}
   * 
   * @param u Unidad de medida a establecer
   */
  @Override
  public void setUnidad(Unidad u) {
    if (!u.isTemperatura()) {
      throw new IllegalArgumentException(
          "La unidad " + u + " no es válida para temperatura.");
    }
    this.unidad = u;
  }

  /**
   * Reprsentación textual de un sensor de temperatura.
   */
  @Override
  public String toString() {
    return String.format("Sensor Temperatura (%.2f%s) última lectura: %s",
        getValorUltimaLectura(),
        getUnidad().getTexto(),
        getFechaUltimaLectura());
  }
}