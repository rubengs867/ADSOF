package apartado2;

import decisionTree.*;
import dataset.*;

import apartado1.Person;
import apartado1.PersonFeaturizer;

public class Main {

  public static void main(String[] args) {
    Dataset<Person> dataSet = buildDataSet();
    DecisionTree<Person> dt = buildPersonDecisionTree();

    System.out.println(dt.predict(dataSet));
    System.out.println(dt.predict(new Person("Miguel", 86, 72, 165, true), new Person("Clara", 42, 59, 162, false)));
  }

  public static DecisionTree<Person> buildPersonDecisionTree() {
    DecisionTree<Person> dt = new DecisionTree<>();
    dt.node("root")
        .withCondition("male", p -> p.isGender())
        .otherwise("female");
    dt.node("male")
        .withCondition("old male", p -> p.getAge() > 65)
        .withCondition("middle male", p -> p.getAge() <= 65 && p.getAge() > 34)
        .otherwise("young male");
    return dt;
  }

  public static Dataset<Person> buildDataSet() {
    Person people[] = { new Person("Pedro", 66, 75, 180, true), // name, age, weight, height, male?
        new Person("Ana", 47, 54, 158, false),
        new Person("Luis", 34, 75, 176, true),
        new Person("Rosa", 47, 54, 158, false)
    };

    Dataset<Person> dataSet = new Dataset<>(new PersonFeaturizer()); // A Featurizer for Person objects
    dataSet.addAll(people);
    return dataSet;
  }

} 
