package tree;

import java.util.function.Predicate;

import visualization.TreeElement;
import visualization.TreeVisitor;

public class Rama<T> implements TreeElement {
  private Node<T> nodoDestino;
  /*Se va a encargar de guardar la funcion lamnbdda */
  private Predicate<T> condicion;

  //la interfaz predicate lo unico que hace es evaluar true o false la condicion
  public Rama(String nombreNodoDestino, Predicate<T> condicion) {
    this.nodoDestino = new Node<>(nombreNodoDestino);
    this.condicion = condicion;
  }

  public Node<T> getNodoDestino() {
    return nodoDestino;
  }

  public Predicate<T> getCondicion() {
    return condicion;
  }

  @Override
  public void accept(TreeVisitor visitor, int depth) {
    visitor.visitRama(this, depth);

    visitor.visitTreeNode(nodoDestino, depth + 1);
  }
}