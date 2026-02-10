package trayectos;

public class TramoTren extends TramoTrayecto {
  private Linea linea;
  private int numParadas;
  /**
   * Inicializa el tramo de tren con el origen, destino, línea y número de paradas, llamando al constructor de la clase padre con la línea super.
   * @param origen
   * @param destino
   * @param linea
   * @param numParadas
   */
  public TramoTren(String origen, String destino, Linea linea, int numParadas) {
    super(origen, destino);
    this.linea = linea;
    this.numParadas = numParadas;
  }
  /*Viene de un método abstracto y esta es la función concreta que se encarga de devolver el tiempo*/
  @Override
  public double tiempo(){
    return (double)this.linea.getTiempo() * (this.numParadas);
  }

  @Override
  public String toString() {
    return "En tren de la línea "+this.linea+" "+super.toString();
  }
}