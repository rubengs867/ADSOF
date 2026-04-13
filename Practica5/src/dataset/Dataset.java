package dataset;

import java.util.HashMap;
import java.util.Map;

public class Dataset<T extends Comparable<T>> {
  Map<String, Feature<?>> datos = new HashMap<>();
  IFeaturizer featurizer;
  Feature<T> objetos;
  
  /**
   * Constructor base de {@link Dataset}
   * @param featurizer Interfaz para 
   */
  public Dataset(IFeaturizer featurizer) {
    this.featurizer = featurizer;
  }
  
  
  
  
  
  
  
  public Map<String, Feature<?>> getDatos() {
    return datos;
  }
  public void setDatos(Map<String, Feature<?>> datos) {
    this.datos = datos;
  }
  public IFeaturizer getFeaturizer() {
    return featurizer;
  }
  public void setFeaturizer(IFeaturizer featurizer) {
    this.featurizer = featurizer;
  }
  public Feature<T> getObjetos() {
    return objetos;
  }
  public void setObjetos(Feature<T> objetos) {
    this.objetos = objetos;
  }

  
}