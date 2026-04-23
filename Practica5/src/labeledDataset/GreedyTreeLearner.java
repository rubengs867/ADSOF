package labeledDataset;

import decisionTree.*;
import java.util.*;

public class GreedyTreeLearner<T, L> { 
  
  public GreedyTreeLearner() {}

  public DecisionTree<T> learn(LabeledDataset<T, L> dataset) {
    DecisionTree<T> arbol = new DecisionTree<>();
    
    // Obtenemos los datos completos para empezar
    List<T> datos = dataset.getObjetos();
    
    // Obtenemos la lista de características disponibles 
    List<String> featuresDisponibles = new ArrayList<>(dataset.getFeaturizer().featureDeInteres());
    
    // Llamamos a nuestro método recursivo para que construya la raíz y cuelgue todo de ahí.
    // Le pasamos un nombre de nodo inventado como "root" para empezar.
    construirArbol(arbol, "root", datos, featuresDisponibles, dataset);
    
    return arbol;
  }

  /**
   * Método recursivo que implementa el algoritmo Greedy.
   * Devuelve un String que es el nombre del nodo destino 
   */
  private String construirArbol(DecisionTree<T> arbol, String nombreNodoActual, List<T> datos, List<String> featuresDisponibles, LabeledDataset<T, L> dataset) {
    


    return nombreNodoActual; 
  }

}