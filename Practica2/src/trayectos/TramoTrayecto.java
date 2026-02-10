package trayectos;
public abstract class TramoTrayecto {
  private String origen;
  private String destino;
  /**Constructor del tramo, recordemos que es una clase abstracta y que sus hijos la invocan con la linea super
  *@param origen Origen del tramo.
  *@param destino Destino del tramo.
  */
  public TramoTrayecto (String origen, String destino){
    this.origen = origen;
    this.destino = destino;
  }
  /*Este es un método abstracto. Esta funcion llama las funciones de sus clases hijos que se llamen igual*/
  public abstract double tiempo();

  @Override
  public String toString(){
    return "desde "+this.origen+ " a "+this.destino+ ": "+this.tiempo() + " minutos";
  }
}
