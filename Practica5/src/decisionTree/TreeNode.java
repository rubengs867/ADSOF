package decisionTree;

import java.util.*;
import java.util.function.Predicate; //para usar la interfaz Predicate, su metodo es .test

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

  public String evaluate(T dato){
    for(Rama r : this.ramas){
      //cogemos la condicion de la rama y probamos con el metodo de la interfaz test
      //el metodo .test es de tipo boolean
      if(r.getCondicion().test(dato)){
        return r.getNodoDestino();
      }
    }
    return this.nodoDefecto;
  }

}
