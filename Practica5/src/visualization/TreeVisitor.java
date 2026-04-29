package visualization;

import tree.Rama;
import tree.Node;

public interface TreeVisitor {

  public <T> void visitTreeNode(Node<T> tn, int depth);
  public <T> void visitRama(Rama<T> rama,  int depth);
}