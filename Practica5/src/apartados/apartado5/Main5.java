package apartados.apartado5;

import java.util.List;
import java.util.Map;

import apartados.apartado4.ShouldIPlayTennisToday;
import apartados.apartado4.Temperature;
import apartados.apartado4.Weather;
import apartados.apartado4.WeatherCondition;
import apartados.apartado4.WeatherFeaturizer;
import model.LabeledDataset;
import strategy.MisclassificationStrategy;
import strategy.RandomStrategy;
import tree.DecisionTree;
import tree.GreedyTreeLearner;

/**
 * Clase principal de pruebas para comparar distintas estrategias
 * de aprendizaje en la construcción de árboles de decisión.
 */
public class Main5 {

  /**
   * Punto de entrada de ejecución.
   *
   * @param args argumentos de línea de comandos
   */
  public static void main(String[] args) {
    System.out.println("PRUEBA APARTADO 5: ");

    LabeledDataset<Weather, Boolean> dataSet = buildDataSet();

    // usamos la Estrategia Aleatoria
    System.out.println("\n1. Entrenando árbol con RandomStrategy...");
    GreedyTreeLearner<Weather, Boolean> learnerAleatorio = new GreedyTreeLearner<>(new RandomStrategy<>());
    DecisionTree<Weather> arbolAleatorio = learnerAleatorio.learn(dataSet);

    Map<String, List<Weather>> prediccionesAleatorias = arbolAleatorio.predict(dataSet);
    System.out.println("Resultados (Aleatorio): " + prediccionesAleatorias);

    // estrategia misclassification
    System.out.println("\n2. Entrenando árbol con MisclassificationStrategy...");
    GreedyTreeLearner<Weather, Boolean> learnerInteligente = new GreedyTreeLearner<>(new MisclassificationStrategy<>());
    DecisionTree<Weather> arbolInteligente = learnerInteligente.learn(dataSet);

    Map<String, List<Weather>> prediccionesInteligentes = arbolInteligente.predict(dataSet);
    System.out.println("Resultados (Inteligente): " + prediccionesInteligentes);
  }

  /**
   * Construye el conjunto de datos de ejemplo utilizado
   * para entrenar los árboles.
   *
   * @return dataset etiquetado con condiciones meteorológicas
   */
  private static LabeledDataset<Weather, Boolean> buildDataSet() {
    Weather[] conditions = {
        new Weather(WeatherCondition.RAINY, Temperature.COLD),
        new Weather(WeatherCondition.RAINY, Temperature.HOT),
        new Weather(WeatherCondition.SUNNY, Temperature.HOT),
        new Weather(WeatherCondition.SUNNY, Temperature.COLD),
        new Weather(WeatherCondition.OVERCAST, Temperature.MILD)
    };

    LabeledDataset<Weather, Boolean> ds = new LabeledDataset<>(
        new WeatherFeaturizer(),
        new ShouldIPlayTennisToday());

    ds.addAll(conditions);
    return ds;
  }
}