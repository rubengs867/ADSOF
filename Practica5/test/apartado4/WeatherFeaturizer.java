package apartado4;

import java.util.Arrays;
import java.util.List;

import model.IFeaturizer;

/**
 * Implementación de extracción de características para objetos Weather.
 */
public class WeatherFeaturizer implements IFeaturizer<Weather> {

  /**
   * Devuelve la lista de características disponibles.
   *
   * @return nombres de atributos utilizables
   */
  @Override
  public List<String> featureDeInteres() {
    return Arrays.asList("condition", "temperature");
  }

  /**
   * Obtiene el valor de una característica concreta.
   *
   * @param obj         objeto evaluado
   * @param featureName nombre de la característica
   * @return valor comparable asociado, o {@code null} si no existe
   */
  @Override
  public Comparable datoDeInteres(Weather obj, String featureName) {
    if (featureName.equals("condition")) {
      return obj.getCondition().name();
    }

    if (featureName.equals("temperature")) {
      return obj.getTemperature().name();
    }

    return null;
  }
}