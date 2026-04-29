package visualization;

import tree.Node;
import tree.Rama;

public class PlainTextTreeVisitor implements TreeVisitor {

  @Override
  public <T> void visitTreeNode(Node<T> node, int depth) {
    String indent = "  ".repeat(depth - 1);
    System.out.println(indent + "└─── [Nodo: " + node.getName() + "]");
  }

  @Override
  public <T> void visitRama(Rama<T> rama, int depth) {
    String indent = "  ".repeat(depth);
    System.out.println(indent + "├─ Condición: " + rama.getCondicion());
  }
}