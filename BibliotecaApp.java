import java.util.*;
public class BibliotecaApp {
  public static void main (String[] args){
    List<Libro> libros = new ArrayList<>( List.of(
      new Libro("1", "El Quijote", "Miguel de Cervantes", 5, "Ficcion", 1605),
      new Libro("2", "El múrcielago", "Jo Nesbo", 2, "Ficcion", 1900),
      new Libro ("3", "Learn Java", "David Hoffman", 6, "Ficcion", 2000)));
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

    System.out.println("\nPrueba 1:\n");
    // Imprimo los libros con género X
    System.out.println("\n\n" + biblio.librosPorGenero("Ficcion") + "\n");
    // Imprimo los libros posteriores a X año
    System.out.println(biblio.librosPosterioresA(1800) + "\n");

    System.out.println("\nPrueba 2:\n");
    List <String> generos = List.of("Ficcion", "No Ficcion", "Poesia");
    for(String g : generos){
      biblio.añadirLibro(new Libro ("5", "Learn C", "Teuman", 1, g, 2020));
    } 
    System.out.println("\n " + biblio.librosPorGenero("Poesia") + "\n");

    System.out.println("\nPrueba 3:\n");
    System.out.println("\n " + biblio.librosPorGenero("Novela") + "\n");
    System.out.println("\n " + biblio.librosPosterioresA(2026) + "\n");
  }
}