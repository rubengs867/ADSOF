package dataset;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dataset<T extends Comparable<T>> {

  /** Datos de interés de los objetos que componen el Dataset */
  Map<String, Feature<?>> data = new LinkedHashMap<>();

  /**
   * Interfaz que obtiene las features de interés y el valor que tienen dichas
   * features
   */
  IFeaturizer<T> featurizer;

  /** Objetos que componen el Dataset */
  List<T> objects;

  /**
   * Constructor base de {@link Dataset}
   * 
   * @param featurizer Interfaz para
   */
  public Dataset(IFeaturizer<T> featurizer) {
    this.featurizer = featurizer;
  }

  public Feature<?> feature(String featureName) {
    return data.get(featureName);
  }

  public boolean removeDuplicates() {
    return false;
  }

  public Map<String, Feature<?>> getData() {
    return data;
  }

  public IFeaturizer<T> getFeaturizer() {
    return featurizer;
  }

  public List<T> getObjects() {
    return objects;
  }
}