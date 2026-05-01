package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dataset<T> {

  /** Datos de interés de los objetos que componen el Dataset */
  private LinkedHashMap<String, Feature<?>> features = new LinkedHashMap<>();

  /** Interfaz que obtiene las features de interés */
  private IFeaturizer<T> featurizer;

  private LinkedHashSet<T> data = new LinkedHashSet<>();

  /**
   * Constructor base de Dataset
   * 
   * @param featurizer Interfaz para extraer datos
   */
  public Dataset(IFeaturizer<T> featurizer) {
    this.featurizer = featurizer;

    // Inicializamos una Feature por cada característica de interés
    for (String featureName : featurizer.featureDeInteres()) {
      features.put(featureName, new Feature<>());
    }
  }

  /**
   * Recibe un array de objetos, extrae sus features y las añade al dataset
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addAll(T[] objects) {
    for (T obj : objects) {
      // Recorro todas las features de interés del objeto (mismas que las claves del
      // mapa)
      for (String featureName : featurizer.featureDeInteres()) {

        // Obtengo el valor de cada feature de interés del objeto
        Comparable value = featurizer.datoDeInteres(obj, featureName);

        // Añado el valor al Feature del mapa de datos
        Feature currentFeature = features.get(featureName);
        currentFeature.add(value);
      }

      data.add(obj);
    }
  }

  /**
   * Devuelve la Feature correspondiente con un casteo automático al tipo de dato
   * esperado
   */
  @SuppressWarnings("unchecked")
  public <R extends Comparable<R>> Feature<R> feature(String featureName) {
    return (Feature<R>) features.get(featureName);
  }

  /**
   * Elimina las filas duplicadas.
   * Dos filas son duplicadas si para todas las features sus valores son iguales.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public boolean removeDuplicates() {
    // Comprobar que el dataset no está vacío y contiene features.
    if (features.isEmpty() || features.values().iterator().next().isEmpty()) {
      return false;
    }

    // Tamaño de una de las listas de Feature
    int size = features.values().iterator().next().size();

    /*
     * Set que va a comparar las filas compuestas por las features de cada objeto
     * que compone el dataset
     */
    Set<List<Object>> unicos = new HashSet<>();

    // Índices de las filas únicas
    List<Integer> indices = new ArrayList<>();

    // Recorremos por filas e identificamos cuáles son únicas
    for (int i = 0; i < size; i++) {

      // Creo la lista de features de interés que define a un objeto
      List<Object> fila = new ArrayList<>();
      for (String key : featurizer.featureDeInteres()) {
        fila.add(features.get(key).get(i));
      }

      // HashSet.add() devuelve true si el elemento no estaba previamente en el Set
      if (unicos.add(fila)) {
        indices.add(i);
      }
    }

    // Si todos los índices se conservan, no había duplicados
    if (indices.size() == size) {
      return false;
    }

    // Reconstruimos el mapa de Feature solo con los índices filtrados
    for (String featureName : featurizer.featureDeInteres()) {
      Feature antiguo = features.get(featureName);
      Feature nuevo = new Feature();
      for (int i : indices) {
        nuevo.add((Comparable) antiguo.get(i));
      }
      features.put(featureName, nuevo);
    }

    List<T> dataList = new ArrayList<>(this.data);
    this.data.clear(); // Vaciamos el set actual

    // Añadimos de nuevo solo los elementos cuyos índices sobrevivieron
    for (int i : indices) {
      this.data.add(dataList.get(i));
    }

    return true;
  }

  @Override
  public String toString() {
    return features.toString();
  }

  public Map<String, Feature<?>> getFeatures() {
    return features;
  }

  public IFeaturizer<T> getFeaturizer() {
    return featurizer;
  }

  public LinkedHashSet<T> getData() {
    return data;
  }
}