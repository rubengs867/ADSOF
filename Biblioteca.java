
import java.util.*;

public class Biblioteca {

  private String nombre;
  private List<Libro> libros;

  private HashMap<String, List<Libro>> porGenero; // HashMap (rápido)
  private TreeMap<Integer, List<Libro>> porAño; // TreeMap (ordenado)

  /**
   * Instancia los atributos nombre y la lista de libros. Introduce los libros
   * a los índices correspondientes.
   */
  public Biblioteca(String nombre, List<Libro> libros) {
    this.nombre = nombre;
    if (libros == null) {
      this.libros = new ArrayList<>();
    } else {
      this.libros = libros; //haciendo esto, si modifica la lista original tambien se modifica la de la biblioteca
    }
    this.porGenero = new HashMap<>(); // inicializamos el mapa
    this.porAño = new TreeMap<>(); // inicializamos el treemap
  }

  public Biblioteca(String nombre) {
    this.nombre = nombre;
    this.porGenero = new HashMap<>(); // inicializamos el mapa
    this.porAño = new TreeMap<>(); // inicializamos el treemap
    this.libros = new ArrayList<>(); // inicializamos la lista de libros
  }


  public void añadirLibro(Libro libro) {
    String genero = libro.getGenero();
    int año = libro.getAño();
    
    this.libros.add(libro); // Añadimos el libro a la lista
    
    /* Añadimos el libro al Hashmap en función del género */
    // this.porGenero.putIfAbsent(genero, new ArrayList<>()); // Crea una lista vacía en caso de no existir el género
    // this.porGenero.get(genero).add(libro); // Anade el libro
    this.porGenero.computeIfAbsent(genero, k -> new ArrayList<>()).add(libro);

    /* Añadimos el libro al Treemap en función del año */
    // this.porAño.putIfAbsent(año, new ArrayList<>()); // Crea una lista vacía en caso de no existir el año
    // this.porAño.get(año).add(libro); // Añade el libro
    this.porAño.computeIfAbsent(año, k -> new ArrayList<>()).add(libro);
  }

  /**
   * Devuelve una lista con todos los libros que pertenecen a un determinado género. 
   * En caso de no existir el género, devuelve una lista vacía.
   */
  public List<Libro> librosPorGenero(String genero) {
    return this.porGenero.getOrDefault(genero, new ArrayList<>()); // si no existe el genero devuelve una lista vacia
    //La lista vacía es mejor que null porque así evitamos NullPointerException al intentar acceder a una lista que no existe
  }

  /**
   * Devuelve una lista con todos los libros con todos los libros publicados después de ese año.
   * En caso de no haber, devuelve una lista vacía
   */
  public List<Libro> librosPosterioresA(int añoPublicacion) {
    List<Libro> total = new ArrayList<>();
    
    // Guarda las listas de libros cuyo año de publicación es mayor al dato pasado, pero lo devuelve como un mapa ordenado por año
    NavigableMap<Integer, List<Libro>> librosFiltrados = this.porAño.tailMap(añoPublicacion, false);

    // Junto todos los libros en la misma lista
    for (List<Libro> l : librosFiltrados.values())
      total.addAll(l);

    return total;
  }
}
