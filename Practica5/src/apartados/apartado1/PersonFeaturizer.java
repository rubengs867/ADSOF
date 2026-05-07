package apartados.apartado1;

import java.util.List;

import model.IFeaturizer;

/**
 * Permite obtener las features de interés de un objeto de tipo {@link Person} y
 * los valores de cada feature.
 */
public class PersonFeaturizer implements IFeaturizer<Person> {

  /**
   * Devuelve la lista de features de interés de un objeto {@link Person}
   */
  @Override
  public List<String> featureDeInteres() {
    return List.of("age", "weight", "gender");
  }

  /**
   * Devuelve el valor de una feature de un objeto {@link Person}
   */
  @Override
  public Comparable<?> datoDeInteres(Person object, String featureName) {
    Comparable<?> c = null;
    switch (featureName) {
      case "age":
        c = object.getAge();
        break;
      case "weight":
        c = object.getWeight();
        break;
      case "gender":
      default:
        c = object.isMale() ? "MALE" : "FEMALE";
        break;
    }
    return c;
  }

}
