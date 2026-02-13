package trayectos;

/**
 * Representa el tramo de trayecto que se realiza a pie.
 * 
 * @author Alejandro Seguido
 * @version 1.0
 */
public class TramoAPie extends TramoTrayecto {
  private Ritmo ritmo;
  private double km;

  /**
   * Establece de el ritmo por DEFECTO que haya establecido en Ritmo.
   * @param origen Origen del tramo.
   * @param destino Destino del tramo.
   * @param km Distancia en kilómetros del tramo.
   */
  public TramoAPie(String origen, String destino, double km) {
    super(origen, destino);
    this.km = km;
    this.ritmo = Ritmo.DEFECTO;
  }
  /**
   * Iniciliza todos los atributos del tramo.
   * @param origen Origen del tramo.
   * @param destino Destino del tramo.
   * @param km Distancia en kilómetros del tramo.
   * @param ritmo Ritmo al que marcha.
   */
  public TramoAPie(String origen, String destino, double km, Ritmo ritmo) {
    super(origen, destino);
    this.km = km;
    this.ritmo = ritmo;
  }

  /**
   * Obtien la duración del tramo.
   * @return Tiempo en minutos calculado según la distancia y el ritmo.
  */
  @Override
  public double tiempo() {
    return (double)(this.km * this.ritmo.getRitmo());
  }

  /**
   * Devuelve una representación textual del tramo a pie.
   * @return Cadena de caracteres con formato: A pie TRAMO_TRAYECTO RITMO_EN_TEXTO.
  */
  @Override
  public String toString() {
    return "A pie "+super.toString()+" "+this.ritmo;
  }
}