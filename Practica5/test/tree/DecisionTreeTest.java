package tree;

import model.Dataset;
import model.IFeaturizer;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el motor del árbol de decisión.
 * Evalúa la creación de nodos, la lógica de predicción y la reconstrucción de
 * predicados de ruta.
 */
public class DecisionTreeTest {

  /** Decision tree de apoyo para probar */
  private DecisionTree<Integer> tree;

  /**
   * Inicializa un árbol vacío antes de cada prueba.
   */
  @Before
  public void setUp() {
    tree = new DecisionTree<>();
  }

  /**
   * Verifica que al crear el primer nodo se establezca como raíz automáticamente.
   */
  @Test
  public void shouldCreateNewNodeAndSetAsRootWhenTreeIsEmpty() {
    Node<Integer> node = tree.node("root");

    assertNotNull(node);
    assertEquals("root", node.getName());
    assertEquals(node, tree.getRaiz());
    assertTrue(tree.getNodosVisitados().contains(node));
  }

  /**
   * Comprueba el mecanismo de caché de nodos para evitar duplicados por nombre.
   */
  @Test
  public void shouldRetrieveExistingNodeWhenNameAlreadyExists() {
    Node<Integer> first = tree.node("A");
    Node<Integer> second = tree.node("A");

    assertSame(first, second);
    assertEquals(1, tree.getNodos().size());
  }

  /**
   * Valida la predicción por lotes sobre una secuencia de elementos.
   */
  @Test
  public void shouldPredictMultipleItemsCorrectly() {
    tree.node("root")
        .withCondition("Even", x -> x % 2 == 0)
        .otherwise("Odd");

    Map<String, List<Integer>> results = tree.predict(1, 2, 3, 4, 5);

    assertEquals(2, results.size());
    assertEquals(Arrays.asList(2, 4), results.get("Even"));
    assertEquals(Arrays.asList(1, 3, 5), results.get("Odd"));
  }

  /**
   * Valida la predicción utilizando un objeto Dataset como entrada.
   */
  @Test
  public void shouldPredictDatasetCorrectly() {
    IFeaturizer<Integer> simpleFeaturizer = new IFeaturizer<Integer>() {
      @Override
      public List<String> featureDeInteres() {
        return Arrays.asList("value");
      }

      @Override
      public Comparable<?> datoDeInteres(Integer obj, String f) {
        return obj;
      }
    };

    Dataset<Integer> dataset = new Dataset<>(simpleFeaturizer);
    dataset.addAll(new Integer[] { -5, 10, 0 });

    tree.node("root")
        .withCondition("Positive", x -> x > 0)
        .otherwise("NegativeOrZero");

    Map<String, List<Integer>> results = tree.predict(dataset);

    assertEquals(2, results.size());
    assertEquals(List.of(10), results.get("Positive"));
    assertEquals(Arrays.asList(-5, 0), results.get("NegativeOrZero"));
  }

  /**
   * Verifica que se devuelva el nombre del nodo hoja al finalizar la evaluación.
   */
  @Test
  public void shouldReturnNodeNameWhenLeafNodeIsReached() {
    tree.node("root").withCondition("leaf", x -> true);
    Map<String, List<Integer>> results = tree.predict(99);
    assertTrue(results.containsKey("leaf"));
  }

  /**
   * Valida que el predicado compuesto generado para un nodo represente
   * correctamente la conjunción de condiciones de la ruta desde la raíz.
   */
  @Test
  public void shouldGenerateCorrectPredicateForComplexPath() {
    tree.node("root")
        .withCondition("BranchA", x -> x > 10)
        .otherwise("BranchB");

    tree.node("BranchA")
        .withCondition("Target", x -> x == 15);

    Predicate<Integer> predicate = tree.getPredicate("Target");

    assertNotNull(predicate);
    assertTrue(predicate.test(15)); // (x > 10) AND (x == 15)
    assertFalse(predicate.test(5)); // Falla primera condición
    assertFalse(predicate.test(11)); // Falla segunda condición
  }

  /**
   * Verifica que se devuelva null si se solicita el predicado de un nodo
   * inalcanzable.
   */
  @Test
  public void shouldReturnNullPredicateWhenNoPathExists() {
    tree.node("root").otherwise("End");
    assertNull(tree.getPredicate("NonExistent"));
  }
}