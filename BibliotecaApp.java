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

    System.out.println("\nPrueba 1: Imprimir la lista de libros de un género determinado (Ficcion)\n");
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

    System.out.println("\nPrueba 3: Imprimir los libros con género \"Novela\"\nOutput esperado: ----\n");
    System.out.println("\n " + biblio.librosPorGenero("Novela") + "\n");
    System.out.println("\nPrueba 4: Imprimir los libros posteriores a 2026 \nOutput esperado: Lista vacía\n");
    System.out.println("\n " + biblio.librosPosterioresA(2026) + "\n");
    System.out.println("\nPrueba 5: Imprimir los libros posteriores a 1800\n Output esperado: 5 libros: El murcielago, Learn Java y Learn C (3 veces)\n");
    System.out.println("\n " + biblio.librosPosterioresA(1800) + "\n");
    System.out.println("\nPrueba 6: Imprimir los libros posteriores a 1900\n Output esperado: 4 libros: Learn Java y Learn C (3 veces)\n");
    System.out.println("\n " + biblio.librosPosterioresA(1900) + "\n");
    System.out.println("\nPrueba 7: Imprimir los libros posteriores a 1899\n Output esperado: 5 libros: El murcielago, Learn Java y Learn C (3 veces)\n");
    System.out.println("\n " + biblio.librosPosterioresA(1899) + "\n");
    System.out.println("\nPrueba 8: Imprimir todos los libros\n");
    System.out.println("\n " + biblio.librosPosterioresA(0000) + "\n");
    System.out.println("\nPrueba 9: se pasa un número negativo\n");
    System.out.println("\n " + biblio.librosPosterioresA(-1000) + "\n");

    System.out.println("\nPrueba 9: Devolver un libro que nunca a sido prestado\n");
    libros.get(0).devolver();
    System.out.println("\n " + libros.get(0) + "\n");
  }
}