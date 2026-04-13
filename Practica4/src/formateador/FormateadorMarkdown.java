package formateador;

import java.util.List;
import java.util.Map;

import estacion.IDocumento;

/**
 * Implementación de {@link IFormateador} encargada de generar una
 * representación del documento en formato HTML.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class FormateadorMarkdown implements IFormateador {

  @Override
  public String formatear(IDocumento doc) {
    StringBuilder sb = new StringBuilder();

    // Título del documento (#) y Título de la sección (##)
    sb.append("# ").append(doc.getTituloDocumento()).append("\n\n");
    sb.append("## ").append(doc.getTituloSeccion()).append("\n\n");

    // Párrafos separados por una línea en blanco
    for (String parrafo : doc.getParrafos()) {
      sb.append(parrafo).append("\n\n");
    }

    // Listas (Sensores y Alertas)
    for (Map.Entry<String, List<String>> entrada : doc.getListas().entrySet()) {
      sb.append("### ").append(entrada.getKey()).append("\n");

      for (String item : entrada.getValue()) {
        // Guiones para los elementos de la lista
        sb.append("- ").append(item).append("\n");
      }
      sb.append("\n"); // Línea en blanco después de cada lista
    }

    // trim() elimina los posibles saltos de línea sobrantes al final del todo
    return sb.toString().trim();
  }
}