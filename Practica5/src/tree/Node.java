package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate; //para usar la interfaz Predicate, su metodo es .test

import visualization.TreeElement;
import visualization.TreeVisitor;

public class Node<T> implements TreeElement {
  private String name;

  private List<Rama<T>> ramas = new ArrayList<>();

  private DecisionTree<T> tree;

  private Node<T> nodoDefecto = null;

  public Node(String name, DecisionTree<T> tree) {
    this.name = name;
    this.tree = tree;
  }

  public Node<T> withCondition(String destino, Predicate<T> condicion) {
    Node<T> nodoDestino = tree.node(destino);
    Rama<T> rama = new Rama<>(this, nodoDestino, condicion);
    this.ramas.add(rama);
    return this;
  }

  public Node<T> otherwise(String destino) {
    this.nodoDefecto = tree.node(destino);
    return this;
  }

  public String getName() {
    return name;
  }

  public List<Rama<T>> getRamas() {
    return ramas;
  }

  public Node<T> getNodoPorDefecto() {
    return nodoDefecto;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String evaluate(T dato) {
    for (Rama r : this.ramas) {
      // cogemos la condicion de la rama y probamos con el metodo de la interfaz test
      // el metodo .test es de tipo boolean
      if (r.getCondicion().test(dato)) {
        return r.getNodoDestino().getName();
      }
    }
    return (this.nodoDefecto == null) ? null : this.nodoDefecto.name;
  }

  @Override
  public void accept(TreeVisitor visitor, int depth) {
    visitor.visitTreeNode(this, depth);

    for (Rama<?> r : getRamas()) {
      r.accept(visitor, depth + 1);
    }

    if (nodoDefecto != null) {
      nodoDefecto.accept(visitor, depth + 1);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Node node))
      return false;

    return name.equals(node.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
