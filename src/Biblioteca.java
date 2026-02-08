/*
 * Biblioteca
 * Descripción: Implementación de la clase Biblioteca.
 * 
 * Version 1.2
 * Autores: Alejandro Seguido y Rubén García
 */

import java.util.*;

/**
 * Clase Biblioteca.
 * Compuesto por una lista de libros.
 */
public class Biblioteca {

  private String nombre;
  private List<Libro> libros;

  private HashMap<String, List<Libro>> porGenero;
  private TreeMap<Integer, List<Libro>> porAño;

  /**
   * Constructor. En caso de pasar libros = null, se crea un array de libros vacío.
   */
  public Biblioteca(String nombre, List<Libro> libros) {
    this.nombre = nombre;
    // Crea una lista vacía en caso de pasar null
    if (libros == null) {
      this.libros = new ArrayList<>();
    } else {
      this.libros = libros; 
    }
    this.porGenero = new HashMap<>(); // inicializamos el mapa
    this.porAño = new TreeMap<>(); // inicializamos el treemap
    for (Libro libro : this.libros) { // Añadimos los libros al mapa y treemap
      String genero = libro.getGenero();
      int año = libro.getAño();
      this.porGenero.computeIfAbsent(genero, k -> new ArrayList<>()).add(libro);
      this.porAño.computeIfAbsent(año, k -> new ArrayList<>()).add(libro);
    }
  }
  /**
   * Constructor. Inicializa el resto de atributos.
   */
  public Biblioteca(String nombre) {
    this.nombre = nombre;
    this.porGenero = new HashMap<>();
    this.porAño = new TreeMap<>();
    this.libros = new ArrayList<>();
  }

  /**
   * Añade un libro a la biblioteca.
   * En caso de existir, no lo añadimos.
   */
  public void añadirLibro(Libro libro) {
    // Control de errores
    if(libro == null) return;
    if(this.libros.contains(libro)) return; // Si el libro ya existe, no lo añadimos

    String genero = libro.getGenero();
    int año = libro.getAño();
    
    this.libros.add(libro); // Añadimos el libro a la lista
    
    /* En caso de haber género y año se añaden los libros */
    if(libro.getGenero() != null) {
      /* Añadimos el libro al Hashmap en función del género */
      this.porGenero.computeIfAbsent(genero, k -> new ArrayList<>()).add(libro);

      /* Añadimos el libro al Treemap en función del año */
      this.porAño.computeIfAbsent(año, k -> new ArrayList<>()).add(libro);
    }
  }

  /**
   * Devuelve una lista con todos los libros que pertenecen a un género.
   * En caso de no existir el género, devuelve una lista vacía.
   */
  public List<Libro> librosPorGenero(String genero) {
    return this.porGenero.getOrDefault(genero, new ArrayList<>());
  }
  /**
   * Devuelve una lista con todos los libros publicados después de un año.
   * En caso de no haber, devuelve una lista vacía.
   */
  public List<Libro> librosPosterioresA(int añoPublicacion) {
    List<Libro> total = new ArrayList<>();
    
    /* Guarda las listas de libros cuyo año de publicación es mayor al dato pasado */
    NavigableMap<Integer, List<Libro>> librosFiltrados = this.porAño.tailMap(añoPublicacion, false);

    // Junto todos los libros en la misma lista
    for (List<Libro> l : librosFiltrados.values())
      total.addAll(l);

    return total;
  }
}
