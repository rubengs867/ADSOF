package tree;

import org.junit.Before;
import org.junit.Test;
import java.util.function.Predicate;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para la clase {@link Node}.
 * Evalúa la gestión de ramas, evaluación de condiciones y lógica de igualdad.
 * * @author ADSOF
 */
public class NodeTest {

  /** Árbol de decisión de apoyo para usar en los test */
  private DecisionTree<Integer> tree;

  /** Nodo cuyo valor es un entero */
  private Node<Integer> node;

  /**
   * Inicializa un árbol y un nodo de prueba.
   */
  @Before
  public void setUp() {
    tree = new DecisionTree<>();
    node = tree.node("TestNode");
  }

  /**
   * Verifica que el método withCondition cree y registre una nueva rama
   * correctamente.
   */
  @Test
  public void shouldAddBranchWhenWithConditionIsCalled() {
    Predicate<Integer> condition = x -> x > 5;
    node.withCondition("Target", condition);

    assertEquals(1, node.getRamas().size());
    assertEquals("Target", node.getRamas().get(0).getNodoDestino().getName());
    assertEquals(condition, node.getRamas().get(0).getCondicion());
  }

  /**
   * Valida la asignación del nodo de salida por defecto (fallback).
   */
  @Test
  public void shouldSetDefaultNodeWhenOtherwiseIsCalled() {
    node.otherwise("DefaultNode");
    assertNotNull(node.getNodoPorDefecto());
    assertEquals("DefaultNode", node.getNodoPorDefecto().getName());
  }

  /**
   * Comprueba que la evaluación del nodo devuelva el nombre del destino
   * cuya condición se cumple.
   */
  @Test
  public void shouldEvaluateToMatchingBranch() {
    node.withCondition("B1", x -> x == 1)
        .withCondition("B2", x -> x == 2);

    assertEquals("B2", node.evaluate(2));
  }

  /**
   * Verifica que se utilice el nodo por defecto si ninguna rama cumple la
   * condición.
   */
  @Test
  public void shouldEvaluateToOtherwiseWhenNoBranchMatches() {
    node.withCondition("B1", x -> x == 1).otherwise("Fallback");
    assertEquals("Fallback", node.evaluate(99));
  }

  /**
   * Valida que la igualdad de nodos se base en el nombre.
   */
  @Test
  public void shouldBeEqualWhenNamesAreSame() {
    Node<Integer> other = new Node<>("TestNode", tree);
    assertEquals(node, other);
    assertEquals(node.hashCode(), other.hashCode());
  }

  /**
   * Valida que nodos con nombres distintos no sean iguales.
   */
  @Test
  public void shouldNotBeEqualWhenNamesAreDifferent() {
    Node<Integer> other = new Node<>("Other", tree);
    assertNotEquals(node, other);
  }
}