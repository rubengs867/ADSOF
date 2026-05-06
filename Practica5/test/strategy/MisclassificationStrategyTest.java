package strategy;

import model.IFeaturizer;
import model.ILabelProvider;
import model.LabeledDataset;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MisclassificationStrategyTest {

  private MisclassificationStrategy<TestItem, String> strategy;
  private MapFeaturizer featurizer;
  private ILabelProvider<TestItem, String> labelProvider;

  @Before
  public void setUp() {
    strategy = new MisclassificationStrategy<>();
    // Definimos características de interés para el featurizer
    List<String> features = Arrays.asList("Color", "Size");
    featurizer = new MapFeaturizer(features);
    labelProvider = TestItem::getLabel;
  }

  @Test
  public void shouldChooseFeatureWithLowestMisclassification() {
    // Arrange
    LabeledDataset<TestItem, String> dataset = new LabeledDataset<>(featurizer, labelProvider);

    // Items donde "Color" clasifica perfectamente pero "Size" tiene errores
    TestItem item1 = new TestItem("Item1", "Fruit");
    TestItem item2 = new TestItem("Item2", "Fruit");
    TestItem item3 = new TestItem("Item3", "Tool");

    // Color: Red -> Fruit (2), Blue -> Tool (1). Errores = 0.[cite: 44]
    featurizer.setValue(item1, "Color", "Red");
    featurizer.setValue(item2, "Color", "Red");
    featurizer.setValue(item3, "Color", "Blue");

    // Size: Small -> Fruit (1) y Tool (1). Mayoría: cualquiera (1). Errores = 2 - 1
    // = 1.[cite: 44]
    featurizer.setValue(item1, "Size", "Small");
    featurizer.setValue(item2, "Size", "Large");
    featurizer.setValue(item3, "Size", "Small");

    dataset.addAll(new TestItem[] { item1, item2, item3 });

    // Act
    String bestFeature = strategy.chooseBestFeature(dataset);

    // Assert
    assertEquals("Color", bestFeature);
  }

  @Test
  public void shouldHandleEmptyDatasetSafely() {
    // Arrange
    LabeledDataset<TestItem, String> dataset = new LabeledDataset<>(featurizer, labelProvider);

    // Act
    String bestFeature = strategy.chooseBestFeature(dataset);

    // Assert - Si hay features pero no datos, devuelve la primera encontrada[cite:
    // 44]
    assertNotNull(bestFeature);
    assertTrue(featurizer.featureDeInteres().contains(bestFeature));
  }

  // --- Clases de Apoyo para Pruebas ---

  private static class TestItem {
    private final String id;
    private final String label;

    public TestItem(String id, String label) {
      this.id = id;
      this.label = label;
    }

    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      return id;
    }
  }

  private static class MapFeaturizer implements IFeaturizer<TestItem> {
    private final List<String> features;
    private final Map<TestItem, Map<String, Comparable<?>>> values = new HashMap<>();

    public MapFeaturizer(List<String> features) {
      this.features = features;
    }

    public void setValue(TestItem item, String feature, Comparable<?> value) {
      values.computeIfAbsent(item, k -> new HashMap<>()).put(feature, value);
    }

    @Override
    public List<String> featureDeInteres() {
      return features;
    }

    @Override
    public Comparable<?> datoDeInteres(TestItem object, String featureName) {
      return values.getOrDefault(object, Collections.emptyMap()).get(featureName);
    }
  }
}