package visualization;

/**
 * Elemento visitable dentro de la estructura del árbol.
 *
 * <p>
 * Permite aplicar el patrón Visitor sobre nodos y ramas.
 * </p>
 */
public interface TreeElement {

  /**
   * Acepta un visitante.
   *
   * @param v     Visitante aplicado.
   * @param depth Nivel actual en la jerarquía.
   */
  void accept(TreeVisitor v, int depth);
}