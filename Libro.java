public class Libro{
    private String isbn;
    private String titulo;
    private String autor;
    private int ejemplaresDisponibles;
    private String genero;
    private int ano;

    public Libro(String isbn, String titulo, String autor, int ejemplaresDisponibles){
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }


    public Libro(String isbn, String titulo, String autor, int ejemplaresDisponibles, String genero, int ano){
        this(isbn, titulo, autor, ejemplaresDisponibles);
        this.genero = genero;
        this.ano = ano;
    }



    // Método para verificar si el libro está disponible
    public boolean estaDisponible(){
        return this.ejemplaresDisponibles > 0;
    }
    // Método para prestar un libro
    public boolean prestar(){
        if(estaDisponible()){
            this.ejemplaresDisponibles--;
            return true;
        }
        return false;
    }

    // Método para devolver un libro
    public void devolver(){
        this.ejemplaresDisponibles++;
    }
    public String getGenero(){
        return this.genero;
    }

    //Método para obtener la descripción del libro
    private String descripcion(){
        String estado = this.estaDisponible() ? "Disponible" : "No disponible";
        return "'"+this.titulo+"' por "+this.autor+ " [" + estado + "]";

    }
    @Override
    public String toString(){
        return "ISBN: " + this.isbn + ", " + descripcion() + "("+this.ejemplaresDisponibles+ " ejemplares disponibles)" +", "+ this.genero + ", " + this.ano;
    }
}