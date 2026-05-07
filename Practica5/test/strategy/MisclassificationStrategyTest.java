package strategy;

import model.IFeaturizer;
import model.ILabelProvider;
import model.LabeledDataset;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Pruebas de integración para la estrategia de selección de características
 * basada en la métrica de clasificación errónea.
 */
public class MisclassificationStrategyTest {

  /** Estrategia a probar */
  private MisclassificationStrategy<TestItem, String> strategy;

  /** Featurizer de ayuda */
  private MapFeaturizer featurizer;

  /** LabelProvides de ayuda */
  private ILabelProvider<TestItem, String> labelProvider;

  /**
   * Prepara la estrategia y los mocks de featurizer y label provider.
   */
  @Before
  public void setUp() {
    strategy = new MisclassificationStrategy<>();
    List<String> features = Arrays.asList("Color", "Size");
    featurizer = new MapFeaturizer(features);
    labelProvider = TestItem::getLabel;
  }

  /**
   * Prueba que la estrategia seleccione la característica con menos errores de
   * clasificación.
   */
  @Test
  public void shouldChooseFeatureWithLowestMisclassification() {
    LabeledDataset<TestItem, String> dataset = new LabeledDataset<>(featurizer, labelProvider);

    TestItem item1 = new TestItem("Item1", "Fruit");
    TestItem item2 = new TestItem("Item2", "Fruit");
    TestItem item3 = new TestItem("Item3", "Tool");

    /*
     * * Color: Red -> Fruit (2), Blue -> Tool (1).
     * Clasificación pura (error 0).
     */
    featurizer.setValue(item1, "Color", "Red");
    featurizer.setValue(item2, "Color", "Red");
    featurizer.setValue(item3, "Color", "Blue");

    /*
     * * Size: Small -> Fruit (1) y Tool (1).
     * Clasificación impura (error > 0).
     */
    featurizer.setValue(item1, "Size", "Small");
    featurizer.setValue(item2, "Size", "Large");
    featurizer.setValue(item3, "Size", "Small");

    dataset.addAll(new TestItem[] { item1, item2, item3 });

    String bestFeature = strategy.chooseBestFeature(dataset);

    assertEquals("Color", bestFeature);
  }

  /**
   * Verifica que se devuelva una característica válida incluso si el dataset no
   * tiene datos.
   */
  @Test
  public void shouldHandleEmptyDatasetSafely() {
    LabeledDataset<TestItem, String> dataset = new LabeledDataset<>(featurizer, labelProvider);

    String bestFeature = strategy.chooseBestFeature(dataset);

    assertNotNull(bestFeature);
    assertTrue(featurizer.featureDeInteres().contains(bestFeature));
  }

  /**
   * Clase interna para representar ítems de prueba con etiquetas.
   */
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

  /**
   * Implementación de IFeaturizer basada en un mapa para control granular en
   * tests.
   */
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