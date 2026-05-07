package tree;

import model.IFeaturizer;
import model.ILabelProvider;
import model.LabeledDataset;
import org.junit.Before;
import org.junit.Test;
import strategy.FeatureSelectionStrategy;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Pruebas para el algoritmo de aprendizaje Greedy para árboles de decisión.
 */
public class GreedyTreeLearnerTest {

  /** Atributo de apoyo para cargar los datos y usarlo en los test */
  private GreedyTreeLearner<TestObject, String> learner;

  /** Lista de features */
  private List<String> features;

  /**
   * Configuración del alumno con una estrategia de selección simple (primera
   * disponible).
   */
  @Before
  public void setUp() {
    FeatureSelectionStrategy<TestObject, String> strategy = dataset -> dataset.getFeatures().keySet().iterator().next();

    learner = new GreedyTreeLearner<>(strategy);
    features = Arrays.asList("Color", "Size");
  }

  /**
   * Verifica que un dataset vacío no genere estructura de árbol.
   */
  @Test
  public void shouldReturnEmptyTreeWhenDatasetIsEmpty() {
    LabeledDataset<TestObject, String> dataset = createDataset();
    DecisionTree<TestObject> tree = learner.learn(dataset);
    assertNull(tree.getRaiz());
  }

  /**
   * Valida que si todos los datos tienen la misma etiqueta, no se creen
   * divisiones.
   */
  @Test
  public void shouldReturnLeafNodeWhenAllLabelsAreSame() {
    LabeledDataset<TestObject, String> dataset = createDataset();
    dataset.addAll(new TestObject[] {
        new TestObject("Red", "Small", "Fruit"),
        new TestObject("Blue", "Large", "Fruit")
    });

    DecisionTree<TestObject> tree = learner.learn(dataset);
    // Sin divisiones internas
    assertTrue(tree.getNodos().isEmpty());
  }

  /**
   * Verifica que se cree una estructura jerárquica cuando las etiquetas difieren.
   */
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
    assertFalse(tree.getRaiz().getRamas().isEmpty());
  }

  /**
   * Crea un dataset de prueba configurado con featurizer y labeler.
   * 
   * @return Instancia de LabeledDataset poblada.
   */
  private LabeledDataset<TestObject, String> createDataset() {
    IFeaturizer<TestObject> featurizer = new IFeaturizer<>() {
      @Override
      public List<String> featureDeInteres() {
        return features;
      }

      @Override
      public Comparable<?> datoDeInteres(TestObject obj, String f) {
        return f.equals("Color") ? obj.color : obj.size;
      }
    };
    ILabelProvider<TestObject, String> labeler = obj -> obj.label;
    return new LabeledDataset<>(featurizer, labeler);
  }

  /**
   * Clase interna para objetos de entrenamiento de prueba.
   */
  private static class TestObject {
    String color, size, label;

    TestObject(String c, String s, String l) {
      this.color = c;
      this.size = s;
      this.label = l;
    }
  }
}