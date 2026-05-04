package strategy;

import model.LabeledDataset;

/**
 * Define la estrategia de selección de características para algoritmos
 * de aprendizaje basados en árboles de decisión.
 *
 * @param <T> Tipo de dato contenido en el dataset.
 * @param <L> Tipo de etiqueta asociada.
 */
public interface FeatureSelectionStrategy<T, L> {

  /**
   * Selecciona la mejor característica disponible para dividir el dataset.
   *
   * @param dataset Dataset etiquetado de entrada.
   *
   * @return Nombre de la característica seleccionada.
   */
  String chooseBestFeature(LabeledDataset<T, L> dataset);
}