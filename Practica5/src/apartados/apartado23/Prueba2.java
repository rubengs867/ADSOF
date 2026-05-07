package apartados.apartado23;

import apartados.apartado1.Person;
import apartados.apartado1.Prueba1;
import model.Dataset;
import tree.DecisionTree;

/**
 * Clase de prueba para demostrar el uso de DecisionTree
 * con objetos Person y datasets dado por el enunciado de la práctica.
 */
public class Prueba2 {

  /**
   * Punto de entrada de ejecución.
   *
   * @param args argumentos de línea de comandos
   */
  public static void main(String[] args) {

    Dataset<Person> dataSet = Prueba1.buildDataSet();
    DecisionTree<Person> dt = buildPersonDecisionTree();

    System.out.println(dt.predict(dataSet));

    System.out.println(
        dt.predict(
            new Person("Miguel", 86, 72, 165, true),
            new Person("Clara", 42, 59, 162, false)));
  }

  /**
   * Construye un árbol de decisión para clasificar personas
   * según sexo y rango de edad.
   *
   * @return árbol de decisión configurado
   */
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