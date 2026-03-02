public class Mensaje {
  private String autor;
  private Usuario actual;
  private int alcance;

  public Mensaje(String autor, int alcance, Usuario usuario){
    this.autor = autor;
    this.actual = usuario;
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

  public String getUsuarioAutor(){
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
      
      this.alcance += destino.getCapacidadAmplificacion(); 
      
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

  public boolean difunde (Usuario ... ruta){
    Boolean exito = true;

    for (Usuario siguiente : ruta){
      Enlace enlace = this.actual.getEnlace(siguiente); /* cogemos el objeto de la clase mensaje, y vemos a que usuario apunta actualmente */

      if(enlace != null && this.difunde(enlace)){

      }
      else{
        exito = false;
      }
    }
    return exito;
    
  }

  @Override
  public String toString(){
    return "Mensaje(" + this.autor + this.alcance + ") en @" +this.actual.getNombre();
  }
}
