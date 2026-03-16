/**
 * Representa un mensaje dentro de la red social.
 * Almacena el contenido del mensaje, el usuario en el que se encuentra
 * actualmente y el alcance restante para poder seguir difundiéndose.
 */
public class Mensaje {
  /** Funciona como el contenido textual del mensaje */
  private String autor;

  /** Usuario actual donde se encuenta el mensaje */
  private Usuario actual;

  /** Alcance del mensaje */
  private int alcance;

  /**
   * Construye un nuevo Mensaje con su contenido, alcance inicial y usuario de
   * origen.
   * 
   * @param autor   el contenido textual del mensaje
   * @param alcance el alcance inicial del mensaje
   * @param usuario el usuario en el que se origina o encuentra el mensaje
   */
  public Mensaje(String autor, int alcance, Usuario usuario) {
    this.autor = autor;
    this.actual = usuario;
    this.alcance = alcance;
  }

  /**
   * Actualiza el usuario en el que se encuentra posicionado el mensaje.
   * 
   * @param actual el nuevo usuario actual
   */
  public void setUsuarioActual(Usuario actual) {
    this.actual = actual;
  }

  /**
   * Actualiza el alcance disponible del mensaje.
   * 
   * @param alcance el nuevo valor de alcance
   */
  public void setAlcanceActual(int alcance) {
    this.alcance = alcance;
  }

  /**
   * Obtiene el usuario en el que se encuentra actualmente el mensaje.
   * 
   * @return el usuario actual
   */
  public Usuario getUsuarioActual() {
    return this.actual;
  }

  /**
   * Obtiene el contenido textual del mensaje.
   * 
   * @return el texto del mensaje (almacenado en la propiedad autor)
   */
  public String getUsuarioAutor() {
    return this.autor;
  }

  /**
   * Obtiene el alcance actual disponible para difundir el mensaje.
   * 
   * @return el alcance del mensaje
   */
  public int getAlcanceActual() {
    return this.alcance;
  }

  /**
   * Intenta difundir el mensaje a través del enlace especificado.
   * Si tiene éxito, actualiza el usuario actual y recalcula el alcance del
   * mensaje.
   * 
   * @param e el enlace por el cual se intentará difundir
   * @return {@code true} si la difusión fue exitosa,
   *         {@code false} en caso contrario o si el enlace es nulo
   */
  public boolean difunde(Enlace e) {
    // Control errores
    if (e == null) {
      return false;
    }

    Usuario destino = e.getUsuarioDestino();
    if (puedeDifundirPor(e) &&
        aceptadoPor(destino) &&
        e.getUsuarioOrigen().equals(this.actual)) {

      // 1. El usuario actual pasa a ser el destino
      this.actual = destino;
      // 2. El alcance disminuye en el coste del enlace
      this.alcance -= e.getCoste();
      // 3. El alcance aumenta en la capacidad de amplificación del usuario destino
      this.alcance += destino.getCapacidadAmplificacion();

      this.alcance = (this.alcance < 0) ? 0 : this.alcance;
      return true;
    }
    return false;
  }

  /**
   * Intenta difundir el mensaje secuencialmente a través de una ruta de usuarios.
   * 
   * @param ruta secuencia variable de usuarios que conforman la ruta destino
   * @return {@code true} si el mensaje logró difundirse por toda la ruta con
   *         éxito,
   *         {@code false} en caso de error o si no pudo completar la ruta
   */
  public boolean difunde(Usuario... ruta) {
    // Control de errores
    if (ruta == null)
      return false;

    boolean exito = true;
    // Recorremos la lista de usuarios que es la ruta del mensaje
    for (Usuario siguiente : ruta) {
      // Vemos si el usuario siguiente tiene un enlace con el usuario actual
      Enlace enlace = this.actual.getEnlace(siguiente);
      if (enlace == null || !this.difunde(enlace)) {
        exito = false;
      }
    }
    return exito;
  }

  /**
   * Comprueba si el mensaje tiene alcance suficiente para difundirse por el
   * enlace dado.
   * 
   * @param e el enlace a evaluar
   * @return {@code true} si el coste del enlace es menor o igual al alcance
   *         actual,
   *         {@code false}
   *         en caso contrario
   */
  protected boolean puedeDifundirPor(Enlace e) {
    if (e.getCoste() <= this.alcance) {
      return true;
    }
    return false;
  }

  /**
   * Comprueba si el usuario destino acepta recibir el mensaje.
   * Diseñado para ser sobrescrito en clases hijas (ej. MensajeControlado).
   * 
   * @param u el usuario destino a evaluar
   * @return {@code true} por defecto para un mensaje estándar
   */
  protected boolean aceptadoPor(Usuario u) {
    return true;
  }

  /**
   * Devuelve la representación en cadena de texto del estado actual del mensaje.
   * 
   * @return cadena con el formato "Mensaje(texto:alcance) en @usuario"
   */
  @Override
  public String toString() {
    return "Mensaje(" +
        this.autor + ":" + this.alcance +
        ") en @" + this.actual.getNombre();
  }
}