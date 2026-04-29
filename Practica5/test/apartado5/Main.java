package apartado5; 

import java.util.List;
import java.util.Map;

import apartado4.ShouldIPlayTennisToday;
import apartado4.Temperature;
// Importamos las clases del clima que hicimos en el apartado 4
import apartado4.Weather;
import apartado4.WeatherCondition;
import apartado4.WeatherFeaturizer;
import model.LabeledDataset;
import strategy.MisclassificationStrategy;
import strategy.RandomStrategy;
import tree.DecisionTree;
import tree.GreedyTreeLearner;

public class Main {

    public static void main(String[] args) {
        System.out.println("PRUEBA APARTADO 5: ");
        
        LabeledDataset<Weather, Boolean> dataSet = buildDataSet();

        //usamos la Estrategia Aleatoria
        System.out.println("\n1. Entrenando árbol con RandomStrategy...");
        GreedyTreeLearner<Weather, Boolean> learnerAleatorio = new GreedyTreeLearner<>(new RandomStrategy<>());
        DecisionTree<Weather> arbolAleatorio = learnerAleatorio.learn(dataSet);
        
        Map<String, List<Weather>> prediccionesAleatorias = arbolAleatorio.predict(dataSet);
        System.out.println("Resultados (Aleatorio): " + prediccionesAleatorias);

        //estrategia misclassification
        System.out.println("\n2. Entrenando árbol con MisclassificationStrategy...");
        GreedyTreeLearner<Weather, Boolean> learnerInteligente = new GreedyTreeLearner<>(new MisclassificationStrategy<>());
        DecisionTree<Weather> arbolInteligente = learnerInteligente.learn(dataSet);
        
        Map<String, List<Weather>> prediccionesInteligentes = arbolInteligente.predict(dataSet);
        System.out.println("Resultados (Inteligente): " + prediccionesInteligentes);
    }

    // Reciclamos la creación de datos del apartado anterior
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