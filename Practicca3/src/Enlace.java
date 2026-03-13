/**
 * Un enlace define un camino por el cual un mensaje puede viajar de un usuario
 * origen a un usuario destino, con un coste asociado que reduce el alcance del
 * mensaje.
 * Además, mantiene un registro estático del coste total de todos los enlaces
 * creados.
 */
public class Enlace {

  /** Usuario que envía el mensaje a través del enlace. */
  private Usuario origen;

  /** Usuario que recibe el mensaje a través del enlace. */
  private Usuario destino;

  /** Alcance necesario para atravesar este enlace. */
  private int coste;

  /** Contador estático que acumula el coste de todos los enlaces instanciados. */
  private static int costeTotal = 0;

  /**
   * Construye un nuevo enlace entre dos usuarios con un coste específico.
   * Si el coste proporcionado es menor o igual a 0, se asignará un valor por
   * defecto de 1.
   * 
   * @param origen  el usuario origen del enlace
   * @param destino el usuario destino del enlace
   * @param coste   el coste asociado al enlace (debe ser mayor que 0)
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
   * Constructor por defecto que establece una conexión con coste 1.
   * 
   * @param origen  el usuario origen del enlace
   * @param destino el usuario destino del enlace
   */
  public Enlace(Usuario origen, Usuario destino) {
    this(origen, destino, 1);
  }

  /**
   * Modifica el destino y el coste de un enlace existente, actualizando
   * proporcionalmente el contador de coste total acumulado.
   * 
   * @param nuevoDestino el nuevo usuario destino para este enlace
   * @param nuevoCoste   el nuevo coste a aplicar (si es menor o igual a 0, se
   *                     pondrá 1)
   */
  public void cambiarDestino(Usuario nuevoDestino, int nuevoCoste) {
    this.destino = nuevoDestino;
    int costeOld = this.coste;

    if (nuevoCoste <= 0) {
      this.coste = 1;
    } else {
      this.coste = nuevoCoste;
    }

    // Actualizamos costeTotal restando el viejo y sumando el nuevo
    costeTotal = costeTotal + (this.coste - costeOld);
  }

  /**
   * Obtiene la suma de los costes base de todos los enlaces activos en el
   * sistema.
   * 
   * @return el coste total acumulado de forma estática
   */
  public static int getCosteTotalAcumulado() {
    return costeTotal;
  }

  /**
   * Obtiene el usuario origen.
   * 
   * @return el objeto usuario desde donde parte el enlace
   */
  public Usuario getUsuarioOrigen() {
    return this.origen;
  }

  /**
   * Obtiene el usuario destino.
   * 
   * @return el objeto usuario al que llega el enlace
   */
  public Usuario getUsuarioDestino() {
    return this.destino;
  }

  /**
   * Obtiene el coste base del enlace.
   * 
   * @return el valor entero del coste
   */
  public int getCoste() {
    return this.coste;
  }

  /**
   * Representa un coste adicional calculado según el tipo de enlace.
   * 
   * @return el valor del coste especial (0 en la implementación base)
   */
  public int costeEspecial() {
    return 0;
  }

  /**
   * Calcula el coste total real del enlace sumando el coste base y el especial.
   * 
   * @return el coste total resultante de la operación
   */
  public int costeReal() {
    return this.coste + this.costeEspecial();
  }

  /**
   * Devuelve una representación textual del enlace.
   * 
   * @return una cadena con el formato (@origen--coste-->@destino)
   */
  @Override
  public String toString() {
    return "(@" + this.origen.getNombre() +
        "--" + this.coste + "-->@" +
        this.destino.getNombre() + ")";
  }
}