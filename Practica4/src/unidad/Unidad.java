package Practica4.src.unidad;

/**
 * Interfaz que define la base para cualquier unidad de medida
 * soportada por los sensores del sistema.
 */
public interface Unidad {

  /**
   * Devuelve la representación textual de la unidad de medida.
   * <p>
   * Esta cadena está pensada para ser concatenada a los valores numéricos
   * y mostrar la información de forma legible al usuario final
   * (por ejemplo: "ºC", " hPa", " K", "%").
   * </p>
   *
   * @return Cadena de caracteres que representa visualmente el símbolo de la
   *         unidad.
   */
  String getTexto();

  /**
   * Indica si esta unidad pertenece a la magnitud de Temperatura.
   * 
   * @return {@code true} si es una unidad de temperatura;
   *         {@code false} por defecto para cualquier otra magnitud.
   */
  default boolean isTemperatura() {
    return false;
  }

  /**
   * Indica si esta unidad pertenece a la magnitud de Presión.
   * 
   * @return {@code true} si es una unidad de presión;
   *         {@code false} por defecto para cualquier otra magnitud.
   */
  default boolean isPresion() {
    return false;
  }

  /**
   * Indica si esta unidad pertenece a la magnitud de Humedad.
   * 
   * @return {@code true} si es una unidad de humedad;
   *         {@code false} por defecto para cualquier otra magnitud.
   */
  default boolean isHumedad() {
    return false;
  }
}