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
  String getTituloDocumento();

  String getTituloSeccion();

  List<String> getParrafos();

  Map<String, List<String>> getListas();
}
