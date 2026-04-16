package dataset;

import java.util.List;

public interface IFeaturizer<T> {

  public List<String> featureDeInteres();

  public Object datoDeInteres(T object, String featureName);
}
