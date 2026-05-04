package apartado6;

import apartado1.Person;
import tree.DecisionTree;
import visualization.GraphvizTreeVisitor;
import visualization.PlainTextTreeVisitor;

/**
 * Clase de pruebas exhaustivas para el Apartado 6.
 * Evalúa el patrón Visitor para la visualización del árbol de decisión.
 */
public class Main6 {

  public static void main(String[] args) {
    // Construcción del árbol de prueba
    DecisionTree<Person> decisionTree = buildComplexTree();

    // Prueba del PlainTextTreeVisitor
    testPlainTextVisitor(decisionTree);

    // Prueba del GraphvizTreeVisitor
    testGraphvizVisitor(decisionTree);

    // Validación de Profundidad (Depth) y Casos Límite
    testDepthAndCornerCases();
  }

  /**
   * Construye un árbol multinivel para pruebas de Visitor.
   * Estructura: ROOT -> ADULTO (cond) / MENOR (otherwise)
   * ADULTO -> PESADO (cond) / LIGERO (otherwise)
   */
  private static DecisionTree<Person> buildComplexTree() {
    DecisionTree<Person> dt = new DecisionTree<>();

    // Nivel 1: Clasificación por edad
    dt.node("ROOT")
        .withCondition("ADULTO", p -> p.getAge() >= 18)
        .otherwise("MENOR");

    // Nivel 2: Subclasificación de adultos por peso
    dt.node("ADULTO")
        .withCondition("PESADO", p -> p.getWeight() >= 80)
        .otherwise("LIGERO");

    return dt;
  }

  /**
   * Ejecuta el visitante de texto plano y verifica la indentación.
   */
  private static void testPlainTextVisitor(DecisionTree<Person> tree) {
    System.out.println("===== VISITOR TEXTO PLANO =====");
    PlainTextTreeVisitor visitor = new PlainTextTreeVisitor();

    System.out.println("Representación jerárquica (Root Depth = 1):");
    // El método accept inicia el recorrido[cite: 22, 25]
    tree.getRaiz().accept(visitor, 1);
    System.out.println();
  }

  /**
   * Ejecuta el visitante de Graphviz y genera el código DOT.
   */
  private static void testGraphvizVisitor(DecisionTree<Person> tree) {
    System.out.println("===== VISITOR GRAPHVIZ =====");
    GraphvizTreeVisitor visitor = new GraphvizTreeVisitor();

    // Recorrido para generar el grafo[cite: 20, 25]
    tree.getRaiz().accept(visitor, 1);

    System.out.println("Código DOT generado:");
    System.out.println(visitor.getResult());
    System.out.println();
  }

  /**
   * Valida casos frontera y el incremento correcto de la profundidad.
   */
  private static void testDepthAndCornerCases() {
    System.out.println("===== CASOS LÍMITE =====");
    PlainTextTreeVisitor visitor = new PlainTextTreeVisitor();

    // Caso: Árbol con solo raíz
    System.out.println(" Caso 1: Solo Raíz ");
    DecisionTree<Person> single = new DecisionTree<>();
    single.node("ROOT_ONLY");
    single.getRaiz().accept(visitor, 1);

    // Caso: Nodo con únicamente Otherwise (prueba de Rama virtual)
    System.out.println("\n Caso 2: Solo Otherwise (Rama Virtual) ");
    DecisionTree<Person> onlyOtherwise = new DecisionTree<>();
    onlyOtherwise.node("START").otherwise("END");
    // El accept del nodo debe crear la rama virtual para el visitor[cite: 25]
    onlyOtherwise.getRaiz().accept(visitor, 1);

    // Caso: Profundidad incremental en árbol lineal
    System.out.println("\n Caso 3: Profundidad Incremental ");
    DecisionTree<Person> linear = new DecisionTree<>();
    linear.node("Nivel 1").withCondition("Nivel 2", p -> true);
    linear.node("Nivel 2").withCondition("Nivel 3", p -> true);
    linear.node("Nivel 3").withCondition("Hoja", p -> true);

    linear.getRaiz().accept(visitor, 1);
  }
}