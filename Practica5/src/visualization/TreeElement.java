package visualization;

public interface TreeElement {

  public void accept(TreeVisitor v, int depth);
}
