package trayectos;

/**
 * Enumeración de valores de ritmo para calcular trayectos a pie.
 * 
 * @author Alejandro Seguido y Ruben Garcia
 * @version 1.0
 */
public enum Ritmo{
  /** Ritmo suave: tarda 15 minutos en recorrer 1km. */
  SUAVE(15),
  /** Ritmo moderado: tarda 10 minutos en recorrer 1km. */
  MODERADO(10),
  /** Ritmo rápido: tarda 8 minutos en recorrer 1km. */
  RAPIDO(8);

  /** Minutos que tarda en recorrer 1km a este ritmo. */
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