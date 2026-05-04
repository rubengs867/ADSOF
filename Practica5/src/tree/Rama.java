package tree;

import java.util.function.Predicate;

import visualization.TreeElement;
import visualization.TreeVisitor;

/**
 * Representa una transición entre dos nodos.
 *
 * @param <T> Tipo de dato evaluado.
 */
public class Rama<T> implements TreeElement {

  /**
   * Nodo origen.
   */
  private Node<T> origen;

  /**
   * Nodo destino.
   */
  private Node<T> destino;

  /**
   * Condición asociada a la rama.
   */
  private Predicate<T> condicion;

  /**
   * Crea una nueva rama.
   *
   * @param origen    Nodo origen.
   * @param destino   Nodo destino.
   * @param condicion Condición aplicada.
   */
  public Rama(Node<T> origen, Node<T> destino, Predicate<T> condicion) {
    this.origen = origen;
    this.destino = destino;
    this.condicion = condicion;
  }

  /**
   * Devuelve el nodo origen.
   *
   * @return Nodo origen.
   */
  public Node<T> getOrigen() {
    return origen;
  }

  /**
   * Devuelve el nodo destino.
   *
   * @return Nodo destino.
   */
  public Node<T> getNodoDestino() {
    return destino;
  }

  /**
   * Devuelve la condición asociada.
   *
   * @return Predicado de evaluación.
   */
  public Predicate<T> getCondicion() {
    return condicion;
  }

  /**
   * Acepta un visitante y continúa el recorrido.
   *
   * @param visitor Visitante.
   * @param depth   Nivel actual.
   */
  @Override
  public void accept(TreeVisitor visitor, int depth) {
    visitor.visitRama(this, depth);
    destino.accept(visitor, depth + 1);
  }
}