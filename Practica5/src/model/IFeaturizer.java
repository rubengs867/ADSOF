package model;

import java.util.List;

public interface IFeaturizer<T> {

  /** Devuelve la lista de nombres de las características a extraer */
  List<String> featureDeInteres();

  /**
   * Devuelve el valor de la característica solicitada para el objeto dado.
   * Retorna Comparable<?> porque la clase Feature exige valores comparables.
   */
  Comparable<?> datoDeInteres(T object, String featureName);
}