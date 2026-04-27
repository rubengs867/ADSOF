package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.LabeledDataset;

/**
 * Estrategia de selección de características basada en la métrica de
 * clasificación errónea (Misclassification).
 * Esta heurística evalúa cada característica disponible agrupando los datos
 * según los distintos
 * valores que toma dicha característica. Para cada grupo, asume que la
 * predicción será la etiqueta
 * mayoritaria y cuenta cuántos elementos no coinciden con esa etiqueta
 * (elementos mal clasificados).
 * La característica óptima será aquella cuya suma total de errores en todos sus
 * grupos sea la menor.
 *
 * @param <T> El tipo de objeto que se está clasificando (ej. Person, Weather).
 * @param <L> El tipo de la etiqueta asignada a los objetos (ej. String,
 *            Boolean).
 */
public class MisclassificationStrategy<T, L> implements FeatureSelectionStrategy<T, L> {

  /**
   * Evalúa las características disponibles y elige la mejor para realizar la
   * siguiente
   * división en el árbol de decisión, buscando minimizar los errores de
   * clasificación.
   *
   * @param dataset             El conjunto de datos etiquetados, necesario para
   *                            extraer los valores de las características
   *                            (Featurizer) y sus etiquetas (LabelProvider).
   * @return El nombre de la característica seleccionada que produce el menor
   *         número de clasificaciones erróneas.
   */
  @Override
  public String chooseBestFeature(LabeledDataset<T, L> dataset) {
    Set<String> featuresDisponibles = dataset.getFeatures().keySet();
    LinkedHashSet<T> datos = dataset.getData();

    String mejorFeature = null;
    int menorPuntuacion = Integer.MAX_VALUE; // Empezamos con una puntuación altísima

    for (String feature : featuresDisponibles) {

      // Agrupar datos según el valor de la característica
      Map<Object, List<T>> grupos = new HashMap<>();
      for (T dato : datos) {
        Object valor = dataset.getFeaturizer().datoDeInteres(dato, feature);
        grupos.putIfAbsent(valor, new ArrayList<>());
        grupos.get(valor).add(dato);
      }

      int puntuacionTotalFeature = 0;

      // Analizar cada subgrupo generado
      for (List<T> grupo : grupos.values()) {

        // Recolectar y contar las etiquetas del grupo
        Map<L, Integer> conteoEtiquetas = new HashMap<>();
        for (T dato : grupo) {
          L etiqueta = dataset.getLabel(dato);
          conteoEtiquetas.put(etiqueta, conteoEtiquetas.getOrDefault(etiqueta, 0) + 1);
        }

        // Encontrar la cantidad de veces que aparece la etiqueta mayoritaria
        int maxApariciones = 0;
        for (int cantidad : conteoEtiquetas.values()) {
          if (cantidad > maxApariciones) {
            maxApariciones = cantidad;
          }
        }

        // Si el grupo tiene N elementos y la etiqueta mayoritaria sale M veces,
        int erroresDelGrupo = grupo.size() - maxApariciones;

        // Acumular los errores de este grupo a la característica
        puntuacionTotalFeature += erroresDelGrupo;
      }

      // Comprobamos si esta feature es la mejor hasta ahora, la que tiene menos
      // errores
      if (puntuacionTotalFeature < menorPuntuacion) {
        menorPuntuacion = puntuacionTotalFeature;
        mejorFeature = feature;
      }
    }

    // Devolver la característica ganadora
    return mejorFeature;
  }
}