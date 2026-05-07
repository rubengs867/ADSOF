package tree;

import org.junit.Before;
import org.junit.Test;
import java.util.function.Predicate;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para la clase {@link Rama}.
 * Verifica la integridad de las conexiones entre nodos y la lógica de
 * predicados.
 * * @author ADSOF
 */
public class RamaTest {

  /** Nodo origen */
  private Node<String> origen;
  /** Nodo destino */
  private Node<String> destino;
  /** Condición de la rama */
  private Predicate<String> condicion;
  /** Rama a usar en los test */
  private Rama<String> rama;

  /**
   * Inicializa una rama con nodos y condición de prueba.
   */
  @Before
  public void setUp() {
    DecisionTree<String> tree = new DecisionTree<>();
    origen = tree.node("Origen");
    destino = tree.node("Destino");
    condicion = s -> s.length() > 3;

    rama = new Rama<>(origen, destino, condicion);
  }

  /**
   * Valida que los atributos de la rama se asignen y recuperen correctamente.
   */
  @Test
  public void shouldReturnCorrectProperties() {
    assertEquals(origen, rama.getOrigen());
    assertEquals(destino, rama.getNodoDestino());
    assertEquals(condicion, rama.getCondicion());
  }

  /**
   * Comprueba la ejecución del predicado asociado a la rama.
   */
  @Test
  public void shouldEvaluateConditionCorrectly() {
    assertTrue(rama.getCondicion().test("LongString"));
    assertFalse(rama.getCondicion().test("ABC"));
  }
}