package strategy;

import model.IFeaturizer;
import model.ILabelProvider;
import model.LabeledDataset;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RandomStrategyTest {

  private RandomStrategy<String, String> strategy;
  private LabeledDataset<String, String> dataset;

  @Before
  public void setUp() {
    strategy = new RandomStrategy<>();

    // Featurizer simple con 3 características
    IFeaturizer<String> featurizer = new IFeaturizer<String>() {
      @Override
      public List<String> featureDeInteres() {
        return Arrays.asList("FeatureA", "FeatureB", "FeatureC");
      }

      @Override
      public Comparable<?> datoDeInteres(String object, String featureName) {
        return object.length();
      }
    };

    ILabelProvider<String, String> labelProvider = obj -> "Label";
    dataset = new LabeledDataset<>(featurizer, labelProvider);
  }

  @Test
  public void shouldReturnOneOfTheAvailableFeatures() {
    // Act
    String chosen = strategy.chooseBestFeature(dataset);

    // Assert
    assertNotNull(chosen);
    // Debe ser una de las llaves del mapa de características del dataset[cite: 45]
    assertTrue(dataset.getFeatures().containsKey(chosen));
  }

  @Test
  public void shouldReturnOnlyFeatureWhenOnlyOneIsAvailable() {
    // Arrange
    IFeaturizer<String> singleFeaturizer = new IFeaturizer<String>() {
      @Override
      public List<String> featureDeInteres() {
        return Collections.singletonList("Single");
      }

      @Override
      public Comparable<?> datoDeInteres(String o, String f) {
        return 0;
      }
    };
    LabeledDataset<String, String> singleDataset = new LabeledDataset<>(singleFeaturizer, obj -> "L");

    // Act
    String chosen = strategy.chooseBestFeature(singleDataset);

    // Assert
    assertEquals("Single", chosen);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void shouldThrowExceptionIfNoFeaturesAvailable() {
    // Arrange
    IFeaturizer<String> emptyFeaturizer = new IFeaturizer<String>() {
      @Override
      public List<String> featureDeInteres() {
        return Collections.emptyList();
      }

      @Override
      public Comparable<?> datoDeInteres(String o, String f) {
        return 0;
      }
    };
    LabeledDataset<String, String> emptyDataset = new LabeledDataset<>(emptyFeaturizer, obj -> "L");

    // Act
    strategy.chooseBestFeature(emptyDataset);
  }
}