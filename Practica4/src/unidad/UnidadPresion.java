package unidad;

/**
 * Enumerado que representa las distintas unidades de medida
 * utilizadas específicamente para los sensores de presión atmosférica.
 * <p>
 * Implementa la interfaz {@link Unidad} para garantizar que
 * proporcione una representación textual estandarizada.
 * </p>
 */
public enum UnidadPresion implements Unidad {

  /** Hectopascales (unidad estándar para presión atmosférica). */
  HPA(" hPa");

  /** Representación textual de la unidad. */
  private String texto;

  /**
   * Constructor privado del enumerado.
   *
   * @param texto Representación en texto de la unidad (ej. " hPa").
   */
  private UnidadPresion(String texto) {
    this.texto = texto;
  }

  /**
   * Devuelve la representación textual de la unidad de presión.
   * * @return cadena de caracteres con el símbolo de la unidad.
   */
  @Override
  public String getTexto() {
    return texto;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code true} dado que es una unidad de presión.
   */
  @Override
  public boolean isPresion() {
    return true;
  }
}