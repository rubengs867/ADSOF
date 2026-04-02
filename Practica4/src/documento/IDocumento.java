package Practica4.src.documento;

import java.util.*;
public interface IDocumento {
  String getTitulo();

  String getCuerpo();

  List<String> getParrafos();

  Map<String, List<String>> getListas();
}
