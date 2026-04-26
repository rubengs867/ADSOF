package labeledDataset;

import java.util.*;

public class MisclassificationStrategy<T, L> implements FeatureSelectionStrategy<T, L> {

    @Override
    public String chooseBestFeature(List<T> datos, List<String> featuresDisponibles, LabeledDataset<T, L> dataset) {

        String mejorFeature = null;
        int menorPuntuacion = Integer.MAX_VALUE; // Empezamos con una puntuación altísima

        // for-each feature:
        for (String feature : featuresDisponibles) {

            // Group data by feature value
            Map<Object, List<T>> grupos = new HashMap<>();
            for (T dato : datos) {
                Object valor = dataset.getFeaturizer().datoDeInteres(dato, feature);
                grupos.putIfAbsent(valor, new ArrayList<>());
                grupos.get(valor).add(dato);
            }

            int puntuacionTotalFeature = 0;

            // for-each group:
            for (List<T> grupo : grupos.values()) {

                // Collect the labels per group
                Map<L, Integer> conteoEtiquetas = new HashMap<>();
                for (T dato : grupo) {
                    L etiqueta = dataset.getLabel(dato);
                    conteoEtiquetas.put(etiqueta, conteoEtiquetas.getOrDefault(etiqueta, 0) + 1);
                }

                // Compute the majority label (la que más se repite en este grupito)
                int maxApariciones = 0;
                for (int cantidad : conteoEtiquetas.values()) {
                    if (cantidad > maxApariciones) {
                        maxApariciones = cantidad;
                    }
                }

                // Count how many elements don't match it (los errores)
                // Si el grupo tiene N elementos y la etiqueta mayoritaria sale M veces,
                // ¿cuántos elementos son "errores"? ¡Exacto, N - M!
                int erroresDelGrupo = grupo.size() - maxApariciones;

                // score = sum all those mismatches
                puntuacionTotalFeature += erroresDelGrupo;
            }

            // Comprobamos si esta feature es la mejor hasta ahora
            if (puntuacionTotalFeature < menorPuntuacion) {
                menorPuntuacion = puntuacionTotalFeature;
                mejorFeature = feature;
            }
        }

        // return the feature with the lowest score
        return mejorFeature;
    }
}