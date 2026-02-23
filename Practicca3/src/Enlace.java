public class Enlace {
  private Usuario origen;
  private Usuario destino;
  private int coste;
  static int costeTotal= 0;
  public Enlace(Usuario origen, Usuario destino, int coste){
    this.origen = origen;
    this.destino = destino;
    if(coste <= 0){
      this.coste = 1;
    }else{
      this.coste = coste;
    }
    costeTotal += coste;
  }

  public Enlace(Usuario origen, Usuario destino){
    this(origen, destino, 1);
  }

  public Usuario getUsuarioOrigen(){
    return this.origen;  
  }
  public Usuario getUsuarioDestino(){
    return this.destino;  
  }
  public int getCoste(){
    return this.coste;
  }

  @Override
  public String toString(){
    return "(@" + this.origen + "--" + this.coste + "-->@" + this.destino + ")";
  }

}
