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
   * @param alcance el nuevo alcance del mensaje
   */
  public void setAlcanceActual(int alcance) {
    this.alcance = alcance;
  }

  /**
   * devuelve el usuario en el que se encuentra el mensaje
   * 
   * @return devuelve el usuario donde está actualmente el mensaje
   */
  public Usuario getUsuarioActual() {
    return this.actual;
  }

  /**
   * devuelve el autor del mensaje
   * 
   * @return String con el mensaje, en la practica se llama autor pero se refiere al contenido
   */
  public String getUsuarioAutor() {
    return this.autor;
  }

  /**
   * devuelve el alcance del mensaje
   * 
   * @return el alcance del mensaje
   */
  public int getAlcanceActual() {
    return this.alcance;
  }

  /**
   * Intenta difundir el mensaje por el enlace e, actualizando el usuario actual
   * y el alcance del mensaje en caso de éxito.
   * 
   * @param e enlace
   * @return devuelve true si todo ha ido bien
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
   * @param ruta la ruta es un numero variable de usuarios, por eso los 3 puntos
   * @return true si todo ha ido correctamente, false en caso de error
   */
  public boolean difunde(Usuario... ruta) {
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
   * @param e enlace
   * @return devuelve true si se puede difundir
   */
  protected boolean puedeDifundirPor(Enlace e) {
    if (e.getCoste() <= this.alcance) {
      return true;
    }
    return false;
  }

  /**
   * comprueba si el usuario u ha aceptado el mensaje
   * como se sobre escribe en el mensaje controlado usamos protected ya que solo se utiliza dentro de esta herencia
   * @param u usuario
   * @return en este caso siempre true
   */
  protected boolean aceptadoPor(Usuario u) {
    return true;
  }

  /**
   * La composicion del mensaje
   * @return String con el formato del mensaje
   */
  @Override
  public String toString() {
    return "Mensaje(" +
        this.autor + ":" + this.alcance +
        ") en @" + this.actual.getNombre();
  }
}
