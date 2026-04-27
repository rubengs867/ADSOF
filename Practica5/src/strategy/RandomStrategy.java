package strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import model.LabeledDataset;

/**
 * Estrategia de selección de características que elige una característica de
 * forma aleatoria.
 * Esta clase sirve como una heurística base (baseline) para el algoritmo de
 * aprendizaje
 * del árbol de decisión. Al no realizar ningún cálculo matemático para
 * optimizar la división,
 * suele generar árboles menos óptimos y más profundos que otras estrategias
 * avanzadas,
 * pero es extremadamente rápida en su ejecución.
 *
 * @param <T> El tipo de objeto que se está clasificando (ej. Person, Weather).
 * @param <L> El tipo de la etiqueta asignada a los objetos (ej. String,
 *            Boolean).
 */
public class RandomStrategy<T, L> implements FeatureSelectionStrategy<T, L> {

  /**
   * Selecciona una característica al azar de la lista de características
   * disponibles.
   *
   * @param dataset             El conjunto de datos etiquetados original (no se
   *                            utiliza en esta estrategia).
   * @return El nombre de la característica seleccionada aleatoriamente.
   */
  @Override
  public String chooseBestFeature(LabeledDataset<T, L> dataset) {
    Set<String> featuresDisponibles = dataset.getFeatures().keySet();
    
    // Hacemos una copia para no alterar la lista original al mezclar.
    // Esto previene "efectos colaterales" indeseados en la lista del llamador.
    List<String> mezclada = new ArrayList<>(featuresDisponibles);

    // Barajamos aleatoriamente la copia de la lista
    Collections.shuffle(mezclada);

    // Devolvemos la primera característica tras barajar
    return mezclada.get(0);
  }
}