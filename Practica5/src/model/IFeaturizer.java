package model;

import java.util.List;

/**
 * Define el contrato para extraer características relevantes de un objeto.
 *
 * @param <T> Tipo de objeto procesado.
 */
public interface IFeaturizer<T> {

  /**
   * Devuelve los nombres de las características disponibles.
   *
   * @return Lista de nombres de características.
   */
  List<String> featureDeInteres();

  /**
   * Obtiene el valor de una característica concreta para un objeto dado.
   *
   * @param object      Objeto origen.
   * @param featureName Nombre de la característica.
   *
   * @return Valor de la característica como dato comparable.
   */
  Comparable<?> datoDeInteres(T object, String featureName);
}