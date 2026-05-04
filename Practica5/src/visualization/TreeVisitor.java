package visualization;

import tree.Node;
import tree.Rama;

/**
 * Define las operaciones del patrón Visitor para los
 * elementos de un árbol de decisión.
 */
public interface TreeVisitor {

  /**
   * Procesa un nodo del árbol.
   *
   * @param node  Nodo visitado.
   * @param depth Nivel actual.
   * @param <T>   Tipo de dato evaluado.
   */
  <T> void visitTreeNode(Node<T> node, int depth);

  /**
   * Procesa una rama del árbol.
   *
   * @param rama  Rama visitada.
   * @param depth Nivel actual.
   * @param <T>   Tipo de dato evaluado.
   */
  <T> void visitRama(Rama<T> rama, int depth);
}