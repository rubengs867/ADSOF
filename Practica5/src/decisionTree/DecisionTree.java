package decisionTree;
import java.util.*;

public class DecisionTree <T>{
  private TreeNode raiz = null;
  //cada TreeNode guarda el valor generico
  private Map<String, TreeNode<T>> nodos;

  //Constructor vacio para el javadoc
  public DecisionTree(){
    this.nodos = new HashMap<>();
  }

  public TreeNode<T> node (String name){
    if(nodos.containsKey(name)){
      return nodos.get(name);
    }
    //no existe ese nuevo nodo, entonces hay que crearlo, el constructor de Treenode podria ser protected
    TreeNode<T> nuevoNodo = new TreeNode<>(name);
    nodos.put(name, nuevoNodo);

    if(this.raiz == null){
      this.raiz= nuevoNodo;
    }
    return nuevoNodo;
  }
}
