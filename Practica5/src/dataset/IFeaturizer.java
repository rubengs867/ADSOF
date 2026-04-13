package dataset;

import java.util.List;

public interface IFeaturizer {

    public List<String> featureDeInteres();
    public List<?> datosDeInteres(String featureInteres);
}
