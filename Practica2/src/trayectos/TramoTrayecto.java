package trayectos;

/**
 * Clase abstracta que representa un tramo genérico dentro de un trayecto.
 * Define la estructura básica (origen y destino) que heredarán todos los tipos de tramos.
 */
public abstract class TramoTrayecto {
  private String origen;
  private String destino;

  /**
   * Constructor del tramo. 
   * Al ser una clase abstracta, este constructor es invocado por sus clases hijas mediante la instrucción super().
   * @param origen  Origen del tramo.
   * @param destino Destino del tramo.
   */
  public TramoTrayecto(String origen, String destino) {
    this.origen = origen;
    this.destino = destino;
  }

  /**
   * Método abstracto para calcular la duración del tramo.
   * Obliga a las clases hijas (como TramoTren o TramoAPie) a implementar 
   * su propia lógica para calcular el tiempo según el tipo de transporte.
   * @return El tiempo en minutos que se tarda en recorrer el tramo.
   */
  public abstract double tiempo();

  /**
   * Devuelve una representación en forma de cadena de texto del tramo.
   * Utiliza el método abstracto tiempo() para incluir la duración calculada.
   * @return Cadena con el formato "desde [origen] a [destino]: [tiempo] minutos".
   */
  @Override
  public String toString() {
    return "desde " + this.origen + " a " + this.destino + ": " + this.tiempo() + " minutos";
  }
}