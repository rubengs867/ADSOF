package model;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase genérica {@link Feature}.
 * Evalúa las capacidades estadísticas (mínimo, máximo, distribución) y
 * las operaciones de lista estándar sobre colecciones de datos comparables.
 */
public class FeatureTest {

  /** Lista Feature vacía */
  private Feature<Integer> emptyFeature;

  /** Lista Feature poblada */
  private Feature<Integer> populatedFeature;

  /** Lista Feature compuesta por strings */
  private Feature<String> stringFeature;

  /**
   * Configuración inicial de los objetos de prueba.
   * Crea una característica vacía y dos pobladas (Integer y String).
   */
  @Before
  public void setUp() {
    emptyFeature = new Feature<>();

    populatedFeature = new Feature<>();
    populatedFeature.add(5);
    populatedFeature.add(1);
    populatedFeature.add(10);
    populatedFeature.add(5);

    stringFeature = new Feature<>();
    stringFeature.add("Apple");
    stringFeature.add("Zebra");
    stringFeature.add("Banana");
  }

  /**
   * Verifica que el cálculo del valor mínimo devuelva null en una característica
   * sin datos.
   */
  @Test
  public void shouldReturnNullWhenMinCalledOnEmptyFeature() {
    assertNull(emptyFeature.min());
  }

  /**
   * Verifica que el método min devuelva el valor menor según el orden natural del
   * tipo.
   */
  @Test
  public void shouldReturnMinimumValueWhenFeatureContainsElements() {
    assertEquals(Integer.valueOf(1), populatedFeature.min());
    assertEquals("Apple", stringFeature.min());
  }

  /**
   * Verifica que el cálculo del valor máximo devuelva null en una característica
   * sin datos.
   */
  @Test
  public void shouldReturnNullWhenMaxCalledOnEmptyFeature() {
    assertNull(emptyFeature.max());
  }

  /**
   * Verifica que el método max devuelva el valor mayor según el orden natural del
   * tipo.
   */
  @Test
  public void shouldReturnMaximumValueWhenFeatureContainsElements() {
    assertEquals(Integer.valueOf(10), populatedFeature.max());
    assertEquals("Zebra", stringFeature.max());
  }

  /**
   * Verifica que la generación de la distribución de frecuencias devuelva null si
   * no hay elementos.
   */
  @Test
  public void shouldReturnNullWhenDistributionCalledOnEmptyFeature() {
    assertNull(emptyFeature.distribution());
  }

  /**
   * Valida que el mapa de distribución calcule correctamente la frecuencia de
   * cada elemento único.
   */
  @Test
  public void shouldReturnCorrectDistributionMapWhenFeatureContainsElements() {
    Map<Integer, Integer> dist = populatedFeature.distribution();

    assertNotNull(dist);
    // El dataset poblado tiene 3 valores únicos: 1, 5, 10
    assertEquals(3, dist.size());
    assertEquals(Integer.valueOf(2), dist.get(5)); // El 5 aparece dos veces
    assertEquals(Integer.valueOf(1), dist.get(1));
    assertEquals(Integer.valueOf(1), dist.get(10));
  }

  /**
   * Comprueba que la clase mantenga el comportamiento esperado de una lista
   * (herencia de ArrayList).
   */
  @Test
  public void shouldAllowStandardListOperations() {
    emptyFeature.add(100);
    assertEquals(1, emptyFeature.size());
    assertTrue(emptyFeature.contains(100));

    emptyFeature.remove(Integer.valueOf(100));
    assertTrue(emptyFeature.isEmpty());
  }
}