package decisionTree;

import java.util.function.Predicate;

public class Rama<T> {
  private String nodoDestino;
  private Predicate<T> condicion;

  //la interfaz predicate lo unico que hace es evaluar true o false la condicion
  public Rama(String nodoDestino, Predicate<T> condicion) {
    this.nodoDestino = nodoDestino;
    this.condicion = condicion;
  }

  public String getNodoDestino() {
    return nodoDestino;
  }

  public Predicate<T> getCondicion() {
    return condicion;
  }
}