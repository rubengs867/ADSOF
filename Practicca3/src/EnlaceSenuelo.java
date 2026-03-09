public class EnlaceSenuelo extends Enlace {
  
  private int factor_coste_extra;
  private double prob_retorno;

  public EnlaceSenuelo(Usuario origen, Usuario destino, int coste, int factor_coste_extra, double prob_retorno){
    super(origen, destino, coste);
    this.factor_coste_extra = factor_coste_extra;
    this.prob_retorno = prob_retorno;
  }

  /**
   * En lugar de cambiar el getCoste(), sobrescribimos el costeEspecial 
   */
  @Override
  public int costeEspecial() {
    // coste especial = coste original * factor extra
    return this.getCoste() * this.factor_coste_extra;
  }

  /**
   * Mantenemos la lógica de la probabilidad de retorno.
   */
  @Override
  public Usuario getUsuarioDestino() {
    if (Math.random() < this.prob_retorno) {
      return this.getUsuarioOrigen(); 
    }
    return super.getUsuarioDestino();
  }
}