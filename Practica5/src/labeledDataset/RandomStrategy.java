package labeledDataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomStrategy<T, L> implements FeatureSelectionStrategy<T, L> {

    @Override
    public String chooseBestFeature(List<T> datos, List<String> featuresDisponibles, LabeledDataset<T, L> dataset) {
        // Hacemos una copia para no alterar la lista original al mezclar
        List<String> mezclada = new ArrayList<>(featuresDisponibles);
        Collections.shuffle(mezclada);
        return mezclada.get(0); // Devolvemos la primera característica tras barajar
    }
}