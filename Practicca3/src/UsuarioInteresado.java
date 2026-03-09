public class UsuarioInteresado extends Usuario{

  //como detalle arraylist mantiene el orden de insercion de los datos

  public UsuarioInteresado(String nombre, int capacidadAmplificacion) {
    super(nombre, capacidadAmplificacion);
  }

  public UsuarioInteresado(String nombre, int capacidadAmplificacion, Exposicion exposicion) {
    super(nombre, capacidadAmplificacion, exposicion);
  }

  public UsuarioInteresado(String nombre) {
    super(nombre);
  }

  @Override
  public Enlace getEnlace(Usuario destUsuario) {

    for (int i = 0; i < this.getNumEnlaces(); i++) {
      Enlace enlaceActual = this.getEnlace(i);
      Usuario destino = enlaceActual.getUsuarioDestino();
      
     
      Exposicion exp = destino.getExposicion(); 
      
      if (exp == Exposicion.ALTA || exp == Exposicion.VIRAL) {
        return enlaceActual; 
      }
    }

    return super.getEnlace(destUsuario);
  }
}
