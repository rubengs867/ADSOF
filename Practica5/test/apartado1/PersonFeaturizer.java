package apartado1;

import java.util.List;

import model.IFeaturizer;

public class PersonFeaturizer implements IFeaturizer<Person> {

  @Override
  public List<String> featureDeInteres() {
    return List.of("age", "weight", "gender");
  }

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
