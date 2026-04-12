package Practica4.src.sensor;

import Practica4.src.estrategia.EstrategiaAleatoria;
import Practica4.src.estrategia.EstrategiaLectura;
import Practica4.src.unidad.Unidad;
import Practica4.src.unidad.UnidadHumedad;

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
   * 3. Estrategia aleatoria por defecto con una probabilidad del 5%.
   * </p>
   *
   * @param offset Ajuste de calibración.
   */
  public SensorHumedad(double offset) {
    this(offset, new EstrategiaAleatoria(0.05));
  }

  /**
   * Constructor que crea una nueva instancia de sensor de humedad, especificando
   * la estragia de lectura.
   * <p>
   * Inicializa el sensor con:
   * <p>
   * 1. Prefijo de tipo {@code HUM}.
   * 2. Unidad de medida en porcentaje.
   * </p>
   *
   * @param offset Ajuste de calibración.
   */
  public SensorHumedad(double offset, EstrategiaLectura estrategia) {
    super(TIPO, offset, UnidadHumedad.PORCENTAJE, MIN_VALOR, MAX_VALOR, estrategia);
  }

  /**
   * Modifica la unidad de medida.
   * Solo puede sor modificada por otra unidad que mida la humedad, en caso
   * contrario se lanzará una excepción {@link IllegalArgumentException}
   * 
   * @param u Unidad de medida a establecer
   */
  @Override
  public void setUnidad(Unidad u) {
    if (!u.isHumedad()) {
      throw new IllegalArgumentException(
          "La unidad " + u + " no es válida para temperatura.");
    }
    this.unidad = u;
  }

  /**
   * Representación textual de un sensor de humedad.
   */
  @Override
  public String toString() {
    return String.format("Sensor Humedad (%.2f%s) última lectura: %s",
        getValorUltimaLectura(),
        getUnidad().getTexto(),
        getFechaUltimaLectura());
  }

}