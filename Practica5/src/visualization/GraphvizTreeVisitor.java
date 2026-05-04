package visualization;

import tree.Node;
import tree.Rama;

/**
 * Implementación de {@link TreeVisitor} que genera una representación
 * del árbol en formato Graphviz DOT.
 *
 * <p>
 * El resultado puede utilizarse posteriormente para renderizar
 * diagramas del árbol de decisión.
 * </p>
 */
public class GraphvizTreeVisitor implements TreeVisitor {

  /**
   * Acumulador de texto DOT generado.
   */
  private StringBuilder sb = new StringBuilder();

  /**
   * Inicializa la cabecera del grafo.
   */
  public GraphvizTreeVisitor() {
    sb.append("digraph DecisionTree {\n");
    sb.append("  node [shape=box];\n");
  }

  /**
   * Devuelve el contenido completo en formato DOT.
   *
   * @return Texto Graphviz generado.
   */
  public String getResult() {
    return sb.toString() + "}\n";
  }

  /**
   * Genera un identificador único para un nodo.
   *
   * @param node Objeto nodo.
   *
   * @return Identificador interno.
   */
  private String getNodeId(Object node) {
    return "node_" + System.identityHashCode(node);
  }

  /**
   * Escapa caracteres conflictivos para Graphviz.
   *
   * @param s Texto original.
   *
   * @return Texto escapado.
   */
  private String escape(String s) {

    if (s == null) {
      return "";
    }

    return s.replace("\"", "\\\"");
  }

  /**
   * Añade la definición visual de un nodo.
   *
   * @param node  Nodo visitado.
   * @param depth Nivel actual del árbol.
   * @param <T>   Tipo de dato evaluado.
   */
  @Override
  public <T> void visitTreeNode(Node<T> node, int depth) {

    String id = getNodeId(node);
    String label = escape(node.getName());

    sb.append("  \"")
        .append(id)
        .append("\" [label=\"")
        .append(label)
        .append("\"];\n");
  }

  /**
   * Añade una arista entre dos nodos con su condición asociada.
   *
   * @param rama  Rama visitada.
   * @param depth Nivel actual del árbol.
   * @param <T>   Tipo de dato evaluado.
   */
  @Override
  public <T> void visitRama(Rama<T> rama, int depth) {

    String origenId = getNodeId(rama.getOrigen());
    String destinoId = getNodeId(rama.getNodoDestino());

    String label = (rama.getCondicion() != null)
        ? escape(rama.getCondicion().toString())
        : "otherwise";

    sb.append("  \"")
        .append(origenId)
        .append("\" -> \"")
        .append(destinoId)
        .append("\" [label=\"")
        .append(label)
        .append("\"];\n");
  }
}