/**
 * Representa un tipo especial de enlace llamado "señuelo" dentro de la red
 * social.
 * Este enlace hereda de la clase {@link Enlace}.
 */
public class EnlaceSenuelo extends Enlace {

  /** Multiplicador utilizado para calcular el coste especial del enlace. */
  private int factorCosteExtra;

  /**
   * Probabilidad (valor entre 0 - 1) de que el enlace devuelva el mensaje al
   * origen.
   */
  private double probRetorno;

  /**
   * Construye un nuevo enlace señuelo con los parámetros especificados.
   * 
   * @param origen           el usuario del que parte el enlace
   * @param destino          el usuario al que teóricamente se dirige el enlace
   * @param coste            el coste base de atravesar el enlace
   * @param factorCosteExtra el factor por el cual se multiplicará el coste base
   *                         para el coste especial
   * @param probRetorno      la probabilidad (entre 0 - 1) de que el enlace
   *                         retorne al origen
   */
  public EnlaceSenuelo(Usuario origen, Usuario destino,
      int coste, int factorCosteExtra,
      double probRetorno) {
    super(origen, destino, coste);
    this.factorCosteExtra = factorCosteExtra;
    this.probRetorno = probRetorno;
  }

  /**
   * Calcula el coste especial que aplica este enlace señuelo.
   * Se obtiene multiplicando el coste base por el factor extra.
   * 
   * @return el coste especial calculado para este enlace
   */
  @Override
  public int costeEspecial() {
    // coste especial = coste original * factor extra
    return this.getCoste() * this.factorCosteExtra;
  }

  /**
   * Obtiene el usuario destino al que conducirá este enlace al ser atravesado.
   * Evalúa aleatoriamente si el mensaje avanza hacia el destino real o
   * rebota hacia el usuario de origen.
   * 
   * @return el usuario de origen si se cumple la probabilidad de retorno,
   *         o el usuario de destino real en caso contrario
   */
  @Override
  public Usuario getUsuarioDestino() {
    if (Math.random() < this.probRetorno) {
      return this.getUsuarioOrigen(); // El señuelo hace su efecto y rebota
    }
    return super.getUsuarioDestino(); // Avanza normalmente
  }
}