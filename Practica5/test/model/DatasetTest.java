package model;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase {@link Dataset}.
 * Verifica el correcto funcionamiento de la gestión de datos, extracción
 * de características y eliminación de duplicados.
 */
public class DatasetTest {

  /**
   * Entidad de prueba simplificada para validar el comportamiento del dataset.
   */
  static class DummyEntity {
    /** Nombre de la entidad. */
    String name;
    /** Edad de la entidad. */
    int age;

    /**
     * Constructor de la entidad de prueba.
     * * @param name Nombre de la entidad.
     * 
     * @param age Edad de la entidad.
     */
    DummyEntity(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  /**
   * Implementación de prueba de {@link IFeaturizer} para {@link DummyEntity}.
   */
  static class DummyFeaturizer implements IFeaturizer<DummyEntity> {

    /**
     * Define las características de interés para la entidad de prueba.
     * * @return Lista con los nombres de las características ("name", "age").
     */
    @Override
    public List<String> featureDeInteres() {
      return Arrays.asList("name", "age");
    }

    /**
     * Extrae el valor de una característica específica de un objeto.
     * * @param object Objeto del que extraer el dato.
     * 
     * @param featureName Nombre de la característica a extraer.
     * @return El valor de la característica solicitada.
     * @throws IllegalArgumentException Si el nombre de la característica no es
     *                                  reconocido.
     */
    @Override
    public Comparable<?> datoDeInteres(DummyEntity object, String featureName) {
      switch (featureName) {
        case "name":
          return object.name;
        case "age":
          return object.age;
        default:
          throw new IllegalArgumentException("Unknown feature");
      }
    }
  }

  /** Instancia del dataset bajo prueba. */
  private Dataset<DummyEntity> dataset;

  /** Instancia del featurizer utilizado en las pruebas. */
  private DummyFeaturizer featurizer;

  /**
   * Configuración inicial antes de cada test.
   * Inicializa el featurizer y un dataset vacío.
   */
  @Before
  public void setUp() {
    featurizer = new DummyFeaturizer();
    dataset = new Dataset<>(featurizer);
  }

  /**
   * Verifica que el dataset inicialice correctamente sus estructuras
   * basándose en la configuración del featurizer.
   */
  @Test
  public void shouldInitializeFeaturesBasedOnFeaturizer() {
    assertNotNull(dataset.getFeatures());
    assertEquals(2, dataset.getFeatures().size());
    assertTrue(dataset.getFeatures().containsKey("name"));
    assertTrue(dataset.getFeatures().containsKey("age"));

    assertNotNull(dataset.getData());
    assertTrue(dataset.getData().isEmpty());
  }

  /**
   * Valida que al añadir objetos al dataset, las características se
   * pueblen con los valores correspondientes.
   */
  @Test
  public void shouldAddAllObjectsAndPopulateFeaturesCorrectly() {
    DummyEntity e1 = new DummyEntity("Alice", 30);
    DummyEntity e2 = new DummyEntity("Bob", 25);

    dataset.addAll(new DummyEntity[] { e1, e2 });

    assertEquals(2, dataset.getData().size());

    Feature<String> names = dataset.feature("name");
    Feature<Integer> ages = dataset.feature("age");

    assertEquals("Alice", names.get(0));
    assertEquals("Bob", names.get(1));

    assertEquals(Integer.valueOf(30), ages.get(0));
    assertEquals(Integer.valueOf(25), ages.get(1));
  }

  /**
   * Comprueba la recuperación de una característica específica por su nombre.
   */
  @Test
  public void shouldReturnSpecificFeatureByName() {
    dataset.addAll(new DummyEntity[] { new DummyEntity("Charlie", 40) });

    Feature<String> nameFeature = dataset.feature("name");
    assertNotNull(nameFeature);
    assertEquals("Charlie", nameFeature.get(0));
  }

  /**
   * Verifica que el método de eliminación de duplicados devuelva falso
   * si el dataset está vacío.
   */
  @Test
  public void shouldReturnFalseWhenRemovingDuplicatesFromEmptyDataset() {
    assertFalse(dataset.removeDuplicates());
  }

  /**
   * Verifica que no se realicen cambios si no existen elementos duplicados.
   */
  @Test
  public void shouldReturnFalseWhenRemovingDuplicatesFromDatasetWithoutDuplicates() {
    dataset.addAll(new DummyEntity[] {
        new DummyEntity("Alice", 30),
        new DummyEntity("Bob", 25)
    });

    assertFalse(dataset.removeDuplicates());
    assertEquals(2, dataset.getData().size());
  }

  /**
   * Valida que se eliminen correctamente las filas duplicadas y se actualice
   * el estado interno del dataset.
   */
  @Test
  public void shouldReturnTrueAndRemoveDuplicatesWhenDatasetHasDuplicateRows() {
    DummyEntity e1 = new DummyEntity("Alice", 30);
    DummyEntity e2 = new DummyEntity("Bob", 25);
    DummyEntity e3 = new DummyEntity("Alice", 30);

    dataset.addAll(new DummyEntity[] { e1, e2, e3 });

    assertEquals(3, dataset.getData().size());
    assertTrue(dataset.removeDuplicates());

    // Verifica que las estructuras de datos se hayan reducido
    assertEquals(2, dataset.feature("name").size());
    assertEquals(2, dataset.getData().size());

    Object[] remaining = dataset.getData().toArray();
    assertSame(e1, remaining[0]);
    assertSame(e2, remaining[1]);
  }

  /**
   * Verifica que la representación en cadena del dataset sea válida.
   */
  @Test
  public void shouldReturnMeaningfulToString() {
    assertNotNull(dataset.toString());
  }
}