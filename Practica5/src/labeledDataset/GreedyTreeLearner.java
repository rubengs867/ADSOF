package labeledDataset;

import decisionTree.*;
import java.util.*;

public class GreedyTreeLearner<T, L> {

  public GreedyTreeLearner() {
  }

  public DecisionTree<T> learn(LabeledDataset<T, L> dataset) {
    DecisionTree<T> arbol = new DecisionTree<>();

    // Obtenemos los datos completos para empezar
    List<T> datos = dataset.getObjetos();

    // Obtenemos la lista de características disponibles
    List<String> featuresDisponibles = new ArrayList<>(dataset.getFeaturizer().featureDeInteres());

    // Llamamos a nuestro método recursivo para que construya la raíz y cuelgue todo
    // de ahí.
    // Le pasamos un nombre de nodo inventado como "root" para empezar.
    construirArbol(arbol, "root", datos, featuresDisponibles, dataset);

    return arbol;
  }

  /**
   * Método recursivo que implementa el algoritmo Greedy.
   * Devuelve un String que es el nombre del nodo destino
   */
  private String construirArbol(DecisionTree<T> arbol, String nombreNodoActual, List<T> datos,
      List<String> featuresDisponibles, LabeledDataset<T, L> dataset) {


    if (datos == null || datos.isEmpty()) {
      return "";
    }

    L primeraEtiqueta = dataset.getLabel(datos.get(0));
    boolean todosIguales = true;

    for (T dato : datos) {
      L etiquetaActual = dataset.getLabel(dato);
      if (!primeraEtiqueta.equals(etiquetaActual)) {
        todosIguales = false;
        break; // Hay etiquetas mezcladas
      }
    }

    // Caso base: Si todos son iguales, hemos llegado a una hoja del árbol
    if (todosIguales) {
      return primeraEtiqueta.toString();
    }

    if (featuresDisponibles.isEmpty()) {
      return primeraEtiqueta.toString();
    }

    List<String> featuresRestantes = new ArrayList<>(featuresDisponibles);
    Collections.shuffle(featuresRestantes); // Mezclamos la lista para que sea aleatorio
    String featureElegida = featuresRestantes.remove(0); // Extraemos la primera feature y luego la borramos

    Map<Object, List<T>> subconjuntos = new HashMap<>();

    for (T dato : datos) {
      // Extraemos el valor de la feature para este dato (ej. true, false, 45,
      // "soleado"...)
      Object valorDato = dataset.getFeaturizer().datoDeInteres(dato, featureElegida);

      subconjuntos.putIfAbsent(valorDato, new ArrayList<>());
      subconjuntos.get(valorDato).add(dato);
    }


    arbol.node(nombreNodoActual);

    // Por cada subconjunto (rama) que hemos creado:
    for (Map.Entry<Object, List<T>> entrada : subconjuntos.entrySet()) {
      Object valorRama = entrada.getKey();
      List<T> datosRama = entrada.getValue();

      // Inventamos un nombre único para el nodo hijo
      String nombreHijo = nombreNodoActual + "_" + featureElegida + "_" + valorRama.toString();

      // LLAMADA RECURSIVA: Construimos el sub-árbol para esta rama
      String destinoHijo = construirArbol(arbol, nombreHijo, datosRama, featuresRestantes, dataset);

      // Conectamos el nodo actual con el hijo usando una condición lambda
      arbol.node(nombreNodoActual).withCondition(destinoHijo, obj -> {
        Object valorObj = dataset.getFeaturizer().datoDeInteres(obj, featureElegida);
        return valorRama.equals(valorObj);
      });
    }

    // Devolvemos el nombre de nuestro nodo para que el nodo superior (el padre) nos
    // conecte
    return nombreNodoActual;
  }

}