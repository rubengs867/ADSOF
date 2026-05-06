package apartado2_3;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import apartado1.Person;
import apartado1.PersonFeaturizer;
import model.Dataset;
import tree.DecisionTree;
import tree.Node;
import tree.Rama;

/**
 * Clase principal de pruebas para validar la construcción y uso
 * de árboles de decisión aplicados a objetos Person.
 */
public class Main_2_3 {

  /**
   * Punto de entrada de ejecución.
   *
   * @param args argumentos de línea de comandos
   */
  public static void main(String[] args) {
    // Personas de prueba
    Person p1 = new Person("Lucas", 15, 60, 170, true); // MENOR
    Person p2 = new Person("Marta", 17, 80, 165, false); // MENOR
    Person p3 = new Person("Ana", 25, 65, 168, false); // ADULTO_LIGERO
    Person p4 = new Person("Luis", 18, 69, 175, true); // ADULTO_LIGERO (frontera edad/peso)
    Person p5 = new Person("Pedro", 30, 80, 180, true); // ADULTO_PESADO
    Person p6 = new Person("Carmen", 18, 70, 160, false); // ADULTO_PESADO (frontera edad/peso)
    Person p7 = new Person("Hulk", 40, 160, 210, true); // DESCONOCIDO (peso >= 150)
    Person p8 = new Person("Error", -5, 50, 120, false); // DESCONOCIDO (edad negativa)

    Person[] allPersons = { p1, p2, p3, p4, p5, p6, p7, p8 };

    DecisionTree<Person> tree = buildTree1();

    testEstructura(tree);
    testEvaluate(tree, allPersons);
    testPredictVariadico(tree, allPersons);
    testPredictDataset(tree, allPersons);
    testPredicates(tree, allPersons);
    testMultiLevel();
    testCornerCases();
  }

  /**
   * Construye un árbol de decisión base para clasificar personas
   * según edad y peso.
   *
   * @return árbol de decisión configurado
   */
  private static DecisionTree<Person> buildTree1() {
    DecisionTree<Person> dt = new DecisionTree<>();

    dt.node("ROOT")
        .withCondition("MENOR", p -> p.getAge() > 0 && p.getAge() < 18)
        .withCondition("ADULTO_LIGERO", p -> p.getAge() >= 18 && p.getWeight() > 0 && p.getWeight() < 70)
        .withCondition("ADULTO_PESADO", p -> p.getAge() >= 18 && p.getWeight() >= 70 && p.getWeight() < 150)
        .otherwise("DESCONOCIDO");

    return dt;
  }

  /**
   * Muestra por consola la estructura interna del árbol,
   * incluyendo nodos y ramas disponibles.
   *
   * @param tree árbol de decisión analizado
   */
  private static void testEstructura(DecisionTree<Person> tree) {
    System.out.println("===== CONSTRUCCIÓN DEL ÁRBOL =====");
    Node<Person> root = tree.getRaiz();

    System.out.println("Nodo raíz: " + root.getName());
    System.out.println("Nodos creados (Mapa completo): " + tree.getNodos().keySet());

    System.out.println("\nAnálisis de Ramas desde la raíz:");
    for (Rama<Person> rama : root.getRamas()) {
      System.out.println("  Rama -> Origen: [" + rama.getOrigen().getName() +
          "] | Destino: [" + rama.getNodoDestino().getName() +
          "] | Condición presente: " + (rama.getCondicion() != null));
    }

    Node<Person> otherwiseNode = root.getNodoPorDefecto();
    System.out.println("Nodo por defecto (otherwise): [" +
        (otherwiseNode != null ? otherwiseNode.getName() : "Ninguno") + "]");
  }

  /**
   * Evalúa individualmente cada persona desde el nodo raíz
   * y muestra la etiqueta resultante.
   *
   * @param tree    árbol de decisión utilizado
   * @param persons personas evaluadas
   */
  private static void testEvaluate(DecisionTree<Person> tree, Person[] persons) {
    System.out.println("\n===== EVALUACIÓN INDIVIDUAL =====");
    Node<Person> root = tree.getRaiz();

    for (Person p : persons) {
      String destino = root.evaluate(p);
      System.out.println(p.getName() + " -> " + destino);
    }
  }

  /**
   * Ejecuta una predicción múltiple mediante argumentos variables.
   *
   * @param tree    árbol de decisión utilizado
   * @param persons personas clasificadas
   */
  private static void testPredictVariadico(DecisionTree<Person> tree, Person[] persons) {
    System.out.println("\n===== PREDICCIÓN =====");
    Map<String, List<Person>> resultados = tree.predict(persons);

    for (Map.Entry<String, List<Person>> entry : resultados.entrySet()) {
      System.out.println("Etiqueta: " + entry.getKey());
      for (Person p : entry.getValue()) {
        System.out.println("  - " + p.getName() + " (" + p.getAge() + "a, " + p.getWeight() + "kg)");
      }
    }
  }

