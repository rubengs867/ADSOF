/*
 * Libro
 * Descripción: Implementación de la clase Libro.
 * 
 * Version 2.1
 * Autores: Alejandro Seguido y Rubén García
 */

/**
 * Clase Libro.
 */
public class Libro{
  private String isbn = null;
  private String titulo = null;
  private String autor = null;
  private int ejemplaresDisponibles = -1;
  private String genero = null;
  private int año = -1;

  /**
   * Constructor.
   * No instancia género ni año.
   */
  public Libro( String isbn, String titulo, 
                String autor, int ejemplaresDisponibles) 
  {
    this.isbn = isbn;
    this.titulo = titulo;
    this.autor = autor;
    this.ejemplaresDisponibles = ejemplaresDisponibles;
  }

  /**
   * Constructor.
   * Instancia todos los atributos.
   */
  public Libro( String isbn,    String titulo, 
                String autor,   int ejemplaresDisponibles, 
                String genero,  int año) 
  {
    this(isbn, titulo, autor, ejemplaresDisponibles);
    this.genero = genero;
    this.año = año;
  }

  /**
   * Verifica si hay ejemplares disponibles.
  */
  public boolean estaDisponible() {
    return this.ejemplaresDisponibles > 0;
  }

  /**
   * Disminuye el número de ejemplares en una unidad.
   */
  public boolean prestar() {
    if(estaDisponible()){
      this.ejemplaresDisponibles--;
      return true;
    }
    return false;
  }
  /**
   * Aumenta el número de ejemplares en una unidad.
   */
  public void devolver() {
    this.ejemplaresDisponibles++;
  }

  /**
   * Devuelve el ISBN del libro.
   */
  public String getISBN() {
    return this.isbn;
  }
  /**
   * Devuelve el autor del libro.
   */
  public String getAutor() {
    return this.autor;
  }
  /**
   * Devuelve el género del libro.
   */
  public String getGenero() {
      return this.genero;
  }
  /**
   * Devuelve el año del libro.
   */
  public int getAño() {
    return this.año;
  }

  /**
   * Obtiene la descripción del libro.
   * Formato: 'TÍTULO por AUTOR [ESTADO(Disponible/No disponible)]'
   */
  private String descripcion() {
    String estado = this.estaDisponible() ? "Disponible" : "No disponible";
    return "'"+this.titulo+"' por "+this.autor+ " [" + estado + "]";

  }

  /**
   * Imprime el objeto Libro. 
   * En caso de no haber género ni año, no los imprime.
   */
  @Override
  public String toString(){
    String base = "ISBN: " + this.isbn + ", " + descripcion() + "("+this.ejemplaresDisponibles+ " ejemplares disponibles)";
    if((this.getGenero() != null) && (this.getAño() >= 0 ))
      return base + ", Género: "+ this.genero + ", Año: " + this.año;
    return base;
  }
}