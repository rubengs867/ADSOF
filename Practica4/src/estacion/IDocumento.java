package estacion;

import java.util.*;

/**
 * Interfaz que define la estructura para cualquier objeto que
 * pueda ser procesado por un formateador de documentos.
 * <p>
 * Proporciona un esquema común para que las clases que lo implementes puedan
 * exportar su estado interno a diferentes formatos de manera uniforme.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public interface IDocumento {

  /**
   * @return El título principal del documento.
   */
  String getTituloDocumento();

  /**
   * @return El título o encabezado de la sección actual.
   */
  String getTituloSeccion();

  /**
   * @return Una lista de cadenas de texto que representan los párrafos del
   *         documento.
   */
  List<String> getParrafos();

  /**
   * @return Un mapa donde cada clave es el nombre de una lista y el valor es el
   *         conjunto de sus elementos.
   */
  Map<String, List<String>> getListas();
}
