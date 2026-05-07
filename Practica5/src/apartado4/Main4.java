package apartado4;

import tree.DecisionTree;
import tree.GreedyTreeLearner;

import java.util.Map;
import java.util.List;

import model.LabeledDataset;

/**
 * Clase principal de ejecución para el aprendizaje automático
 * de un árbol de decisión aplicado a condiciones meteorológicas.
 */
public class Main4 {

  /**
   * Punto de entrada de la aplicación.
   *
   * @param args argumentos de línea de comandos
   */
  public static void main(String[] args) {
    DecisionTree<Weather> tree = learnTree();

    LabeledDataset<Weather, Boolean> dataSet = buildDataSet();
    Map<String, List<Weather>> predicciones = tree.predict(dataSet);

    System.out.println("Resultados de la clasificación automática:");
    System.out.println(predicciones);
  }

  /**
   * Entrena un árbol de decisión a partir del conjunto de datos disponible.
   *
   * @return árbol de decisión generado
   */
  public static DecisionTree<Weather> learnTree() {
    LabeledDataset<Weather, Boolean> dataSet = buildDataSet();
    GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();
    DecisionTree<Weather> tree = learner.learn(dataSet);
    return tree;
  }

  /**
   * Construye el conjunto de datos etiquetado usado para entrenamiento.
   *
   * @return dataset etiquetado de condiciones meteorológicas
   */
  private static LabeledDataset<Weather, Boolean> buildDataSet() {
    Weather[] conditions = {
        new Weather(WeatherCondition.RAINY, Temperature.COLD),
        new Weather(WeatherCondition.RAINY, Temperature.HOT),
        new Weather(WeatherCondition.SUNNY, Temperature.HOT),
        new Weather(WeatherCondition.SUNNY, Temperature.COLD),
        new Weather(WeatherCondition.OVERCAST, Temperature.MILD)
    };

    LabeledDataset<Weather, Boolean> ds = new LabeledDataset<>(new WeatherFeaturizer(), new ShouldIPlayTennisToday());

    ds.addAll(conditions);
    return ds;
  }
}