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

public class DecisionTreeTest {

  private DecisionTree<Integer> tree;

  @Before
  public void setUp() {
    tree = new DecisionTree<>();
  }

  @Test
  public void shouldCreateNewNodeAndSetAsRootWhenTreeIsEmpty() {
    Node<Integer> node = tree.node("root");

    assertNotNull(node);
    assertEquals("root", node.getName());
    assertEquals(node, tree.getRaiz());
    assertTrue(tree.getNodosVisitados().contains(node));
  }

  @Test
  public void shouldRetrieveExistingNodeWhenNameAlreadyExists() {
    Node<Integer> first = tree.node("A");
    Node<Integer> second = tree.node("A");

    assertSame(first, second);
    assertEquals(1, tree.getNodos().size());
  }

  @Test
  public void shouldPredictMultipleItemsCorrectly() {
    // Arrange
    tree.node("root")
        .withCondition("Even", x -> x % 2 == 0)
        .otherwise("Odd");

    // Act
    Map<String, List<Integer>> results = tree.predict(1, 2, 3, 4, 5);

    // Assert
    assertEquals(2, results.size());
    assertEquals(Arrays.asList(2, 4), results.get("Even"));
    assertEquals(Arrays.asList(1, 3, 5), results.get("Odd"));
  }

  @Test
    public void shouldPredictDatasetCorrectly() {
        // Arrange
        IFeaturizer<Integer> simpleFeaturizer = new IFeaturizer<Integer>() {
            @Override public List<String> featureDeInteres() { return Arrays.asList("value"); }
            @Override public Comparable<?> datoDeInteres(Integer obj, String f) { return obj; }
        };

        Dataset<Integer> dataset = new Dataset<>(simpleFeaturizer);
        dataset.addAll(new Integer[]{-5, 10, 0});

        tree.node("root")
                .withCondition("Positive", x -> x > 0)
                .otherwise("NegativeOrZero");

        // Act
        Map<String, List<Integer>> results = tree.predict(dataset);

        // Assert
        assertEquals(2, results.size());
        assertEquals(List.of(10), results.get("Positive"));
        assertEquals(Arrays.asList(-5, 0), results.get("NegativeOrZero"));
    }

  @Test
  public void shouldReturnNodeNameWhenLeafNodeIsReached() {
    tree.node("root").withCondition("leaf", x -> true);
    Map<String, List<Integer>> results = tree.predict(99);
    assertTrue(results.containsKey("leaf"));
  }

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
    assertFalse(predicate.test(5)); // Fails first condition
    assertFalse(predicate.test(11)); // Fails second condition
  }

  @Test
  public void shouldReturnNullPredicateWhenNoPathExists() {
    tree.node("root").otherwise("End");
    assertNull(tree.getPredicate("NonExistent"));
  }
}