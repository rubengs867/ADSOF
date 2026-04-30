package visualization;

import tree.Node;
import tree.Rama;

public class GraphvizTreeVisitor implements TreeVisitor {

  private StringBuilder sb = new StringBuilder();

  public GraphvizTreeVisitor() {
    sb.append("digraph DecisionTree {\n");
    sb.append("  node [shape=box];\n");
  }

  public String getResult() {
    return sb.toString() + "}\n";
  }

  private String getNodeId(Object node) {
    return "node_" + System.identityHashCode(node);
  }

  private String escape(String s) {
    if (s == null)
      return "";
    return s.replace("\"", "\\\"");
  }

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