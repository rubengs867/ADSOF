package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import visualization.TreeElement;
import visualization.TreeVisitor;

/**
 * Representa un nodo del árbol de decisión.
 *
 * @param <T> Tipo de dato evaluado.
 */
public class Node<T> implements TreeElement {

  /**
   * Nombre identificador del nodo.
   */
  private String name;

  /**
   * Ramas salientes del nodo.
   */
  private List<Rama<T>> ramas = new ArrayList<>();

  /**
   * Árbol al que pertenece el nodo.
   */
  private DecisionTree<T> tree;

  /**
   * Nodo de destino por defecto.
   */
  private Node<T> nodoDefecto = null;

  /**
   * Crea un nodo nuevo.
   *
   * @param name Nombre del nodo.
   * @param tree Árbol propietario.
   */
  public Node(String name, DecisionTree<T> tree) {
    this.name = name;
    this.tree = tree;
  }

  /**
   * Añade una rama condicional hacia otro nodo.
   *
   * @param destino   Nombre del nodo destino.
   * @param condicion Condición de activación.
   *
   * @return Nodo actual.
   */
  public Node<T> withCondition(String destino, Predicate<T> condicion) {

    Node<T> nodoDestino = tree.node(destino);

    if (tree.getNodosVisitados().add(nodoDestino)) {
      Rama<T> rama = new Rama<>(this, nodoDestino, condicion);
      this.ramas.add(rama);
    }

    return this;
  }

  /**
   * Define el nodo por defecto.
   *
   * @param destino Nombre del nodo destino.
   *
   * @return Nodo actual.
   */
  public Node<T> otherwise(String destino) {

    Node<T> nodoDestino = tree.node(destino);

    if (tree.getNodosVisitados().add(nodoDestino)) {
      this.nodoDefecto = nodoDestino;
    }

    return this;
  }

  /**
   * Devuelve el nombre del nodo.
   *
   * @return Nombre identificador.
   */
  public String getName() {
    return name;
  }

  /**
   * Devuelve las ramas del nodo.
   *
   * @return Lista de ramas.
   */
  public List<Rama<T>> getRamas() {
    return ramas;
  }

  /**
   * Devuelve el nodo por defecto.
   *
   * @return Nodo por defecto o {@code null}.
   */
  public Node<T> getNodoPorDefecto() {
    return nodoDefecto;
  }

  /**
   * Evalúa un dato y determina el siguiente destino.
   *
   * @param dato Dato de entrada.
   *
   * @return Nombre del siguiente nodo o {@code null}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String evaluate(T dato) {

    for (Rama rama : this.ramas) {
      if (rama.getCondicion().test(dato)) {
        return rama.getNodoDestino().getName();
      }
    }

    return (this.nodoDefecto == null)
        ? null
        : this.nodoDefecto.name;
  }

  /**
   * Acepta un visitante para recorrer el árbol.
   *
   * @param visitor Visitante.
   * @param depth   Nivel actual.
   */
  @Override
  public void accept(TreeVisitor visitor, int depth) {

    visitor.visitTreeNode(this, depth);

    for (Rama<?> r : getRamas()) {
      r.accept(visitor, depth + 1);
    }

    if (nodoDefecto != null) {
      Rama<T> ramaOtherwise = new Rama<>(this, nodoDefecto, null);

      ramaOtherwise.accept(visitor, depth + 1);
    }
  }

  /**
   * Compara nodos por nombre.
   *
   * @param o Objeto a comparar.
   *
   * @return {@code true} si son equivalentes.
   */
  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (!(o instanceof Node node)) {
      return false;
    }

    return name.equals(node.name);
  }

  /**
   * Devuelve el hash basado en el nombre.
   *
   * @return Código hash.
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }
}