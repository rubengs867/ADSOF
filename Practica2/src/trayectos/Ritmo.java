package trayectos;

/**
 * Enumeración de valores de ritmo para calcular trayectos a pie.
 * 
 * @author Alejandro Seguido
 * @version 1.0
 */
public enum Ritmo{
  SUAVE(15),
  MODERADO(10),
  RAPIDO(8);

  private double ritmo;

  /**
   * Inicializa el ritmo.
   * @param ritmo Minutos que tarda en recorrer 1km.
   */
  private Ritmo(double ritmo) {
    this.ritmo = ritmo;
  }

  /**
   * Obtiene el ritmo a pie.
   * @return Tiempo en minutos que tarda en recorrer 1km. 
   */
  public double getRitmo() {
    return (double)this.ritmo;
  }

  /**
   * Devuelve una representación textual del ritmo.
   * @return Cadena de caracteres con formato: (ritmo NOMBRE_RITMO).
   */
  @Override
  public String toString() {
    return "(ritmo "+this.name()+")";
  }
}