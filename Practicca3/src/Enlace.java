/* Esta clase contiene la informacion del enlace entre dos usuarios */

public class Enlace {
  /* Usuario origen */
  private Usuario origen;
  /* Usuario destino */
  private Usuario destino;
  private int coste;
  private static int costeTotal = 0;

  /**
   * Constructor
   * En caso del que el coste sea menor o igual a 0, se asigna un coste por
   * defecto de 1
   * 
   * @param origen  Usuario origen del enlace
   * @param destino Usuario destino del enlace
   * @param coste   Coste del enlace
   */
  public Enlace(Usuario origen, Usuario destino, int coste) {
    this.origen = origen;
    this.destino = destino;
    if (coste <= 0) {
      this.coste = 1;
    } else {
      this.coste = coste;
    }
    costeTotal += this.coste;
  }

  /**
   * Constructor por defecto, asigna un coste de 1
   * 
   * @param origen  Usuario origen del enlace
   * @param destino Usuario destino del enlace
   */
  public Enlace(Usuario origen, Usuario destino) {
    this(origen, destino, 1);
  }

  public void cambiarDestino(Usuario nuevoDestino, int nuevoCoste) {
    this.destino = nuevoDestino;
    // Aplicamos la misma regla de coste <= 0
    if (nuevoCoste <= 0) {
      this.coste = 1;
    } else {
      this.coste = nuevoCoste;
    }

  }

  public static int getCosteTotalAcumulado() {
    return costeTotal;
  }

  /**
   * 
   * @return El usuario origen del enlace
   */
  public Usuario getUsuarioOrigen() {
    return this.origen;
  }

  /**
   * 
   * @return El usuario destino del enlace
   */
  public Usuario getUsuarioDestino() {
    return this.destino;
  }

  /**
   * 
   * @return El coste del enlace
   */
  public int getCoste() {
    return this.coste;
  }

  public int costeEspecial() {
    return 0;
  }

  public int costeReal() {
    return this.coste + this.costeEspecial();
  }

  /**
   * @return Una cadena de texto con la información del enlace, en el formato:
   *         (@origen--coste-->@destino)
   */
  @Override
  public String toString() {

    return "(@" + this.origen + "  " + this.coste + "-->@" + this.destino + ")";
  }

}
