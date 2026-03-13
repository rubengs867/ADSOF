/* Esta clase guarda el mensaje, en que usuario esta actualmente y cuanto alcance tiene */

public class Mensaje {
  private String autor;
  private Usuario actual;
  private int alcance;

  /**
   * 
   * @param autor   el string del mensaje
   * @param alcance el alcance del mensaje
   * @param usuario el usuario en el que se encuentra el mensaje
   */
  public Mensaje(String autor, int alcance, Usuario usuario) {
    this.autor = autor;
    this.actual = usuario;
    this.alcance = alcance;
  }

  /**
   * 
   * @param actual actualiza el usuario en el que se encuentra el mensaje
   */

  public void setUsuarioActual(Usuario actual) {
    this.actual = actual;
  }

  /**
   * actualiza el alcance del mensaje
   * 
   * @param alcance
   */
  public void setAlcanceActual(int alcance) {
    this.alcance = alcance;
  }

  /**
   * devuelve el usuario en el que se encuentra el mensaje
   * 
   * @return
   */
  public Usuario getUsuarioActual() {
    return this.actual;
  }

  /**
   * devuelve el autor del mensaje
   * 
   * @return
   */
  public String getUsuarioAutor() {
    return this.autor;
  }

  /**
   * devuelve el alcance del mensaje
   * 
   * @return
   */
  public int getAlcanceActual() {
    return this.alcance;
  }

  /**
   * Intenta difundir el mensaje por el enlace e, actualizando el usuario actual
   * y el alcance del mensaje en caso de éxito.
   * 
   * @param e
   * @return
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
   * intenta difundir el mensaje por la ruta dada, devolviendo true si se ha
   * difundido con éxito por toda la ruta o false en caso contrario
   * 
   * @param ruta
   * @return
   */
  public boolean difunde(Usuario... ruta) {
    // Control de errores
    if (ruta == null)
      return false;

    Boolean exito = true;
    // Recorremos la lista de usuarios que es la ruta del mensaje
    for (Usuario siguiente : ruta) {
      // Vemos si el usuario siguiente tiene un enlace con el usuario actual
      Enlace enlace = this.actual.getEnlace(siguiente);
      if (enlace == null || this.difunde(enlace) == false) {
        exito = false;
      }
    }
    return exito;
  }

  /**
   * comprueba si el mensaje puede difundirse por el enlace e, es decir,
   * si el coste del enlace es menor o igual que el alcance actual del mensaje.
   * 
   * @param e
   * @return
   */
  protected boolean puedeDifundirPor(Enlace e) {
    if (e.getCoste() <= this.alcance) {
      return true;
    }
    return false;
  }

  /**
   * comprueba si el usuario u ha aceptado el mensaje
   * 
   * @param u
   * @return
   */
  protected boolean aceptadoPor(Usuario u) {
    return true;
  }

  @Override
  public String toString() {
    return "Mensaje(" +
        this.autor + ":" + this.alcance +
        ") en @" + this.actual.getNombre();
  }
}
