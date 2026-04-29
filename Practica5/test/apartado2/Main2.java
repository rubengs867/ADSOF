package apartado2;

import java.util.function.Predicate;

import apartado1.Main1;
import apartado1.Person;
import model.Dataset;
import tree.DecisionTree;

public class Main2 {

  public static void main(String[] args) {
    Dataset<Person> dataSet = Main1.buildDataSet();
    DecisionTree<Person> dt = buildPersonDecisionTree();

    System.out.println(dt.predict(dataSet));
    System.out.println(dt.predict(new Person("Miguel", 86, 72, 165, true), new Person("Clara", 42, 59, 162, false)));

    Predicate<Person> isYoungMale = dt.getPredicate("young male");
    System.out.println("Es Pedro un young male? " + isYoungMale.test(new Person("Pedro", 66, 75, 180, true)));
    System.out.println("Es Luis un young male? " + isYoungMale.test(new Person("Luis", 34, 75, 176, true)));
  }

  public static DecisionTree<Person> buildPersonDecisionTree() {
    DecisionTree<Person> dt = new DecisionTree<>();
    dt.node("root")
        .withCondition("male", p -> p.isMale())
        .otherwise("female");
    dt.node("male")
        .withCondition("old male", p -> p.getAge() > 65)
        .withCondition("middle male", p -> p.getAge() <= 65 && p.getAge() > 34)
        .otherwise("young male");
    return dt;
  }
} 
