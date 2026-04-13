package estacion;

import java.util.*;
public interface IDocumento {
  String getTituloDocumento();

  String getTituloSeccion();

  List<String> getParrafos();

  Map<String, List<String>> getListas();
}
