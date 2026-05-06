package visualization;

import tree.Node;
import tree.Rama;

/**
 * Implementación de {@link TreeVisitor} que genera una visualización del árbol
 * en texto plano.
 */
public class PlainTextTreeVisitor implements TreeVisitor {

  /**
   * Acumulador de texto plano generado.
   */
  private StringBuilder sb = new StringBuilder();

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

    sb.append(indent + "└─── [Nodo: " + node.getName() + "]");
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

    sb.append(indent + "  ├─ Condición: " + condicion);
  }

  /**
   * Devuelve el árbol completo en texto plano.
   *
   * @return Texto plano generado.
   */
  public String getResult() {
    return sb.toString();
  }
}