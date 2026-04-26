package labeledDataset;

import decisionTree.*;
import java.util.*;

/**
 * Clase que genera un Árbol de Decisión  de forma automática
 * a partir de un conjunto de datos etiquetados (LabeledDataset) 
 * * @param <T> El tipo de objeto que clasifica el árbol 
 * @param <L> El tipo de la etiqueta 
 */
public class GreedyTreeLearner<T, L> {

  /**
   * Estrategia a utilizar para seleccionar la mejor característica en cada división del árbol.
   */
  private FeatureSelectionStrategy<T, L> estrategia;

  /**
   * Constructor por defecto.
   * Inicializa el generador de árboles con una estrategia de selección de características aleatoria.
   */
  public GreedyTreeLearner() {
      this.estrategia = new RandomStrategy<>();
  }

  /**
   * Constructor parametrizado.
   * Permite asignar una estrategia específica para la selección de características.
   * * @param estrategia La estrategia a utilizar 
   */
  public GreedyTreeLearner(FeatureSelectionStrategy<T, L> estrategia) {
      this.estrategia = estrategia;
  }

  /**
   * Inicia el proceso de aprendizaje y construye el árbol de decisión completo.
   * * @param dataset El conjunto de datos etiquetados de entrenamiento.
   * @return Un objeto DecisionTree configurado y listo para hacer predicciones.
   */
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
   * Método recursivo interno que implementa el algoritmo Greedy
   * Divide el conjunto de datos en subconjuntos y crea los nodos del árbol iterativamente.
   * * @param arbol El árbol de decisión que se está construyendo.
   * @param nombreNodoActual El identificador del nodo que se está procesando en esta llamada.
   * @param datos La lista de objetos que han llegado hasta este nodo.
   * @param featuresDisponibles Lista de características que aún no se han utilizado en esta rama.
   * @param dataset El dataset original, necesario para acceder al Featurizer y al LabelProvider.
   * @return Un String que representa el nombre del nodo destino (o el valor de la etiqueta si es hoja).
   */
  private String construirArbol(DecisionTree<T> arbol, String nombreNodoActual, List<T> datos,
      List<String> featuresDisponibles, LabeledDataset<T, L> dataset) {

    // Comprobación de seguridad
    if (datos == null || datos.isEmpty()) {
      return "";
    }

    //comprobamos si todas las etiquetas son iguales
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

    //elegir la mejor etiqueta segun la interfaz de feature Selection
    String featureElegida = estrategia.chooseBestFeature(datos, featuresDisponibles, dataset);

    // Creamos la nueva lista de características para los hijos, quitando la que acabamos de usar
    List<String> featuresRestantes = new ArrayList<>(featuresDisponibles);
    featuresRestantes.remove(featureElegida); 

    //Dividir los datos en subconjuntos según el valor de la característica elegida
    Map<Object, List<T>> subconjuntos = new HashMap<>();

    for (T dato : datos) {
      // Extraemos el valor de la feature para este dato 
      Object valorDato = dataset.getFeaturizer().datoDeInteres(dato, featureElegida);
      //lo metemos en nuestro mapa
      subconjuntos.putIfAbsent(valorDato, new ArrayList<>());
      subconjuntos.get(valorDato).add(dato);
    }

    //Creamos el nodo actual y enlazar a los sub-árboles
    arbol.node(nombreNodoActual);

    // Por cada subconjunto (rama) que hemos creado:
    for (Map.Entry<Object, List<T>> entrada : subconjuntos.entrySet()) {
      Object valorRama = entrada.getKey();
      List<T> datosRama = entrada.getValue();

      // Inventamos un nombre único para el nodo hijo
      String nombreHijo = nombreNodoActual + "_" + featureElegida + "_" + valorRama.toString();

      //recursion, Construimos el sub arbol para esta rama
      String destinoHijo = construirArbol(arbol, nombreHijo, datosRama, featuresRestantes, dataset);

      // Conectamos el nodo actual con el hijo usando una condición lambda
      arbol.node(nombreNodoActual).withCondition(destinoHijo, obj -> {
        Object valorObj = dataset.getFeaturizer().datoDeInteres(obj, featureElegida);
        return valorRama.equals(valorObj);
      });
    }

    // Devolvemos el nombre de nuestro nodo para que el nodo superior (el padre) nos conecte
    return nombreNodoActual;
  }
}