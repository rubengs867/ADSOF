package Practica4.src.formateador;

import java.util.List;
import java.util.Map;

import Practica4.src.documento.IDocumento;
import Practica4.src.documento.IFormateador;

/**
 * Seguimos formato del pdf
 */
public class FormateadorHTML implements IFormateador {

  @Override
  public String formatear(IDocumento doc) {
    StringBuilder sb = new StringBuilder();

    sb.append("<!DOCTYPE html>\n");
    sb.append("<html lang=\"es\">\n");
    sb.append("<head>\n");

    sb.append("  <title>").append(doc.getTituloDocumento()).append("</title>\n");
    sb.append("</head>\n");
    sb.append("<body>\n");

    // Título de la sección
    sb.append("  <h1>").append(doc.getTituloSeccion()).append("</h1>\n");

    // Párrafos
    for (String parrafo : doc.getParrafos()) {
      sb.append("  <p>").append(parrafo).append("</p>\n");
    }

    // Listas (Sensores y Alertas)
    for (Map.Entry<String, List<String>> entrada : doc.getListas().entrySet()) {

      sb.append("  <p>").append(entrada.getKey()).append("</p>\n");
      sb.append("  <ul>\n");

      for (String item : entrada.getValue()) {
        sb.append("    <li>").append(item).append("</li>\n");
      }
      sb.append("  </ul>\n");
    }

    sb.append("</body>\n");
    sb.append("</html>\n");

    return sb.toString();
  }
}