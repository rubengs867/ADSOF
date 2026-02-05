import java.util.*;

public class Biblioteca {
    private String nombre;
    private List<Libro> libros;

    // Las estructuras EFICIENTES (Índices)
    private HashMap<String, List<Libro>> porGenero; // HashMap (rápido)
    private TreeMap<Integer, List<Libro>> porAnno; // TreeMap (ordenado)

    /** 
     * Instancia los atributos nombre y la lista de libros. Introduce los libros a los índices correspondientes. 
    */
    public Biblioteca(String nombre, List<Libro> libros){
        this.nombre = nombre;
        //this.libros = libros;
        if(libros == null){
            this.libros = new ArrayList<>();
        } else {
            this.libros = libros; //haciendo esto, si modifica la lista original tambien se modifica la de la biblioteca
        }
        this.porGenero = new HashMap<>(); //inicializamos el mapa
        this.porAnno = new TreeMap<>(); //inicializamos el treemap        
    }

    public Biblioteca(String nombre) {
        this.nombre = nombre;
        this.porGenero = new HashMap<>(); // inicializamos el mapa
        this.porAno = new TreeMap<>(); // inicializamos el treemap
        this.libros = new ArrayList<>(); // inicializamos la lista de libros
    }

    public void nuevoLibro(Libro libro) {

        String genero = libro.getGenero();
        this.libros.add(libro); // añadimos a la lista de libros
        // if(!this.porGenero.containsKey(genero)){
        // this.porGenero.put(genero, new ArrayList<>()); //si no existe el genero lo
        // creamos
        // }
        // this.porGenero.get(genero).add(libro); //añadimos el libro al genero
        // correspondiente
        this.porGenero.computeIfAbsent(genero, k -> new ArrayList<>()).add(libro); // esto hace lo mismo que las 3
                                                                                   // lineas comentadas arriba

    }

    public List<Libro> buscarPorGenero(String genero) {
        return this.porGenero.getOrDefault(genero, new ArrayList<>()); // si no existe el genero devuelve una lista
                                                                       // vacia
    }

}