  /**
   * Ejecuta una predicción utilizando un dataset.
   *
   * @param tree    árbol de decisión utilizado
   * @param persons personas incluidas en el dataset
   */
  private static void testPredictDataset(DecisionTree<Person> tree, Person[] persons) {
    System.out.println("\n===== PREDICCIÓN (CON DATASET) =====");
    Dataset<Person> dataset = new Dataset<>(new PersonFeaturizer());
    dataset.addAll(persons);

    Map<String, List<Person>> resultadosDs = tree.predict(dataset);

    for (Map.Entry<String, List<Person>> entry : resultadosDs.entrySet()) {
      System.out.println("Etiqueta: " + entry.getKey() + " -> Contiene " + entry.getValue().size() + " personas.");
    }
  }

  /**
   * Comprueba los predicados generados para cada etiqueta
   * del árbol de decisión.
   *
   * @param tree    árbol de decisión utilizado
   * @param persons personas evaluadas
   */
  private static void testPredicates(DecisionTree<Person> tree, Person[] persons) {
    System.out.println("\n===== PREDICADOS =====");
    String[] etiquetas = { "MENOR", "ADULTO_LIGERO", "ADULTO_PESADO", "DESCONOCIDO" };

    for (String etiqueta : etiquetas) {
      Predicate<Person> pred = tree.getPredicate(etiqueta);
      System.out.println("Evaluando predicado generado para: " + etiqueta);

      if (pred != null) {
        for (Person p : persons) {
          System.out.println(String.format("  %-10s -> %b", p.getName(), pred.test(p)));
        }
      } else {
        System.out.println("  Predicado es nulo.");
      }
    }
  }

  /**
   * Construye y prueba un árbol multinivel con varias profundidades.
   */
  private static void testMultiLevel() {
    System.out.println("\n===== ÁRBOL MULTINIVEL (PROFUNDIDAD >= 2) =====");
    DecisionTree<Person> tree2 = new DecisionTree<>();

    tree2.node("ROOT")
        .withCondition("ADULTO", p -> p.getAge() >= 18)
        .otherwise("MENOR");

    tree2.node("ADULTO")
        .withCondition("PESADO", p -> p.getWeight() >= 80)
        .otherwise("LIGERO");

    tree2.node("PESADO")
        .withCondition("GIGANTE", p -> p.getHeight() >= 190)
        .otherwise("ESTANDAR");

    System.out.println("Nodos en el mapa del árbol multinivel: " + tree2.getNodos().keySet());

    Person pEstandar = new Person("Pedro", 30, 80, 180, true);
    Person pGigante = new Person("Hulk", 40, 160, 210, true);
    Person pMenor = new Person("Timmy", 10, 40, 140, true);

    System.out.println("Recorrido para Pedro (30a, 80kg, 180cm) -> " + tree2.predict(pEstandar).keySet());
    System.out.println("Recorrido para Hulk (40a, 160kg, 210cm) -> " + tree2.predict(pGigante).keySet());
    System.out.println("Recorrido para Timmy (10a, 40kg, 140cm) -> " + tree2.predict(pMenor).keySet());
  }

  /**
   * Ejecuta pruebas sobre situaciones límite y estructuras mínimas.
   */
  private static void testCornerCases() {
    System.out.println("\n===== CASOS LÍMITE =====");
    DecisionTree<Person> emptyTree = new DecisionTree<>();
    emptyTree.node("ROOT_ONLY");

    System.out.println("1. Árbol con solo raíz (sin ramas ni otherwise):");
    Person testP = new Person("Fantasma", 20, 70, 170, true);
    Map<String, List<Person>> res = emptyTree.predict(testP);
    System.out.println("   -> Predicción devuelve etiqueta: " + res.keySet());

    System.out.println("2. Predicción con un Dataset vacío:");
    Dataset<Person> emptyDs = new Dataset<>(new PersonFeaturizer());
    Map<String, List<Person>> resDs = emptyTree.predict(emptyDs);
    System.out.println("   -> Resultados (Mapa vacío esperado): " + resDs);

    System.out.println("3. Obtención de predicado para etiqueta inexistente:");
    Predicate<Person> predFantasma = emptyTree.getPredicate("FANTASMA");
    System.out.println("   -> ¿Es el predicado null? " + (predFantasma == null));
  }
}