import java.util.*;

public class Biblioteca{
    private String nombre;
    private List <Libro>  libros;

    // 2. Las estructuras EFICIENTES (Índices)
    private Map<String, List<Libro>> porGenero; // HashMap (rápido)
    private TreeMap<Integer, List<Libro>> porAno; // TreeMap (ordenado)
    
    //Constructor
    public Biblioteca(String nombre, List<Libro> libros){
        this.nombre = nombre;
        if(libros == null){
            this.libros = new ArrayList<>();
        } else {
            this.libros = libros; //haciendo esto si modifican la lista original tambien se modifica la de la biblioteca
        }
        this.porGenero = new HashMap<>(); //inicializamos el mapa
        this.porAno = new TreeMap<>(); //inicializamos el treemap

        if(libros != null){
            for (Libro l: libros){
                this.nuevoLibro(l); //añadimos los libros a la biblioteca, se podría llegar a quitar el this ya que se utiliza dentro de la misma clase
            }
        }
        
    }

    public void nuevoLibro(Libro libro){

        String genero = libro.getGenero();
        this.libros.add(libro); //añadimos a la lista de libros 
        //if(!this.porGenero.containsKey(genero)){
            //this.porGenero.put(genero, new ArrayList<>()); //si no existe el genero lo creamos
        //}
        //this.porGenero.get(genero).add(libro); //añadimos el libro al genero correspondiente
        this.porGenero.computeIfAbsent(genero, k -> new ArrayList<>()).add(libro); //esto hace lo mismo que las 3 lineas comentadas arriba

    }

    public List<Libro> buscarPorGenero(String genero){
        return this.porGenero.getOrDefault(genero, new ArrayList<>()); //si no existe el genero devuelve una lista vacia
    }

    
}
