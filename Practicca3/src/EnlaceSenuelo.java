public class EnlaceSenuelo extends Enlace {

  private int factorCosteExtra;
  private double probRetorno;

  /**
   * Constructor del enlace senuelo
   * 
   * @param origen
   * @param destino
   * @param coste
   * @param factorCosteExtra
   * @param probRetorno
   */
  public EnlaceSenuelo(Usuario origen, Usuario destino,
      int coste, int factorCosteExtra,
      double probRetorno) {
    super(origen, destino, coste);
    this.factorCosteExtra = factorCosteExtra;
    this.probRetorno = probRetorno;
  }

  /**
   * En lugar de cambiar el getCoste(), sobrescribimos el costeEspecial
   * en la clase padre vale 0, pero en esta clase tiene su propia formula
   */
  @Override
  public int costeEspecial() {
    // coste especial = coste original * factor extra
    return this.getCoste() * this.factorCosteExtra;
  }

  /**
   * Mantenemos la lógica de la probabilidad de retorno.
   */
  @Override
  public Usuario getUsuarioDestino() {
    if (Math.random() < this.probRetorno) {
      return this.getUsuarioOrigen();
    }
    return super.getUsuarioDestino();
  }
}