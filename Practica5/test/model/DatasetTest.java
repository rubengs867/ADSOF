// DatasetTest.java
package model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DatasetTest {

  static class DummyEntity {
    String name;
    int age;

    DummyEntity(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  static class DummyFeaturizer implements IFeaturizer<DummyEntity> {

    @Override
    public List<String> featureDeInteres() {
      return Arrays.asList("name", "age");
    }

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

  private Dataset<DummyEntity> dataset;
  private DummyFeaturizer featurizer;

  @Before
  public void setUp() {
    featurizer = new DummyFeaturizer();
    dataset = new Dataset<>(featurizer);
  }

  @Test
  public void shouldInitializeFeaturesBasedOnFeaturizer() {
    assertNotNull(dataset.getFeatures());
    assertEquals(2, dataset.getFeatures().size());
    assertTrue(dataset.getFeatures().containsKey("name"));
    assertTrue(dataset.getFeatures().containsKey("age"));

    assertNotNull(dataset.getData());
    assertTrue(dataset.getData().isEmpty());
  }

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

  @Test
  public void shouldReturnSpecificFeatureByName() {
    dataset.addAll(new DummyEntity[] { new DummyEntity("Charlie", 40) });

    Feature<String> nameFeature = dataset.feature("name");
    assertNotNull(nameFeature);
    assertEquals("Charlie", nameFeature.get(0));
  }

  @Test
  public void shouldReturnFalseWhenRemovingDuplicatesFromEmptyDataset() {
    assertFalse(dataset.removeDuplicates());
  }

  @Test
  public void shouldReturnFalseWhenRemovingDuplicatesFromDatasetWithoutDuplicates() {
    dataset.addAll(new DummyEntity[] {
        new DummyEntity("Alice", 30),
        new DummyEntity("Bob", 25)
    });

    assertFalse(dataset.removeDuplicates());
    assertEquals(2, dataset.getData().size());
  }

  @Test
  public void shouldReturnTrueAndRemoveDuplicatesWhenDatasetHasDuplicateRows() {
    DummyEntity e1 = new DummyEntity("Alice", 30);
    DummyEntity e2 = new DummyEntity("Bob", 25);
    DummyEntity e3 = new DummyEntity("Alice", 30);

    dataset.addAll(new DummyEntity[] { e1, e2, e3 });

    assertEquals(3, dataset.getData().size());
    assertTrue(dataset.removeDuplicates());

    assertEquals(2, dataset.feature("name").size());
    assertEquals(2, dataset.getData().size());

    Object[] remaining = dataset.getData().toArray();
    assertSame(e1, remaining[0]);
    assertSame(e2, remaining[1]);
  }

  @Test
  public void shouldReturnMeaningfulToString() {
    assertNotNull(dataset.toString());
  }
}