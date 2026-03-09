import java.util.*;

/**
 * Representa un usuario dentro de una red de amplificación.
 */
public class Usuario {
  /** Nombre identificador del usuario. */
  private final String nombre;
  
  /** Capacidad de amplificación asociada al usuario. */
  private final int capacidadAmplificacion;

  /** Lista de enlaces salientes desde este usuario. */
  private List<Enlace> enlaces;

  private Exposicion exposicion;

  private List<Mensaje> historialMensajes;

  private double mediaMensajes;
  
  /**
   * Construye un usuario con nombre y capacidad de amplificación especificados.
   *
   * @param nombre Nombre del usuario.
   * @param capacidadAmplificacion Capacidad de amplificación asignada.
   */
  public Usuario(String nombre, int capacidadAmplificacion){
    this.nombre = nombre;
    this.capacidadAmplificacion = capacidadAmplificacion;
    this.enlaces = new ArrayList<>();
    this.exposicion = Exposicion.ALTA;
    this.historialMensajes = new ArrayList<>();
    this.mediaMensajes = 0.0;
  }

  public Usuario(String nombre, int capacidadAmplificacion, Exposicion exposicion){
    this(nombre, capacidadAmplificacion);
    this.exposicion = exposicion;
  }

  /**
   * Construye un usuario con capacidad de amplificación por defecto (2).
   *
   * @param nombre Nombre del usuario.
   */
  public Usuario(String nombre){
    this(nombre, 2);
  }

  public void cambiarExposicion (Exposicion e){
    this.exposicion = e;
  }

  public Exposicion getExposicion(){
    return this.exposicion;
  }

  /**
   * @return El alcance medio de los mensajes recibidos.
   */
  public double alcanceMedio() {
    return this.mediaMensajes;
  }

  private void aumentarExposicion() {
    Exposicion[] niveles = Exposicion.values();

    if (this.exposicion.ordinal() < niveles.length - 1) {
      this.exposicion = niveles[this.exposicion.ordinal() + 1];
    }
  }

  private void reducirExposicion() {
    Exposicion[] niveles = Exposicion.values();
    if (this.exposicion.ordinal() > niveles.length - 1) { 
      this.exposicion = niveles[this.exposicion.ordinal() - 1];
    }
  }

  /**
   * @return Un String con el historial de mensajes, uno por línea.
   */
  public String mostrarHistorial() {
    StringBuilder sb = new StringBuilder();
    for (Mensaje m : this.historialMensajes) {
      sb.append(m.toString()).append("\n"); 
    }
    return sb.toString();
  }


  public void anadirMensaje(Mensaje mensaje) {
    int n = this.historialMensajes.size(); 
    int alcance = mensaje.getAlcanceActual();
    this.mediaMensajes = ((this.mediaMensajes * n) + alcance) / (n + 1);
    
    this.historialMensajes.add(mensaje);

    if(alcance > this.mediaMensajes){
      aumentarExposicion();
    }
    else{
      reducirExposicion();
    }
  }

  /**
   * Añade un enlace saliente al usuario.
   * 
   * El enlace debe cumplir:
   *   El usuario origen debe ser el propio usuario actual.
   *   El usuario destino debe ser distinto del origen.
   *   No debe existir ya un enlace previo con el mismo destino.
   *
   * @param e Enlace a añadir.
   * @return true si el enlace se añadió correctamente;
   *         false en caso contrario.
   */
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

  /**
   * Comprueba si ya existe un enlace saliente hacia un usuario destino dado.
   *
   * @param usuario Usuario destino a comprobar.
   * @return true si ya existe un enlace hacia ese usuario
   *         o si el parámetro es null;
   *         false en caso contrario.
   */
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

  /**
   * Crea y añade un nuevo enlace hacia un usuario destino con un coste determinado.
   *
   * @param destino Usuario destino del enlace.
   * @param coste Coste asociado al enlace.
   * @return true si el enlace fue creado y añadido correctamente;
   *         false si el destino es null o ya existe un enlace previo.
   */
  public boolean addEnlace(Usuario destino, int coste){
    //Error control
    if(destino == null) return false;

    // Compruebo que no exista ya un enlace con ese usuario destino
    if(existeEnlaceConDestino(destino)) return false;

    // Creo el enlace y lo añado a la lista
    return this.enlaces.add(new Enlace(this, destino, coste));
  }

  /**
   * Devuelve el nombre del usuario.
   *
   * @return Nombre del usuario.
   */
  public String getNombre(){
    return this.nombre;
  }

  /**
   * Devuelve la capacidad de amplificación del usuario.
   *
   * @return Capacidad de amplificación.
   */
  public int getCapacidadAmplificacion(){
    return this.capacidadAmplificacion;
  }

  /**
   * Devuelve el enlace en la posición indicada.
   *
   * @param i Índice del enlace en la lista.
   * @return  Enlace correspondiente al índice especificado.
   *          null en caso de que el íncide esté fuera de límites.
   */
  public Enlace getEnlace(int i){
    if(i < 0 || i >= enlaces.size()) 
      return null;

    return this.enlaces.get(i);
  }

  /**
   * Devuelve el enlace saliente hacia un usuario destino específico.
   *
   * @param destUsuario Usuario destino buscado.
   * @return El enlace correspondiente si existe;
   *         {@code null} si no se encuentra o si el parámetro es {@code null}.
   */
  public Enlace getEnlace(Usuario destUsuario){
    // Error control
    if(destUsuario == null) return null;

    for(Enlace i : this.enlaces) {
      if(i.getUsuarioDestino() == destUsuario)
        return i;
    }

    return null; // No se ha encontrado
  }

  /**
   * Devuelve una representación textual del usuario.
   *
   * El formato es:
   * <pre>
   * @nombre(capacidad)[(enlace1), (enlace2), ...]
   * </pre>
   *
   * @return Cadena representando al usuario y sus enlaces.
   */
  public int getNumEnlaces(){
    return this.enlaces.size();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("@")
      .append(nombre)
      .append("(")
      .append(capacidadAmplificacion)
      .append(") EXP:")                      // Añadimos la etiqueta EXP:
      .append(this.exposicion.name())        // Añadimos el valor del enum 
      .append(" [");                         // Añadimos un espacio antes del corchete
    
    for (int i = 0; i < enlaces.size(); i++) {
      sb.append(enlaces.get(i).toString());
      if (i < enlaces.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");

    return sb.toString();
  }
}