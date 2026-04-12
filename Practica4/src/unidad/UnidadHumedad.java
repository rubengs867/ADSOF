package src.unidad;

/**
 * Enumerado que representa las distintas unidades de medida
 * utilizadas específicamente para los sensores de humedad.
 * <p>
 * Implementa la interfaz {@link Unidad} para garantizar que
 * proporcione una representación textual estandarizada.
 * </p>
 */
public enum UnidadHumedad implements Unidad {

  /** Porcentaje (usado para medir la humedad relativa). */
  PORCENTAJE("%");

  /** Representación textual de la unidad. */
  private String texto;

  /**
   * Constructor privado del enumerado.
   *
   * @param texto Representación en texto de la unidad (ej. "%").
   */
  private UnidadHumedad(String texto) {
    this.texto = texto;
  }

  /**
   * Devuelve la representación textual de la unidad de humedad.
   * * @return cadena de caracteres con el símbolo de la unidad.
   */
  @Override
  public String getTexto() {
    return texto;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code true} dado que es una unidad de humedad.
   */
  @Override
  public boolean isHumedad() {
    return true;
  }
}