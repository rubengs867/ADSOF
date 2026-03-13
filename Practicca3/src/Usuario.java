import java.util.ArrayList;
import java.util.List;

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

  /**
   * Nivel de exposición pública, cúan visible o accesible
   * es para la recepción de mensajes
   */
  private Exposicion exposicion;

  /** Hisotial de mensajes recibidos */
  private List<Mensaje> historialMensajes;

  private double mediaMensajes;

  /**
   * Construye un usuario con nombre, capacidad de amplificación especificados y
   * nivel de exposición por defecto (ALTA).
   *
   * @param nombre                 Nombre del usuario.
   * @param capacidadAmplificacion Capacidad de amplificación asignada.
   */
  public Usuario(String nombre, int capacidadAmplificacion) {
    this(nombre, capacidadAmplificacion, Exposicion.ALTA);
  }

  /**
   * Construye un usuario con nombre, capacidad de amplificación especificados y
   * exposición.
   * 
   * @param nombre                 Nombre del usuario.
   * @param capacidadAmplificacion Capacidad de amplificación asignada.
   * @param exposicion             Nivel de exposición pública.
   */
  public Usuario(String nombre, int capacidadAmplificacion, Exposicion exposicion) {
    this.nombre = nombre;
    this.capacidadAmplificacion = (capacidadAmplificacion < 0) ? 0 : capacidadAmplificacion;
    this.enlaces = new ArrayList<>();
    this.exposicion = exposicion;
    this.historialMensajes = new ArrayList<>();
    this.mediaMensajes = 0.0;
  }

  /**
   * Construye un usuario con capacidad de amplificación por defecto (2).
   *
   * @param nombre Nombre del usuario.
   */
  public Usuario(String nombre) {
    this(nombre, 2);
  }

  /**
   * Modifica el nivel de exposición púbicla del usuario.
   * 
   * @param e Nuevo nivel de exposición.
   */
  public void cambiarExposicion(Exposicion e) {
    this.exposicion = e;
  }

  /**
   * Devuelve el nivel de exposición del usuario.
   * 
   * @return Nivel de exposición pública.
   */
  public Exposicion getExposicion() {
    return this.exposicion;
  }

  /**
   * Devuelve el alcance medio de los mensajes recibidos.
   * 
   * @return Alcance medio.
   */
  public double alcanceMedio() {
    return this.mediaMensajes;
  }

  /**
   * Aumenta en un nivel la exposición publica del usuario
   * en caso de no ser la máxima posible.
   */
  private void aumentarExposicion() {
    Exposicion[] niveles = Exposicion.values();

    if (this.exposicion.ordinal() < niveles.length - 1) {
      this.exposicion = niveles[this.exposicion.ordinal() + 1];
    }
  }

  /**
   * Reduce en un nivel la exposición pública del usuario
   * en caso de no ser la mínima posible.
   */
  private void reducirExposicion() {
    Exposicion[] niveles = Exposicion.values();

    if (this.exposicion.ordinal() > 0) {
      this.exposicion = niveles[this.exposicion.ordinal() - 1];
    }
  }

  /**
   * Representación textual del historial de mensajes.
   * Cada mensaje es representado textualmente en una lína
   * 
   * @return Un String con el historial de mensajes, uno por línea.
   */
  public String mostrarHistorial() {
    StringBuilder sb = new StringBuilder();

    // Recorre todos los mensajes del historial del usuario
    for (Mensaje m : this.historialMensajes) {
      // Añade el mensaje en formato texto y añade un salto de línea
      sb.append(m.toString()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Añade un mensaje al hitorial de mensajes del usuario.
   * Actualiza el alcance medio de los mensajes.
   * Actualiza el nivel de exposición pública del usuario.
   * 
   * @param mensaje Mensaje recibido por el usuario.
   * @return {@code true} si el mensaje se añadió correctamente;
   *         {@code false} en caso contrario.
   */
  public boolean addMensaje(Mensaje mensaje) {
    if (mensaje == null)
      return false;

    int n = this.historialMensajes.size();
    int alcance = mensaje.getAlcanceActual();

    // Actualiza el alcance medio de los mensajes
    this.mediaMensajes = ((this.mediaMensajes * n) + alcance) / (n + 1);

    // Añade le mensaje
    this.historialMensajes.add(mensaje);

    // Actualiza exposición
    if (alcance > this.mediaMensajes) {
      aumentarExposicion();
    } else {
      reducirExposicion();
    }
    return true;
  }

  /**
   * Añade un enlace saliente al usuario.
   * 
   * El enlace debe cumplir:
   * El usuario origen debe ser el propio usuario actual.
   * El usuario destino debe ser distinto del origen.
   * No debe existir ya un enlace previo con el mismo destino.
   *
   * @param e Enlace a añadir.
   * @return {@code true} si el enlace se añadió correctamente;
   *         {@code false} en caso contrario.
   */
  public boolean addEnlace(Enlace e) {
    // Control de errores
    if (e == null)
      return false;

    /*
     * El usuario origen debe coincidir con el usuario actual y
     * debe ser distinto del usuario destino
     */
    if (this != e.getUsuarioOrigen() || this == e.getUsuarioDestino())
      return false;

    /* Evitar añadir un enlace a un destino que ya tiene el usuario */
    if (existeEnlaceConDestino(e.getUsuarioDestino()))
      return false;

    return this.enlaces.add(e);
  }

  /**
   * Crea y añade un nuevo enlace hacia un usuario destino con un coste
   * determinado.
   *
   * @param destino Usuario destino del enlace.
   * @param coste   Coste asociado al enlace.
   * @return {@code true} si el enlace fue creado y añadido correctamente;
   *         {@code false} si el destino es null o ya existe un enlace previo.
   */
  public boolean addEnlace(Usuario destino, int coste) {
    // Control de errores
    if (destino == null)
      return false;

    return this.addEnlace(new Enlace(this, destino, coste));
  }

  /**
   * Comprueba si ya existe un enlace saliente hacia un usuario destino dado.
   *
   * @param usuario Usuario destino a comprobar.
   * @return {@code true} si ya existe un enlace hacia ese usuario
   *         o si el parámetro es null;
   *         {@code false} en caso contrario.
   */
  private boolean existeEnlaceConDestino(Usuario usuario) {
    // Error control
    if (usuario == null)
      return true;

    // Comprueba que no existe un enlace con ese usuario destino
    for (Enlace i : this.enlaces) {
      if (i.getUsuarioDestino() == usuario)
        return true;
    }

    return false;
  }

  /**
   * Devuelve el nombre del usuario.
   *
   * @return Nombre del usuario.
   */
  public String getNombre() {
    return this.nombre;
  }

  /**
   * Devuelve la capacidad de amplificación del usuario.
   *
   * @return Capacidad de amplificación.
   */
  public int getCapacidadAmplificacion() {
    return this.capacidadAmplificacion;
  }

  /**
   * Devuelve el enlace en la posición indicada.
   *
   * @param i Índice del enlace en la lista.
   * @return Enlace correspondiente al índice especificado.
   *         {@code null} en caso de que el íncide esté fuera de límites.
   */
  public Enlace getEnlace(int i) {
    if (i < 0 || i >= enlaces.size())
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
  public Enlace getEnlace(Usuario destUsuario) {
    // Error control
    if (destUsuario == null)
      return null;

    for (Enlace i : this.enlaces) {
      if (i.getUsuarioDestino().equals(destUsuario))
        return i;
    }

    return null; // No se ha encontrado
  }

  /**
   * Devuelve el número de enlaces salientes que tiene el usuario.
   * 
   * @return Tamaño de la lista de enlaces salientes.
   */
  public int getNumEnlaces() {
    return this.enlaces.size();
  }

  /**
   * Devuelve una representación textual del usuario.
   * El formato es:
   * 
   * <pre>
   * &#64;nombre(capacidad)[(enlace1), (enlace2), ...]
   * </pre>
   *
   * @return Cadena representando al usuario y sus enlaces.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("@")
        .append(nombre)
        .append("(")
        .append(capacidadAmplificacion)
        .append(") EXP:") // Añadimos la etiqueta EXP:
        .append(this.exposicion.name()) // Añadimos el valor del enum
        .append(" ["); // Añadimos un espacio antes del corchete

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