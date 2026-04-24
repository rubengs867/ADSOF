package apartado4;

import java.util.Arrays;
import java.util.List;
import dataset.IFeaturizer; 

public class WeatherFeaturizer implements IFeaturizer<Weather> {
    @Override
    public List<String> featureDeInteres() {
        return Arrays.asList("condition", "temperature");
    }

    @Override
    public Comparable datoDeInteres(Weather obj, String featureName) {
        if (featureName.equals("condition")) return obj.getCondition().name();
        if (featureName.equals("temperature")) return obj.getTemperature().name();
        return null;
    }
}