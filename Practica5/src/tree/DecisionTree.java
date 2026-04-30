package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import model.Dataset;

public class DecisionTree<T> {
  private Node<T> raiz = null;
  // cada TreeNode guarda el valor generico
  private Map<String, Node<T>> nodos;

  // Constructor
  public DecisionTree() {
    this.nodos = new HashMap<>();
  }

  public Node<T> node(String name) {
    if (nodos.containsKey(name)) {
      return nodos.get(name);
    }
    // no existe ese nuevo nodo, entonces hay que crearlo
    Node<T> nuevoNodo = new Node<>(name, this);
    nodos.put(name, nuevoNodo);

    if (this.raiz == null) {
      this.raiz = nuevoNodo;
    }
    return nuevoNodo;
  }

  @SafeVarargs
  public final Map<String, List<T>> predict(T... datos) {
    Map<String, List<T>> resultados = new LinkedHashMap<>();

    // Recorremos todos los elementos
    for (T dato : datos) {
      String etiqueta = predicate(dato);

      resultados.putIfAbsent(etiqueta, new ArrayList<>());

      resultados.get(etiqueta).add(dato);
    }

    return resultados;
  }

  public Map<String, List<T>> predict(Dataset<T> dataset) {

    // Lo único que hacemos es preparar un mapa vacío
    Map<String, List<T>> resultados = new LinkedHashMap<>();

    for (T dato : dataset.getData()) {
      String etiqueta = predicate(dato);
      resultados.putIfAbsent(etiqueta, new ArrayList<>());
      resultados.get(etiqueta).add(dato);
    }

    return resultados;
  }

  private String predicate(T dato) {
    Node<T> nodo_actual = this.raiz;

    while (true) {
      // evaluamos la condcion actual
      String next = nodo_actual.evaluate(dato);
      // ahora mismo nos hemos parado
      if (next == null) {
        // este caso implica que hemos llegado hasta abajo
        if (nodo_actual.getRamas().isEmpty()) {
          return nodo_actual.getName();
        } else {
          // nos hemos quedado en un nodo intermedio
          System.err.println("intermedio");
          return nodo_actual.getName();
        }

      }

      // actualizamos el nodo
      if (this.nodos.containsKey(next)) {
        nodo_actual = this.nodos.get(next);
      } else {
        return next;
      }
    }
  }

  //pertenece a la interfaz de functions
  public Predicate<T> getPredicate(String etiquetaDestino) {
    // Si el destino es la propia raíz, el predicado es "siempre true"
    if (this.raiz != null && this.raiz.getName().equals(etiquetaDestino)) {
      return x -> true;
    }
    // Llamamos a nuestra función recursiva secreta
    return buscarPredicado(this.raiz, etiquetaDestino);
  }

  private Predicate<T> buscarPredicado(Node<T> nodo, String etiquetaDestino) {
    if (nodo == null)
      return null;

    // 1. EL ACUMULADOR DE FRACASOS
    // Este predicado guardará el "han fallado todas las ramas anteriores".
    // Al empezar a mirar un nodo, empieza en "true" (aún no ha fallado nada).
    Predicate<T> hanFalladoAnteriores = x -> true;

    // 2. RECORREMOS LA LISTA DE RAMAS
    for (Rama<T> rama : nodo.getRamas()) {

      // La fórmula para ENTRAR por esta rama es:
      // (Han fallado las anteriores) AND (Se cumple la condición de esta rama)
      Predicate<T> formulaParaEntrarAqui = hanFalladoAnteriores.and(rama.getCondicion());

      // CASO A: ¡Esta rama apunta directamente a la etiqueta que buscamos!
      if (rama.getNodoDestino().getName().equals(etiquetaDestino)) {
        return formulaParaEntrarAqui;
      }

      // CASO B: Es un nodo intermedio. Hacemos recursividad para seguir bajando.
      if (this.nodos.containsKey(rama.getNodoDestino().getName())) {
        Node<T> nodoHijo = this.nodos.get(rama.getNodoDestino().getName());
        Predicate<T> formulaHijo = buscarPredicado(nodoHijo, etiquetaDestino);

        // Si por ese camino abajo se encontró la etiqueta, unimos nuestra fórmula a la
        // suya
        if (formulaHijo != null) {
          return formulaParaEntrarAqui.and(formulaHijo);
        }
      }

      // Si llegamos aquí, esta rama no nos servía.
      // ACTUALIZAMOS el acumulador diciendo: "Para la siguiente vuelta del bucle,
      // añade la condición de que ESTA rama TAMBIÉN HA FALLADO (negada)".
      hanFalladoAnteriores = hanFalladoAnteriores.and(rama.getCondicion().negate());
    }

    // 3. EL CAMINO POR DEFECTO (otherwise)
    // Si el bucle termina, la fórmula para caer en el otherwise es exactamente
    // nuestro acumulador (todas las ramas negadas y unidas por AND).
    Node<T> nodoDefecto = nodo.getNodoPorDefecto();
    String destinoDefecto = (nodoDefecto == null) ? null : nodoDefecto.getName();
    if (destinoDefecto != null) {

      // CASO A: El otherwise es nuestra etiqueta
      if (destinoDefecto.equals(etiquetaDestino)) {
        return hanFalladoAnteriores;
      }

      // CASO B: El otherwise es un nodo intermedio (Recursividad)
      if (this.nodos.containsKey(destinoDefecto)) {
        Node<T> nodoHijo = this.nodos.get(destinoDefecto);
        Predicate<T> formulaHijo = buscarPredicado(nodoHijo, etiquetaDestino);

        if (formulaHijo != null) {
          return hanFalladoAnteriores.and(formulaHijo);
        }
      }
    }

    // 4. Si exploramos todas las ramas y el otherwise y no está, es un callejón sin
    // salida
    return null;
  }

  public Node<T> getRaiz() {
    return raiz;
  }

  public Map<String, Node<T>> getNodos() {
    return nodos;
  }
}
