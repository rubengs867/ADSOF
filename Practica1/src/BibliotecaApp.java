import java.util.*;

/**
 * BibliotecaApp
 * Esta clase implementa el punto de entrada de la aplicación y las pruebas 
 * unitarias manuales para las clases Biblioteca y Libros.
 * 
 * El programa ejecuta 8 pruebas consecutivas para validar los filtros 
 * por año de publicación y género.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 * @version Version 2.2
 *
 */
public class BibliotecaApp {
  /**
   * Punto de entrada principal del programa. 
   * @param args Arreglo de cadenas que contiene los argumentos 
   * pasados a través de la línea de comandos.
   */
  public static void main (String[] args)
  {
    /* Crea una array de libros */
    List<Libro> libros = new ArrayList<>( List.of(
    new Libro("1", "El Quijote", "Miguel de Cervantes", 5, "Ficcion", 1605),
    new Libro("2", "El múrcielago", "Jo Nesbo", 1, "Ficcion", 1900),
    new Libro ("3", "Learn Java", "David Hoffman", 6, "Programación", 2000)));
    

    libros.get(1).prestar();
    for(Libro l : libros){
      System.out.println(l);
    }
    libros.get(1).devolver();
    System.out.println(libros);

    libros.add(new Libro("4", "Con viento solano", "Ignacio Aldecoa", 1));
    System.out.println(libros);
    
    // Crea una biblioteca
    Biblioteca biblio = new Biblioteca("Biblio");
    // Añade todos los libros del array a la biblioteca
    for(Libro l : libros)
      biblio.añadirLibro(l);

    // PRUEBAS PARA AÑO PUBLICACIÓN
    System.out.println("\nPrueba 1: Imprimir los libros posteriores a 1800\n");
    System.out.println("Output esperado: El murcielago, Learn Java\n");
    System.out.println("\n " + biblio.librosPosterioresA(1800) + "\n");
    
    System.out.println("\nPrueba 2: Imprimir los libros posteriores a 1899\n");
    System.out.println("Output esperado: El murcielago, Learn Java\n");
    System.out.println("\n " + biblio.librosPosterioresA(1899) + "\n");
    
    System.out.println("\nPrueba 3: Imprimir los libros posteriores a 1900\n");
    System.out.println("Output esperado: Learn Java\n");
    System.out.println("\n " + biblio.librosPosterioresA(1900) + "\n");
    
    System.out.println("\nPrueba 4: Imprimir los libros posteriores a 2026 \n");
    System.out.println("Output esperado: lista vacía\n");
    System.out.println("\n " + biblio.librosPosterioresA(2026) + "\n");
    
    System.out.println("\nPrueba 5: Imprimir todos los posteriores a 0\n");
    System.out.println("Output esperado: El Quijote, El múrcielago, Learn Java\n");
    System.out.println("\n " + biblio.librosPosterioresA(0000) + "\n");
    
    System.out.println("\nPrueba 6: Pasar un año negativo\n");
    System.out.println("Output esperado: El Quijote, El múrcielago, Learn Java\n");
    System.out.println("\n " + biblio.librosPosterioresA(-1000) + "\n");

    // PRUEBAS PARA GÉNERO
    System.out.println("\nPrueba 7: Imprimir la lista de libros del género \"Ficcion\"\n");
    System.out.println("Output esperado: El Quijote y El múrcielago\n");
    System.out.println("\n\n" + biblio.librosPorGenero("Ficcion") + "\n");

    System.out.println("\nPrueba 8: Imprimir los libros con género \"Novela\"\n");
    System.out.println("Output esperado: lista vacía\n");
    System.out.println("\n " + biblio.librosPorGenero("Novela") + "\n");
  }
}