package tree;

import model.IFeaturizer;
import model.ILabelProvider;
import model.LabeledDataset;
import org.junit.Before;
import org.junit.Test;
import strategy.FeatureSelectionStrategy;

import java.util.*;

import static org.junit.Assert.*;

public class GreedyTreeLearnerTest {

  private GreedyTreeLearner<TestObject, String> learner;
  private List<String> features;

  @Before
  public void setUp() {
    // Estrategia que elige siempre la primera feature disponible
    FeatureSelectionStrategy<TestObject, String> strategy = dataset -> dataset.getFeatures().keySet().iterator().next();

    learner = new GreedyTreeLearner<>(strategy);
    features = Arrays.asList("Color", "Size");
  }

  @Test
    public void shouldReturnEmptyTreeWhenDatasetIsEmpty() {
        LabeledDataset<TestObject, String> dataset = createDataset();
        DecisionTree<TestObject> tree = learner.learn(dataset);
        assertNull(tree.getRaiz());
    }

  @Test
  public void shouldReturnLeafNodeWhenAllLabelsAreSame() {
    LabeledDataset<TestObject, String> dataset = createDataset();
    dataset.addAll(new TestObject[] {
        new TestObject("Red", "Small", "Fruit"),
        new TestObject("Blue", "Large", "Fruit")
    });

    DecisionTree<TestObject> tree = learner.learn(dataset);
    // Cuando todos son iguales, el learner devuelve la etiqueta directamente
    // En DecisionTree esto no crea nodos internos.
    assertTrue(tree.getNodos().isEmpty());
  }

  @Test
  public void shouldBuildTreeWithNodesWhenLabelsDiffer() {
    LabeledDataset<TestObject, String> dataset = createDataset();
    dataset.addAll(new TestObject[] {
        new TestObject("Red", "Small", "TypeA"),
        new TestObject("Blue", "Large", "TypeB")
    });

    DecisionTree<TestObject> tree = learner.learn(dataset);

    assertNotNull(tree.getRaiz());
    assertEquals("root", tree.getRaiz().getName());
    // Se espera que haya creado ramas para dividir los datos
    assertFalse(tree.getRaiz().getRamas().isEmpty());
  }

  // --- Helper Methods & Classes ---

  private LabeledDataset<TestObject, String> createDataset() {
        IFeaturizer<TestObject> featurizer = new IFeaturizer<>() {
            @Override public List<String> featureDeInteres() { return features; }
            @Override public Comparable<?> datoDeInteres(TestObject obj, String f) {
                return f.equals("Color") ? obj.color : obj.size;
            }
        };
        ILabelProvider<TestObject, String> labeler = obj -> obj.label;
        return new LabeledDataset<>(featurizer, labeler);
    }

  private static class TestObject {
    String color, size, label;

    TestObject(String c, String s, String l) {
      this.color = c;
      this.size = s;
      this.label = l;
    }
  }
}