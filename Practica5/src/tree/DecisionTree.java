package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import model.Dataset;

/**
 * Representa un árbol de decisión compuesto por nodos y ramas.
 *
 * @param <T> Tipo de dato evaluado por el árbol.
 */
public class DecisionTree<T> {

  /**
   * Nodo raíz del árbol.
   */
  private Node<T> raiz = null;

  /**
   * Índice de nodos por nombre.
   */
  private Map<String, Node<T>> nodos = new HashMap<>();

  /**
   * Nodos que ya reciben una rama de entrada.
   */
  private Set<Node<T>> nodosVisitados = new HashSet<>();

  /**
   * Obtiene un nodo existente o lo crea si no existe.
   *
   * @param name Nombre del nodo.
   *
   * @return Nodo solicitado.
   */
  public Node<T> node(String name) {

    if (nodos.containsKey(name)) {
      return nodos.get(name);
    }

    Node<T> nuevoNodo = new Node<>(name, this);
    nodos.put(name, nuevoNodo);

    if (this.raiz == null) {
      this.raiz = nuevoNodo;
      nodosVisitados.add(raiz);
    }

    return nuevoNodo;
  }

  /**
   * Evalúa múltiples datos individuales.
   *
   * @param datos Datos a clasificar.
   *
   * @return Resultado agrupado por etiqueta.
   */
  @SafeVarargs
  public final Map<String, List<T>> predict(T... datos) {

    Map<String, List<T>> resultados = new LinkedHashMap<>();

    for (T dato : datos) {
      String etiqueta = evaluar(dato);

      resultados.putIfAbsent(etiqueta, new ArrayList<>());
      resultados.get(etiqueta).add(dato);
    }

    return resultados;
  }

  /**
   * Evalúa todos los elementos de un dataset.
   *
   * @param dataset Dataset de entrada.
   *
   * @return Resultado agrupado por etiqueta.
   */
  public Map<String, List<T>> predict(Dataset<T> dataset) {

    Map<String, List<T>> resultados = new LinkedHashMap<>();

    for (T dato : dataset.getData()) {
      String etiqueta = evaluar(dato);

      resultados.putIfAbsent(etiqueta, new ArrayList<>());
      resultados.get(etiqueta).add(dato);
    }

    return resultados;
  }

  /**
   * Recorre el árbol hasta obtener una etiqueta final.
   *
   * @param dato Dato evaluado.
   *
   * @return Etiqueta resultante.
   */
  private String evaluar(T dato) {

    // El recorrido comienza siempre en la raíz del árbol.
    Node<T> nodoActual = this.raiz;

    // Se itera hasta encontrar una salida terminal.
    while (true) {

      // Evalúa el nodo actual y obtiene el siguiente destino.
      String next = nodoActual.evaluate(dato);

      /*
       * Si no existe siguiente destino:
       * - Puede tratarse de un nodo hoja.
       * - O de un nodo intermedio sin coincidencias.
       * En ambos casos se devuelve el nombre actual.
       */
      if (next == null) {

        // Nodo hoja sin ramas salientes.
        if (nodoActual.getRamas().isEmpty()) {
          return nodoActual.getName();
        }

        // Nodo intermedio sin coincidencia de condiciones.
        return nodoActual.getName();
      }

      /*
       * Si el destino existe como nodo registrado,
       * continuamos descendiendo por el árbol.
       */
      if (this.nodos.containsKey(next)) {
        nodoActual = this.nodos.get(next);
      } else {

        /*
         * Si no existe como nodo interno, se considera
         * una etiqueta final directa.
         */
        return next;
      }
    }
  }

  /**
   * Obtiene el predicado lógico necesario para alcanzar una etiqueta.
   *
   * @param etiquetaDestino Etiqueta objetivo.
   *
   * @return Predicado asociado o {@code null} si no existe camino.
   */
  public Predicate<T> getPredicate(String etiquetaDestino) {

    if (this.raiz != null && this.raiz.getName().equals(etiquetaDestino)) {
      return x -> true;
    }

    return buscarPredicado(this.raiz, etiquetaDestino);
  }

