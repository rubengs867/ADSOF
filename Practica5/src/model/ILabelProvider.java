package model;

/**
 * Define el contrato para asignar una etiqueta a un objeto.
 *
 * @param <T> Tipo de objeto evaluado.
 * @param <L> Tipo de etiqueta generada.
 */
public interface ILabelProvider<T, L> {

  /**
   * Devuelve la etiqueta asociada al objeto indicado.
   *
   * @param object Objeto a etiquetar.
   *
   * @return Etiqueta correspondiente.
   */
  L getLabel(T object);
}