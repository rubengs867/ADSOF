public class Libro{
    private String isbn = null;
    private String titulo = null;
    private String autor = null;
    private int ejemplaresDisponibles = -1;
    private String genero = null;
    private int anno = -1;

    /**
     * Constructor de la clase Libro.
     * Instancia todos los atributos de la clase Libro excepto el género y el año.
     */
    public Libro(String isbn, String titulo, 
                String autor, int ejemplaresDisponibles) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }
    /**
     * Constructor de la clase Libro.
     * Instancia todos los atributos de la clase Libro.
     */
    public Libro(String isbn, String titulo, 
                String autor, int ejemplaresDisponibles, 
                String genero, int anno) {
        this(isbn, titulo, autor, ejemplaresDisponibles);
        this.genero = genero;
        this.anno = anno;
    }

    /**
     * Verifica si el libro está disponible. Si hay ejemplares disponibles.
    */
    public boolean estaDisponible() {
        return this.ejemplaresDisponibles > 0;
    }
    /**
     * Presta un libro.
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
     * Devuelve un libro. Aumenta el número de ejemplares en una unidad.
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
    public int getAnno() {
      return this.anno;
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
     * Imprime el objeto Libro. En caso de no haber género ni año, no los imprime
     */
    @Override
    public String toString(){
      String base = "ISBN: " + this.isbn + ", " + descripcion() + "("+this.ejemplaresDisponibles+ " ejemplares disponibles)";
      if((this.getGenero() != null) && (this.getAnno() >= 0 ))
        return base + ", Género: "+ this.genero + ", Año: " + this.anno;
      return base;
    }
}