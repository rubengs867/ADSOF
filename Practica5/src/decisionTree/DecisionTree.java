package decisionTree;
import java.util.*;

import dataset.Dataset;

public class DecisionTree <T>{
  private TreeNode<T> raiz = null;
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



  public  Map<String, List<T>> predict(Collection<T> datos) {
    Map<String, List<T>> resultados = new HashMap<>();

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
    Map<String, List<T>> resultados = new HashMap<>();

    for (T dato : dataset.getObjetos()) { 
      String etiqueta = predicate(dato);
      resultados.putIfAbsent(etiqueta, new ArrayList<>());
      resultados.get(etiqueta).add(dato);
    }

    return resultados;
  }
  private String predicate(T dato){
    TreeNode<T> nodo_actual = this.raiz;

    while(true){
      //evaluamos la condcion actual
      String next = nodo_actual.evaluate(dato);
      //ahora mismo nos hemos parado
      if(next == null){
        //este caso implica que hemos llegado hasta abajo
        if(nodo_actual.getRamas().isEmpty()){
          return nodo_actual.getName();
        }else{
          //nos hemos quedado en un nodo intermedio
          System.err.println("intermedio");
          return nodo_actual.getName();
        }
        
      }

      //actualizamos el nodo
      if(this.nodos.containsKey(next)){
        nodo_actual = this.nodos.get(next);
      }
      else{
        //caso donde nuestro nodo no tiene otro nodo
        //lanzar excepcion
        break;
      }
    }
    return nodo_actual.getName();
  }

}
