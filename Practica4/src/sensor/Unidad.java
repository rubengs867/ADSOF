package Practica4.src.sensor;

/**
 * Enumerado que representa las distintas unidades de medida
 * soportadas por los sensores.
 * <p>
 * Cada unidad incluye una representación textual que puede ser
 * utilizada para mostrar valores de forma legible.
 * </p>
 */
public enum Unidad {

  /** Grados Celsius. */
  CELSIUS("ºC"),

  /** Grados Fahrenheit. */
  FAHRENHEIT("ºF"),

  /** Kelvin (unidad del Sistema Internacional). */
  KELVIN("K"),

  /** Porcentaje (usado para humedad relativa). */
  PORCENTAJE("%"),

  /** Hectopascales (unidad de presión atmosférica). */
  HPA("hPa");

  /** Representación textual de la unidad. */
  private String texto;

  /**
   * Constructor del enumerado.
   *
   * @param texto Representación en texto de la unidad.
   */
  private Unidad(String texto) {
    this.texto = texto;
  }

  /**
   * Indica si la unidad corresponde a temperatura.
   *
   * @return {@code true} si es CELSIUS, FAHRENHEIT o KELVIN; {@code false} en
   *         caso contrario.
   */
  public boolean isTemperatura() {
    return this == CELSIUS || this == FAHRENHEIT || this == KELVIN;
  }

  /**
   * Indica si la unidad corresponde a humedad.
   *
   * @return {@code true} si es PORCENTAJE; {@code false} en caso contrario.
   */
  public boolean isHumedad() {
    return this == PORCENTAJE;
  }

  /**
   * Indica si la unidad corresponde a presión.
   *
   * @return {@code true} si es HPA; {@code false} en caso contrario.
   */
  public boolean isPresion() {
    return this == HPA;
  }

  /**
   * Devuelve la representación textual de la unidad.
   * @return cadena de caracteres
   */
  public String getTexto() {
    return texto;
  }
}