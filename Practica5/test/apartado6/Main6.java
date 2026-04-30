package apartado6;

import apartado1.Person;
import apartado2.Main2;
import tree.DecisionTree;
import visualization.GraphvizTreeVisitor;

public class Main6 {
  public static void main(String[] args) {
    GraphvizTreeVisitor textVisitor = new GraphvizTreeVisitor();
    DecisionTree<Person> decisionTree = Main2.buildPersonDecisionTree();
    decisionTree.getRaiz().accept(textVisitor, 1);
    System.out.println(textVisitor.getResult());
  }
}