  /**
   * Busca recursivamente el predicado lógico necesario para llegar
   * desde un nodo dado hasta una etiqueta destino.
   *
   * <p>
   * El resultado representa la combinación de condiciones que
   * deben cumplirse para alcanzar dicho camino dentro del árbol.
   * </p>
   *
   * @param nodo            Nodo desde el que comienza la búsqueda.
   * @param etiquetaDestino Etiqueta objetivo.
   *
   * @return Predicado acumulado o {@code null} si no existe camino.
   */
  private Predicate<T> buscarPredicado(
      Node<T> nodo,
      String etiquetaDestino) {

    // Caso base: nodo inexistente.
    if (nodo == null) {
      return null;
    }

    /*
     * Acumulador lógico que representa que todas las ramas
     * anteriores han fallado.
     *
     * Inicialmente es true porque aún no se ha evaluado ninguna.
     */
    Predicate<T> hanFalladoAnteriores = x -> true;

    // Recorre cada rama condicional del nodo actual.
    for (Rama<T> rama : nodo.getRamas()) {

      /*
       * Para entrar en esta rama deben cumplirse:
       * - El fallo de todas las anteriores.
       * - La condición actual.
       */
      Predicate<T> formulaActual = hanFalladoAnteriores.and(rama.getCondicion());

      // Si esta rama llega directamente al destino, se devuelve.
      if (rama.getNodoDestino().getName().equals(etiquetaDestino)) {
        return formulaActual;
      }

      /*
       * Si el destino de la rama es un nodo interno,
       * continuamos buscando de forma recursiva.
       */
      if (this.nodos.containsKey(rama.getNodoDestino().getName())) {

        Node<T> nodoHijo = this.nodos.get(rama.getNodoDestino().getName());

        Predicate<T> formulaHijo = buscarPredicado(nodoHijo, etiquetaDestino);

        /*
         * Si el destino aparece en niveles inferiores,
         * se concatena la condición actual con la del hijo.
         */
        if (formulaHijo != null) {
          return formulaActual.and(formulaHijo);
        }
      }

      /*
       * Si esta rama no sirve, para evaluar la siguiente
       * se añade la negación de la condición actual.
       */
      hanFalladoAnteriores = hanFalladoAnteriores.and(
          rama.getCondicion().negate());
    }

    /*
     * Si ninguna rama condicional sirve, se analiza
     * el camino por defecto (otherwise).
     */
    Node<T> nodoDefecto = nodo.getNodoPorDefecto();

    String destinoDefecto = (nodoDefecto == null)
        ? null
        : nodoDefecto.getName();

    if (destinoDefecto != null) {

      /*
       * Si el camino por defecto llega directamente
       * al destino, basta con que fallen las anteriores.
       */
      if (destinoDefecto.equals(etiquetaDestino)) {
        return hanFalladoAnteriores;
      }

      /*
       * Si el camino por defecto lleva a otro nodo interno,
       * continúa la búsqueda recursiva.
       */
      if (this.nodos.containsKey(destinoDefecto)) {

        Node<T> nodoHijo = this.nodos.get(destinoDefecto);

        Predicate<T> formulaHijo = buscarPredicado(nodoHijo, etiquetaDestino);

        if (formulaHijo != null) {
          return hanFalladoAnteriores.and(formulaHijo);
        }
      }
    }

    // No existe camino hacia la etiqueta buscada.
    return null;
  }

  /**
   * Devuelve la raíz del árbol.
   *
   * @return Nodo raíz.
   */
  public Node<T> getRaiz() {
    return raiz;
  }

  /**
   * Devuelve el índice de nodos.
   *
   * @return Mapa de nodos.
   */
  public Map<String, Node<T>> getNodos() {
    return nodos;
  }

  /**
   * Devuelve los nodos enlazados desde una rama.
   *
   * @return Conjunto de nodos visitados.
   */
  public Set<Node<T>> getNodosVisitados() {
    return nodosVisitados;
  }
}