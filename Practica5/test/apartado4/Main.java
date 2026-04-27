package apartado4;

import tree.DecisionTree;
import tree.GreedyTreeLearner;

import java.util.Map;

import model.LabeledDataset;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DecisionTree<Weather> tree = learnTree();
        
        // Vamos a probar a meter todo el dataset por el árbol que se acaba de generar a ver cómo lo clasifica
        LabeledDataset<Weather, Boolean> dataSet = buildDataSet();
        Map<String, List<Weather>> predicciones = tree.predict(dataSet);
        
        System.out.println("Resultados de la clasificación automática:");
        System.out.println(predicciones);
    }

    public static DecisionTree<Weather> learnTree() {
        LabeledDataset<Weather, Boolean> dataSet = buildDataSet();
        GreedyTreeLearner<Weather, Boolean> learner = new GreedyTreeLearner<>();
        DecisionTree<Weather> tree = learner.learn(dataSet);
        return tree;
    }

    private static LabeledDataset<Weather, Boolean> buildDataSet() {
        // Llenamos el dataset con varios ejemplos para que el árbol pueda aprender
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