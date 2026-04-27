package strategy;

import model.LabeledDataset;

public interface FeatureSelectionStrategy<T, L> {
  /**
   * @param dataset             El dataset original para acceder al Featurizer y
   *                            al LabelProvider.
   * @return El nombre de la mejor característica elegida.
   */
  String chooseBestFeature(LabeledDataset<T, L> dataset);
}