package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa una colección de valores homogéneos asociados a una característica
 * de un conjunto de datos.
 *
 * @param <T> Tipo de dato de la característica. Debe ser comparable.
 */
public class Feature<T extends Comparable<T>> extends ArrayList<T> {

  /**
   * Devuelve el valor mínimo contenido en la colección.
   *
   * @return Valor mínimo o {@code null} si la colección está vacía.
   */
  public T min() {
    if (this.isEmpty()) {
      return null;
    }

    T min = this.get(0);

    for (T element : this) {
      if (element.compareTo(min) < 0) {
        min = element;
      }
    }

    return min;
  }

  /**
   * Devuelve el valor máximo contenido en la colección.
   *
   * @return Valor máximo o {@code null} si la colección está vacía.
   */
  public T max() {
    if (this.isEmpty()) {
      return null;
    }

    T max = this.get(0);

    for (T element : this) {
      if (element.compareTo(max) > 0) {
        max = element;
      }
    }

    return max;
  }

  /**
   * Calcula la distribución de frecuencias de los elementos almacenados.
   *
   * @return Mapa con cada valor y su número de apariciones,
   *         o {@code null} si la colección está vacía.
   */
  public Map<T, Integer> distribution() {
    if (this.isEmpty()) {
      return null;
    }

    Map<T, Integer> frecuencia = new HashMap<>();

    for (T element : this) {
      frecuencia.compute(element, (k, v) -> (v == null) ? 1 : v + 1);
    }

    return frecuencia;
  }
}