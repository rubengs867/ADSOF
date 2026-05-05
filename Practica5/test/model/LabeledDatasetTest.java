// LabeledDatasetTest.java
package model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LabeledDatasetTest {

  static class Product {
    String id;
    double price;

    Product(String id, double price) {
      this.id = id;
      this.price = price;
    }
  }

  static class ProductFeaturizer implements IFeaturizer<Product> {

    @Override
    public List<String> featureDeInteres() {
      return Arrays.asList("id", "price");
    }

    @Override
    public Comparable<?> datoDeInteres(Product object, String featureName) {
      switch (featureName) {
        case "id":
          return object.id;
        case "price":
          return object.price;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  static class ProductLabelProvider implements ILabelProvider<Product, String> {

    @Override
    public String getLabel(Product object) {
      return object.price > 100.0 ? "EXPENSIVE" : "CHEAP";
    }
  }

  private LabeledDataset<Product, String> labeledDataset;
  private ProductFeaturizer featurizer;
  private ProductLabelProvider labelProvider;

  @Before
  public void setUp() {
    featurizer = new ProductFeaturizer();
    labelProvider = new ProductLabelProvider();
    labeledDataset = new LabeledDataset<>(featurizer, labelProvider);
  }

  @Test
  public void shouldInitializeWithFeaturizerAndLabelProvider() {
    assertSame(featurizer, labeledDataset.getFeaturizer());
    assertSame(labelProvider, labeledDataset.getLabelProvider());
  }

  @Test
  public void shouldReturnLabelForGivenObject() {
    assertEquals("CHEAP", labeledDataset.getLabel(new Product("P1", 50.0)));
    assertEquals("EXPENSIVE", labeledDataset.getLabel(new Product("P2", 150.0)));
  }

  @Test
  public void shouldCreateSubsetWithSelectedFeaturesAndData() {
    Product p1 = new Product("P1", 50.0);
    Product p2 = new Product("P2", 200.0);

    labeledDataset.addAll(new Product[] { p1, p2 });

    LabeledDataset<Product, String> subset = labeledDataset.subset(Arrays.asList(p2), Arrays.asList("price"));

    assertNotNull(subset);
    assertNotSame(labeledDataset, subset);
    assertSame(labelProvider, subset.getLabelProvider());

    assertEquals(1, subset.getFeatures().size());
    assertTrue(subset.getFeatures().containsKey("price"));
    assertFalse(subset.getFeatures().containsKey("id"));

    assertEquals(1, subset.getData().size());
    assertTrue(subset.getData().contains(p2));

    Feature<Double> priceFeature = subset.feature("price");
    assertEquals(1, priceFeature.size());
    assertEquals(Double.valueOf(200.0), priceFeature.get(0));
  }

  @Test
  public void shouldHandleEmptySubsetSelection() {
    Product p1 = new Product("P1", 50.0);

    LabeledDataset<Product, String> subset = labeledDataset.subset(Arrays.asList(p1), Collections.<String>emptyList());

    assertTrue(subset.getFeatures().isEmpty());
    assertEquals(1, subset.getData().size());
  }
}