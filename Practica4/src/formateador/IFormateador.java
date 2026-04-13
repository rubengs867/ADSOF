package formateador;

import estacion.IDocumento;

/**
 * Interfaz que define el contrato para los formateadores de documentos.
 * <p>
 * Las clases que implementen esta interfaz serán responsables de transformar
 * la información contenida en un objeto que implemente {@link IDocumento}
 * a un formato de texto específico.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public interface IFormateador {
  /**
   * Recibe cualquier objeto que cumpla con ser un documento
   * y devuelve su representación en texto formateado.
   * 
   * @param doc Documento a formatear.
   * @return representación en texto.
   */
  String formatear(IDocumento doc);

}