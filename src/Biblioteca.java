import java.util.*;

/**
 * Representa una biblioteca que gestiona una colección de libros.
 * Permite realizar búsquedas eficientes por género y año mediante el uso 
 * de estructuras indexadas (HashMap y TreeMap).
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 * @version Version 1.2
 */
public class Biblioteca {

  private String nombre;
  private List<Libro> libros;

  private HashMap<String, List<Libro>> porGenero;
  private TreeMap<Integer, List<Libro>> porAño;

  /**
   * Constructor. En caso de pasar libros = null, se crea un array de libros vacío.
   * @param nombre Nombre de la biblioteca
   * @param libros Lista inicial de libros
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
   * @param nombre Nombre de la biblioteca
   */
  public Biblioteca(String nombre) {
    this.nombre = nombre;
    this.porGenero = new HashMap<>();
    this.porAño = new TreeMap<>();
    this.libros = new ArrayList<>();
  }

  /**
   * Añade un libro a la biblioteca.
   * Si el libro es null o ya existe en la biblioteca, no se realiza ninguna acción.
   * 
   * @param libro Lista de libros.
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
   * Devuelve todos los libros que pertenecen a un género específico.
   *
   * @param genero Género del libro a buscar.
   * @return Lista con los libros encontrados, o una lista vacía si el género no existe.
   */
  public List<Libro> librosPorGenero(String genero) {
    return this.porGenero.getOrDefault(genero, new ArrayList<>());
  }
  /**
   * Busca libros cuya fecha de publicación sea estrictamente posterior al año indicado.
   *
   * @param añoPublicacion Año de publicación límite del libro
   * @return Lista de libros publicados después del año dado, o lista vacía en caso de no haber
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
