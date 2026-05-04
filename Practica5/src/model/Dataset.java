package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Estructura de datos que almacena objetos y sus características de interés
 * organizadas por columnas.
 *
 * @param <T> Tipo de objeto base almacenado en el dataset.
 */
public class Dataset<T> {

  /**
   * Mapa de características con sus valores asociados.
   */
  private LinkedHashMap<String, Feature<?>> features = new LinkedHashMap<>();

  /**
   * Componente encargado de extraer las características de los objetos.
   */
  private IFeaturizer<T> featurizer;

  /**
   * Conjunto de objetos originales almacenados.
   */
  private LinkedHashSet<T> data = new LinkedHashSet<>();

  /**
   * Crea un dataset vacío e inicializa sus características disponibles.
   *
   * @param featurizer Estrategia para obtener los datos de interés.
   */
  public Dataset(IFeaturizer<T> featurizer) {
    this.featurizer = featurizer;

    // Inicializa una columna por cada característica declarada.
    for (String featureName : featurizer.featureDeInteres()) {
      features.put(featureName, new Feature<>());
    }
  }

  /**
   * Añade múltiples objetos al dataset y extrae automáticamente sus
   * características configuradas.
   *
   * @param objects Objetos a insertar.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addAll(T[] objects) {
    for (T obj : objects) {
      for (String featureName : featurizer.featureDeInteres()) {

        Comparable value = featurizer.datoDeInteres(obj, featureName);

        Feature currentFeature = features.get(featureName);
        currentFeature.add(value);
      }

      data.add(obj);
    }
  }

  /**
   * Devuelve una característica concreta del dataset.
   *
   * @param <R>         Tipo de dato de la característica.
   * @param featureName Nombre de la característica.
   *
   * @return Feature solicitada o {@code null} si no existe.
   */
  @SuppressWarnings("unchecked")
  public <R extends Comparable<R>> Feature<R> feature(String featureName) {
    return (Feature<R>) features.get(featureName);
  }

  /**
   * Elimina filas duplicadas del dataset.
   * Dos filas se consideran duplicadas cuando todas sus características
   * contienen los mismos valores en el mismo orden.
   *
   * @return {@code true} si se eliminaron duplicados, {@code false} en caso
   *         contrario.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public boolean removeDuplicates() {

    // Verifica que existan columnas con datos.
    if (features.isEmpty() || features.values().iterator().next().isEmpty()) {
      return false;
    }

    int size = features.values().iterator().next().size();

    Set<List<Object>> uniqueRows = new HashSet<>();
    List<Integer> validIndexes = new ArrayList<>();

    // Detecta filas únicas.
    for (int i = 0; i < size; i++) {

      List<Object> row = new ArrayList<>();

      for (String key : featurizer.featureDeInteres()) {
        row.add(features.get(key).get(i));
      }

      if (uniqueRows.add(row)) {
        validIndexes.add(i);
      }
    }

    // No había duplicados.
    if (validIndexes.size() == size) {
      return false;
    }

    // Reconstruye cada columna filtrando índices válidos.
    for (String featureName : featurizer.featureDeInteres()) {

      Feature oldFeature = features.get(featureName);
      Feature newFeature = new Feature();

      for (int index : validIndexes) {
        newFeature.add((Comparable) oldFeature.get(index));
      }

      features.put(featureName, newFeature);
    }

    List<T> dataList = new ArrayList<>(data);
    data.clear();

    // Reconstruye los objetos originales conservados.
    for (int index : validIndexes) {
      data.add(dataList.get(index));
    }

    return true;
  }

  /**
   * Devuelve una representación textual del dataset.
   *
   * @return Texto representando las características almacenadas.
   */
  @Override
  public String toString() {
    return features.toString();
  }

  /**
   * Devuelve el mapa completo de características.
   *
   * @return Mapa de features.
   */
  public Map<String, Feature<?>> getFeatures() {
    return features;
  }

  /**
   * Devuelve el componente extractor de características.
   *
   * @return Instancia de {@link IFeaturizer}.
   */
  public IFeaturizer<T> getFeaturizer() {
    return featurizer;
  }

  /**
   * Devuelve los objetos originales almacenados.
   *
   * @return Conjunto de datos base.
   */
  public LinkedHashSet<T> getData() {
    return data;
  }
}