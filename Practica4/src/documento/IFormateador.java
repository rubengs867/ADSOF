package documento;

public interface IFormateador {
  /**
   * Recibe cualquier objeto que cumpla con ser un documento
   * y devuelve su representación en texto formateado.
   */
  String formatear(IDocumento doc);
  
}