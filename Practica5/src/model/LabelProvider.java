package model;

/**
 * @param <T> El tipo de objeto que estamos evaluando
 * @param <L> El tipo de la etiqueta que le asignamos
 */
public interface LabelProvider<T, L> {

  // Recibe un objeto de tipo T y devuelve una etiqueta de tipo L
  L getLabel(T object);

}