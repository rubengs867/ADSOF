public class EnlaceSenuelo extends Enlace {

  private int factorCosteExtra;
  private double probRetorno;

  public EnlaceSenuelo(Usuario origen, Usuario destino,
      int coste, int factorCosteExtra,
      double probRetorno) {
    super(origen, destino, coste);
    this.factorCosteExtra = factorCosteExtra;
    this.probRetorno = probRetorno;
  }

  /**
   * En lugar de cambiar el getCoste(), sobrescribimos el costeEspecial
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