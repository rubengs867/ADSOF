package tree;

import org.junit.Before;
import org.junit.Test;
import java.util.function.Predicate;
import static org.junit.Assert.*;

public class RamaTest {

  private Node<String> origen;
  private Node<String> destino;
  private Predicate<String> condicion;
  private Rama<String> rama;

  @Before
    public void setUp() {
        DecisionTree<String> tree = new DecisionTree<>();
        origen = tree.node("Origen");
        destino = tree.node("Destino");
        condicion = s -> s.length() > 3;

        rama = new Rama<>(origen, destino, condicion);
    }

  @Test
  public void shouldReturnCorrectProperties() {
    assertEquals(origen, rama.getOrigen());
    assertEquals(destino, rama.getNodoDestino());
    assertEquals(condicion, rama.getCondicion());
  }

  @Test
  public void shouldEvaluateConditionCorrectly() {
    assertTrue(rama.getCondicion().test("LongString"));
    assertFalse(rama.getCondicion().test("ABC"));
  }
}