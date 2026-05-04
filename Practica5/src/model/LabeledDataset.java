package model;

import java.util.List;

/**
 * Representa un dataset cuyos objetos tienen una etiqueta asociada.
 * Combina características extraídas mediante un featurizer con un
 * proveedor de etiquetas.
 *
 * @param <T> Tipo de objeto almacenado.
 * @param <L> Tipo de etiqueta asociada.
 */
public class LabeledDataset<T, L> extends Dataset<T> {

  /**
   * Componente encargado de obtener etiquetas.
   */
  private ILabelProvider<T, L> labelProvider;

  /**
   * Crea un dataset etiquetado.
   *
   * @param featurizer    Extractor de características.
   * @param labelProvider Proveedor de etiquetas.
   */
  public LabeledDataset(IFeaturizer<T> featurizer, ILabelProvider<T, L> labelProvider) {
    super(featurizer);
    this.labelProvider = labelProvider;
  }

  /**
   * Devuelve la etiqueta asociada a un objeto.
   *
   * @param object Objeto consultado.
   *
   * @return Etiqueta correspondiente.
   */
  public L getLabel(T object) {
    return labelProvider.getLabel(object);
  }

  /**
   * Devuelve el proveedor de etiquetas configurado.
   *
   * @return Instancia de {@link ILabelProvider}.
   */
  public ILabelProvider<T, L> getLabelProvider() {
    return this.labelProvider;
  }

  /**
   * Genera un subconjunto del dataset con una selección de datos y columnas.
   *
   * @param datos                 Datos que formarán el subconjunto.
   * @param featuresSeleccionadas Características a conservar.
   *
   * @return Nuevo dataset filtrado.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public LabeledDataset<T, L> subset(List<T> datos, List<String> featuresSeleccionadas) {

    LabeledDataset<T, L> sub = new LabeledDataset<>(this.getFeaturizer(), this.labelProvider);

    // Conserva únicamente las columnas seleccionadas.
    sub.getFeatures().keySet().retainAll(featuresSeleccionadas);

    for (T obj : datos) {

      for (String featureName : featuresSeleccionadas) {
        Comparable value = getFeaturizer().datoDeInteres(obj, featureName);

        Feature feature = sub.getFeatures().get(featureName);
        feature.add(value);
      }

      sub.getData().add(obj);
    }

    return sub;
  }
}