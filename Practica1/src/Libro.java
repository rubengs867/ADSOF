/**
 * Representa un libro físico dentro del sistema de la biblioteca.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 * @version Version 2.1
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
   * 
   * @param isbn ISBN del libro.
   * @param titulo Titulo del libro.
   * @param autor Autor del libro.
   * @param ejemplaresDisponibles Número de ejemplares disponibles del libro.
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
   * 
   * @param isbn ISBN del libro.
   * @param titulo Titulo del libro.
   * @param autor Autor del libro.
   * @param ejemplaresDisponibles Número de ejemplares disponibles del libro.
   * @param genero Género del libro.
   * @param año Año de publicación del libro.
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
   * Verifica la disponiblilidad de ejemplares.
   * @return True si hay al menos un ejemplar disponible, False en caso contrario.
  */
  public boolean estaDisponible() {
    return this.ejemplaresDisponibles > 0;
  }

  /**
   * Realiza el préstamo de un libro reduciendo el stock.
   * @return True si hay al menos un ejemplar disponible, False en caso contrario.
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
   * @return cadena de caracteres del ISBN.
   */
  public String getISBN() {
    return this.isbn;
  }
  /**
   * Devuelve el autor del libro.
   * @return cadena de caracteres del nombre del autor.
   */
  public String getAutor() {
    return this.autor;
  }
  /**
   * Devuelve el género del libro.
   * @return cadena de caracteres del género del libro.
   */
  public String getGenero() {
      return this.genero;
  }
  /**
   * Devuelve el año del libro.
   * @return integer del año de publicación.
   */
  public int getAño() {
    return this.año;
  }

  /**
   * Obtiene la descripción del libro.
   * @return cadena de caracteres con formato: 'TÍTULO por AUTOR [ESTADO(Disponible/No disponible)]'
   */
  private String descripcion() {
    String estado = this.estaDisponible() ? "Disponible" : "No disponible";
    return "'"+this.titulo+"' por "+this.autor+ " [" + estado + "]";

  }

  /**
   * Devuelve una representación textual del libro.
   * En caso de no haber género ni año, no los imprime.
   * @return cadena de caracteres el formato: ISBN, Título, Autor y Disponibilidad.
   */
  @Override
  public String toString(){
    String base = "ISBN: " + this.isbn + ", " + descripcion() + "("+this.ejemplaresDisponibles+ " ejemplares disponibles)";
    /* Solo se comprueba el género, porque o existe género y año de publicación, o no existe ninguno */
    if((this.getGenero() != null))
      return base + ", Género: "+ this.genero + ", Año: " + this.año;
    return base;
  }

  /**
   * Compara si dos libros son iguales.
   * @param o Objeto a comparar.
   * @return True si los ISBN coinciden, False en caso contrario.
  */
  @Override
  public boolean equals(Object o){
    if (this == o) return true; // Si es el mismo objeto, son iguales, comparamos direcciones de memoria
    if (o == null || getClass() != o.getClass()) return false; // Si el objeto es null o de diferente clase, no son iguales
    Libro libro = (Libro) o; // Convertimos el objeto a Libro
    return this.isbn.equals(libro.getISBN()); // Comparamos los ISBN
  }
}