package decisionTree;

import java.util.*;

public class TreeNode<T> {
  private String name;

  private List<Rama<T>> ramas;

  private String nodoDefecto = null;

  public TreeNode(String name) {
    this.name = name;
    this.ramas = new ArrayList<>();
  }

  public TreeNode<T> withCondition(String destino, Predicate<T> condicion) {
    this.ramas.add(new Rama<>(destino, condicion));
    return this;
  }

  public TreeNode<T> otherwise(String destino) {
    this.nodoDefecto = destino;
    return this;
  }

  public String getName() {
    return name;
  }

  public List<Rama<T>> getRamas() {
    return ramas;
  }

  public String getNodoPorDefecto() {
    return nodoDefecto;
  }

}
