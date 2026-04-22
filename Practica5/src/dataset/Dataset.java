package dataset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dataset<T> {

  /** Datos de interés de los objetos que componen el Dataset */
  //tal vez habria que poner que ? extienda de algo por si acaso
  private LinkedHashMap<String, Feature<?>> data = new LinkedHashMap<>();
  List<T> objetos = new ArrayList<>();  
  /** Interfaz que obtiene las features de interés */
  private IFeaturizer<T> featurizer;

  /**
   * Constructor base de Dataset
   * 
   * @param featurizer Interfaz para extraer datos
   */
  public Dataset(IFeaturizer<T> featurizer) {
    this.featurizer = featurizer;

    // Inicializamos una Feature por cada característica de interés
    for (String featureName : featurizer.featureDeInteres()) {
      data.put(featureName, new Feature<>());
    }
  }

  /**
   * Recibe un array de objetos, extrae sus features y las añade al dataset
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addAll(T[] objects) {
    for (T obj : objects) {
      this.objetos.add(obj);
      // Recorro todas las features de interés del objeto (mismas que las claves del mapa)
      for (String featureName : featurizer.featureDeInteres()) {

        // Obtengo el valor de cada feature de interés del objeto
        Comparable value = featurizer.datoDeInteres(obj, featureName);

        // Añado el valor al Feature del mapa de datos
        Feature currentFeature = data.get(featureName);
        currentFeature.add(value);
      }
    }
  }

  public List<T> getObjetos() {
      return this.objetos;
  }

  /**
   * Devuelve la Feature correspondiente con un casteo automático al tipo de dato
   * esperado
   */
  @SuppressWarnings("unchecked")
  public <R extends Comparable<R>> Feature<R> feature(String featureName) {
    return (Feature<R>) data.get(featureName);
  }

  /**
   * Elimina las filas duplicadas.
   * Dos filas son duplicadas si para todas las features sus valores son iguales.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public boolean removeDuplicates() {
    // Comprobar que el dataset no está vacío y contiene features.
    if (data.isEmpty() || data.values().iterator().next().isEmpty()) {
      return false;
    }

    // Tamaño de una de las listas de Feature
    int size = data.values().iterator().next().size();

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
        fila.add(data.get(key).get(i));
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
      Feature antiguo = data.get(featureName);
      Feature nuevo = new Feature();
      for (int i : indices) {
        nuevo.add((Comparable) antiguo.get(i));
      }
      data.put(featureName, nuevo);
    }

    List<T> objetosFiltrados = new ArrayList<>();
    for (int i : indices) {
        objetosFiltrados.add(this.objetos.get(i));
    }
    this.objetos = objetosFiltrados;

    return true;
  }

  @Override
  public String toString() {
    return data.toString();
  }

  public Map<String, Feature<?>> getData() {
    return data;
  }

  public IFeaturizer<T> getFeaturizer() {
    return featurizer;
  }
}