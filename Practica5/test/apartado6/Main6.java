package apartado6;

import apartado1.Person;
import apartado2.Main2;
import tree.DecisionTree;
import visualization.PlainTextTreeVisitor;

public class Main6 {
  public static void main(String[] args) {
    PlainTextTreeVisitor textVisitor = new PlainTextTreeVisitor();
    DecisionTree<Person> decisionTree = Main2.buildPersonDecisionTree();

    textVisitor.visitTreeNode(decisionTree.node("root"), 0);
  }
}
