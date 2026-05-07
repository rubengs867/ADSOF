package strategy;

import model.IFeaturizer;
import model.ILabelProvider;
import model.LabeledDataset;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para la estrategia de selección aleatoria de
 * características.
 */
public class RandomStrategyTest {

  /** Estrategia a probar */
  private RandomStrategy<String, String> strategy;

  /** Dataset etiquetado de apoyo */
  private LabeledDataset<String, String> dataset;

  /**
   * Configuración inicial con un featurizer de tres opciones.
   */
  @Before
  public void setUp() {
    strategy = new RandomStrategy<>();

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

  /**
   * Verifica que la característica elegida pertenezca al conjunto disponible.
   */
  @Test
  public void shouldReturnOneOfTheAvailableFeatures() {
    String chosen = strategy.chooseBestFeature(dataset);

    assertNotNull(chosen);
    assertTrue(dataset.getFeatures().containsKey(chosen));
  }

  /**
   * Valida el caso determinista donde solo existe una opción.
   */
  @Test
  public void shouldReturnOnlyFeatureWhenOnlyOneIsAvailable() {
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

    String chosen = strategy.chooseBestFeature(singleDataset);

    assertEquals("Single", chosen);
  }

  /**
   * Verifica que se lance una excepción si no hay características disponibles
   * para elegir.
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void shouldThrowExceptionIfNoFeaturesAvailable() {
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

    strategy.chooseBestFeature(emptyDataset);
  }
}