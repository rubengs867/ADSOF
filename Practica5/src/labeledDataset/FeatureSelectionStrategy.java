package labeledDataset;

import java.util.List;

public interface FeatureSelectionStrategy<T, L> {
    /**
     * @param datos Los datos actuales en el nodo.
     * @param featuresDisponibles Las características que aún no se han usado.
     * @param dataset El dataset original para acceder al Featurizer y al LabelProvider.
     * @return El nombre de la mejor característica elegida.
     */
    String chooseBestFeature(List<T> datos, List<String> featuresDisponibles, LabeledDataset<T, L> dataset);
}