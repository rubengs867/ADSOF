package unidad;

/**
 * Enumerado que representa las distintas unidades de medida
 * utilizadas específicamente para los sensores de temperatura.
 * <p>
 * Implementa la interfaz {@link Unidad} para garantizar que
 * proporcione una representación textual estandarizada.
 * </p>
 * @author Alejandro Seguido
 * @author Rubén García
 */
public enum UnidadTemperatura implements Unidad {

  /** Grados Celsius (Sistema métrico). */
  CELSIUS("ºC"),

  /** Grados Fahrenheit (Sistema imperial). */
  FAHRENHEIT("ºF"),

  /** Kelvin (Sistema Internacional de Unidades). */
  KELVIN(" K");

  /** Representación textual de la unidad. */
  private String texto;

  /**
   * Constructor privado del enumerado.
   *
   * @param texto Representación en texto de la unidad (ej. "ºC").
   */
  private UnidadTemperatura(String texto) {
    this.texto = texto;
  }

  /**
   * Devuelve la representación textual de la unidad de temperatura.
   * * @return cadena de caracteres con el símbolo de la unidad.
   */
  @Override
  public String getTexto() {
    return texto;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code true} dado que es una unidad de temperatura.
   */
  @Override
  public boolean isTemperatura() {
    return true;
  }
}