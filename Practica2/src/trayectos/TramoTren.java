package trayectos;
/**
 * Clase que representa un tramo de trayecto que se realiza en tren.
 * Hereda de {TramoTrayecto} y calcula el tiempo de viaje basándose
 * en el número de paradas y el tiempo por parada de la línea correspondiente.
 */

public class TramoTren extends TramoTrayecto {
  private Linea linea;
  private int numParadas;
  /**
   * Inicializa el tramo de tren con el origen, destino, línea y número de paradas, llamando al constructor de la clase padre con la línea super.
   * @param origen Origen del tramo.
   * @param destino Destino del tramo.
   * @param linea Línea de tren del tramo.
   * @param numParadas Número de paradas en el tramo.
   */
  public TramoTren(String origen, String destino, Linea linea, int numParadas) {
    super(origen, destino);
    this.linea = linea;
    this.numParadas = numParadas;
  }
  /**
   * Calcula el tiempo total del tramo en tren.
   * Implementa el método abstracto de la clase padre multiplicando el tiempo 
   * que tarda la línea por parada por el número de paradas del tramo.
   * * @return Tiempo total del tramo en minutos.
   */
  @Override
  public double tiempo(){
    return (double)this.linea.getTiempo() * (this.numParadas);
  }

  /**
   * Devuelve una cadena de texto con la información detallada del tramo en tren.
   * Concatena la información de la línea con la representación base de la clase padre.
   * * @return Cadena descriptiva del tramo en tren.
   */
  @Override
  public String toString() {
    return "En tren de la línea "+this.linea+" "+super.toString();
  }
}