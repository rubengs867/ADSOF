import java.util.*;

public class Usuario {
  private final String nombre;
  private final int capacidadAmplificacion;
  private List<Enlace> enlaces;
  
  public Usuario(String nombre, int capacidadAmplificacion){
    this.nombre = nombre;
    this.capacidadAmplificacion = capacidadAmplificacion;
    this.enlaces = new ArrayList<>();
  }

  public Usuario(String nombre){
    this(nombre, 2);
  }

  public boolean addEnlace(Enlace e){
    if(e == null) 
      return false;
    
    /* El usuario origen debe coincidir con el usuario actual y 
      debe ser distinto del usuario destino */
    if(this != e.getUsuarioOrigen() && this == e.getUsuarioDestino()) 
      return false;

    /* Evitar añadir un enlace a un destino que ya tiene el usuario */
    if(existeEnlaceConDestino(e.getUsuarioDestino())) 
      return false;

    return this.enlaces.add(e);
  }

  private boolean existeEnlaceConDestino(Usuario usuario){
    // Error control
    if(usuario == null) return true;

    // Comprueba que no existe un enlace con ese usuario destino
    for(Enlace i : this.enlaces) {
      if(i.getUsuarioDestino() == usuario)
        return true;
    }

    return false;
  }

  public boolean addEnlace(Usuario destino, int coste){
    //Error control
    if(destino == null) return false;

    // Compruebo que no exista ya un enlace con ese usuario destino
    if(existeEnlaceConDestino(destino)) return false;

    // Creo el enlace y lo añado a la lista
    return this.enlaces.add(new Enlace(this, destino, coste));
  }

  public String getNombre(){
    return this.nombre;
  }

  public int getCapacidadAmplificacion(){
    return this.capacidadAmplificacion;
  }

  public Enlace getEnlace(int i){
    return this.enlaces.get(i);
  }

  public Enlace getEnlace(Usuario destUsuario){
    // Error control
    if(destUsuario == null) return null;

    for(Enlace i : this.enlaces) {
      if(i.getUsuarioDestino() == destUsuario)
        return i;
    }

    return null; // No se ha encontrado
  }
}