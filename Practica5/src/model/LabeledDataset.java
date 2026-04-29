package model;

import java.util.List;

/**
 * Representa un conjunto de datos en el que cada objeto tiene una etiqueta
 * asociada.
 * combina los datos extraídos por el featurizer con las respuestas correctas
 * proporcionadas por el etiquetador.
 *
 * @param <T> El tipo de objeto que almacena el dataset (por ejemplo, Person,
 *            Weather).
 * @param <L> El tipo de la etiqueta asignada a cada objeto (por ejemplo,
 *            String, Boolean).
 */
public class LabeledDataset<T, L> extends Dataset<T> {

  /**
   * El proveedor encargado de asignar o recuperar la etiqueta de cada objeto.
   */
  private LabelProvider<T, L> labelProvider;

  /**
   * Construye un nuevo LabeledDataset, inicializando tanto el extractor de
   * características
   * como el asignador de etiquetas.
   * 
   * @param featurizer    Herramienta para extraer las características de los
   *                      objetos de tipo T.
   * @param labelProvider Herramienta para obtener la etiqueta de tipo L
   *                      correspondiente a cada objeto.
   */
  public LabeledDataset(IFeaturizer<T> featurizer, LabelProvider<T, L> labelProvider) {
    super(featurizer);
    this.labelProvider = labelProvider;
  }

  /**
   * Obtiene la etiqueta asociada a un objeto específico utilizando el
   * LabelProvider.
   * 
   * @param object El objeto del cual se quiere conocer la etiqueta.
   * @return La etiqueta correspondiente al objeto, de tipo L.
   */
  public L getLabel(T object) {
    return labelProvider.getLabel(object);
  }

  /**
   * Devuelve el proveedor de etiquetas asociado a este dataset.
   * Útil para algoritmos de clasificación (como GreedyTreeLearner) que necesiten
   * consultar el etiquetador original.
   * 
   * @return El objeto LabelProvider utilizado por este dataset.
   */
  public LabelProvider<T, L> getLabelProvider() {
    return this.labelProvider;
  }

  public LabeledDataset<T, L> subset(List<T> datos, List<String> featuresSeleccionadas) {

    // Crear nuevo dataset con mismo featurizer y labelProvider
    LabeledDataset<T, L> sub = new LabeledDataset<>(this.getFeaturizer(), this.labelProvider);

    // Filtrar features: eliminar las que no están disponibles
    sub.getFeatures().keySet().retainAll(featuresSeleccionadas);

    // Añadir datos filtrados
    for (T obj : datos) {

      // Añadir valores de features seleccionadas
      for (String featureName : featuresSeleccionadas) {
        Comparable value = getFeaturizer().datoDeInteres(obj, featureName);
        Feature feature = sub.getFeatures().get(featureName);
        feature.add(value);
      }

      // Añadir objeto al dataset
      sub.getData().add(obj);
    }

    return sub;
  }

}