package tree;

import java.util.function.Predicate;

import visualization.TreeElement;
import visualization.TreeVisitor;

public class Rama<T> implements TreeElement {
  private Node<T> origen;
  private Node<T> destino;
  private Predicate<T> condicion;

  public Rama(Node<T> origen, Node<T> destino, Predicate<T> condicion) {
    this.origen = origen;
    this.destino = destino;
    this.condicion = condicion;
  }

  public Node<T> getOrigen() {
    return origen;
  }

  public Node<T> getNodoDestino() {
    return destino;
  }

  public Predicate<T> getCondicion() {
    return condicion;
  }

  @Override
  public void accept(TreeVisitor visitor, int depth) {
    visitor.visitRama(this, depth);
    destino.accept(visitor, depth + 1);
  }
}