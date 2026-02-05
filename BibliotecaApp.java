import java.util.*;
public class BibliotecaApp {
  public static void main (String[] args){
    List<Libro> libros = new ArrayList<>( List.of(
      new Libro("1", "El Quijote", "Miguel de Cervantes", 5, "Ficcion", 1605),
      new Libro("2", "El múrcielago", "Jo Nesbo", 2, "Ficcion", 1900),
      new Libro ("3", "Learn Java", "David Hoffman", 6, "Ficcon", 2000)));
    libros.get(1).prestar();
    for(Libro l : libros){
      System.out.println(l);
    }
    libros.get(1).devolver();
    System.out.println(libros);

    libros.add(new Libro("4", "Con viento solano", "Ignacio Aldecoa", 1));
    System.out.println(libros);
    
    // Creo una biblioteca
    Biblioteca biblio = new Biblioteca("alejandro");
    // Añado a la biblioteca todos los libros
    for(Libro l : libros)
      biblio.añadirLibro(l);

    // Imprimo los libros con género X
    System.out.println("\n\n" + biblio.librosPorGenero("Ficcion") + "\n");
    // Imprimo los libros posteriores a X año
    System.out.println(biblio.librosPosterioresA(1800) + "\n");
  }
}