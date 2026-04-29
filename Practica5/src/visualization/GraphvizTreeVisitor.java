package visualization;

import tree.Node;
import tree.Rama;

import java.util.HashSet;
import java.util.Set;

public class GraphvizTreeVisitor implements TreeVisitor {

  private StringBuilder sb = new StringBuilder();
  private Set<String> nodosVisitados = new HashSet<>();
  private Set<String> aristasVisitadas = new HashSet<>();

  public GraphvizTreeVisitor() {
    sb.append("digraph DecisionTree {\n");
  }

  public String getResult() {
    sb.append("}\n");
    return sb.toString();
  }

  @Override
  public <T> void visitTreeNode(Node<T> node, int depth) {
    String nombre = node.getName();

    if (nodosVisitados.add(nombre)) {
      sb.append("  \"").append(nombre).append("\";\n");
    }
  }

  @Override
  public <T> void visitRama(Rama<T> rama, int depth) {
    String origen = rama.getOrigen().getName();
    String destino = rama.getNodoDestino().getName();

    String edgeKey = origen + "->" + destino;

    if (aristasVisitadas.add(edgeKey)) {

      String label = (rama.getCondicion() != null)
          ? rama.getCondicion().toString()
          : "otherwise";

      sb.append("  \"")
          .append(origen)
          .append("\" -> \"")
          .append(destino)
          .append("\" [label=\"")
          .append(label)
          .append("\"];\n");
    }
  }
}