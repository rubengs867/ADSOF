package visualization;

import tree.Node;
import tree.Rama;

public class PlainTextTreeVisitor implements TreeVisitor {

  @Override
  public <T> void visitTreeNode(Node<T> node, int depth) {
    String indent = "  ".repeat(Math.max(0, depth));
    System.out.println(indent + "└─── [Nodo: " + node.getName() + "]");
  }

  @Override
  public <T> void visitRama(Rama<T> rama, int depth) {
    String indent = "  ".repeat(Math.max(0, depth));

    String condicion = (rama.getCondicion() != null)
        ? rama.getCondicion().toString()
        : "otherwise";

    System.out.println(indent + "  ├─ Condición: " + condicion);
  }
}