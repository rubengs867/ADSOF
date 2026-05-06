package tree;

import org.junit.Before;
import org.junit.Test;
import java.util.function.Predicate;
import static org.junit.Assert.*;

public class NodeTest {

  private DecisionTree<Integer> tree;
  private Node<Integer> node;

  @Before
  public void setUp() {
    tree = new DecisionTree<>();
    node = tree.node("TestNode");
  }

  @Test
    public void shouldAddBranchWhenWithConditionIsCalled() {
        Predicate<Integer> condition = x -> x > 5;
        node.withCondition("Target", condition);

        assertEquals(1, node.getRamas().size());
        assertEquals("Target", node.getRamas().get(0).getNodoDestino().getName());
        assertEquals(condition, node.getRamas().get(0).getCondicion());
    }

  @Test
  public void shouldSetDefaultNodeWhenOtherwiseIsCalled() {
    node.otherwise("DefaultNode");
    assertNotNull(node.getNodoPorDefecto());
    assertEquals("DefaultNode", node.getNodoPorDefecto().getName());
  }

  @Test
    public void shouldEvaluateToMatchingBranch() {
        node.withCondition("B1", x -> x == 1)
            .withCondition("B2", x -> x == 2);

        assertEquals("B2", node.evaluate(2));
    }

  @Test
  public void shouldEvaluateToOtherwiseWhenNoBranchMatches() {
    node.withCondition("B1", x -> x == 1).otherwise("Fallback");
    assertEquals("Fallback", node.evaluate(99));
  }

  @Test
  public void shouldBeEqualWhenNamesAreSame() {
    Node<Integer> other = new Node<>("TestNode", tree);
    assertEquals(node, other);
    assertEquals(node.hashCode(), other.hashCode());
  }

  @Test
  public void shouldNotBeEqualWhenNamesAreDifferent() {
    Node<Integer> other = new Node<>("Other", tree);
    assertNotEquals(node, other);
  }
}