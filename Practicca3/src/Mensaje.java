public class Mensaje {
  private Usuario autor;
  private Usuario actual;
  private int alcance;

  public Mensaje(Usuario autor, Usuario actual, int alcance){
    this.autor = autor;
    this.actual = actual;
    this.alcance = alcance;
  }

  public void setUsuarioActual(Usuario actual){
    this.actual = actual;
  }

  public void setAlcanceActual(int alcance){
    this.alcance = alcance;
  }
  public Usuario getUsuarioActual(){
    return this.actual;
  }

  public Usuario getUsuarioAutor(){
    return this.autor;
  }

  public int getAlcanceActual(){
    return this.alcance;
  }

  public boolean difunde(Enlace e){
    Usuario destino = e.getUsuarioDestino(); // Usa el método correcto de tu clase Enlace
    
    if(puedeDifundirPor(e) && aceptadoPor(destino)){
      // 1. El usuario actual pasa a ser el destino
      this.actual = destino;
      
      // 2. El alcance disminuye en el coste (usando el método que te dé el coste)
      this.alcance -= e.getCoste(); 
      
      // 3. El alcance aumenta según la capacidad de amplificación del destino
      //this.alcance += destino.getCapacidadAmplificacion(); // Usa el método correcto de tu clase Usuario
      
      return true;
    }
    return false;
  }

  private boolean puedeDifundirPor(Enlace e){
    if(e.getCoste() <= this.alcance){
      return true;
    }
    return false;
  }
  
  private boolean aceptadoPor(Usuario u){
    return true;
  }

  @Override
  public String toString(){
    return "Mensaje(m:" + this.alcance + ") en @" +this.actual;
  }
}
