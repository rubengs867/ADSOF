package visualization;

import tree.Node;
import tree.Rama;

/**
 * Implementación de {@link TreeVisitor} que muestra el árbol
 * en formato de texto plano por consola.
 */
public class PlainTextTreeVisitor implements TreeVisitor {

  /**
   * Imprime un nodo del árbol con sangrado jerárquico.
   *
   * @param node  Nodo visitado.
   * @param depth Nivel actual.
   * @param <T>   Tipo de dato evaluado.
   */
  @Override
  public <T> void visitTreeNode(Node<T> node, int depth) {

    String indent = "  ".repeat(Math.max(0, depth));

    System.out.println(
        indent + "└─── [Nodo: " + node.getName() + "]");
  }

  /**
   * Imprime una rama y su condición asociada.
   *
   * @param rama  Rama visitada.
   * @param depth Nivel actual.
   * @param <T>   Tipo de dato evaluado.
   */
  @Override
  public <T> void visitRama(Rama<T> rama, int depth) {

    String indent = "  ".repeat(Math.max(0, depth));

    String condicion = (rama.getCondicion() != null)
        ? rama.getCondicion().toString()
        : "otherwise";

    System.out.println(
        indent + "  ├─ Condición: " + condicion);
  }
}