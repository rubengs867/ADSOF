package trayectos;
/**
 * Enumeración de líneas de tren con su color y tiempo por parada.
 * 
 * @author Alejandro Seguido y Ruben Garcia
 * @version 1.0
 */

public enum Linea {
  /** Línea C1: azul claro, 5 minutos por parada. */
  C1("azul claro", 5),
  /** Línea C4: azul oscuro, 10 minutos por parada. */
  C4("azul oscuro", 10),
  /** Línea C5: amarilla, 30 minutos por parada. */
  C5("amarilla", 30);

  private String color;
  private double tiempo;
  /**
   * Constructor del enumerado Linea.
   * Inicializa las constantes con su color identificativo y el tiempo por parada.
   * * @param color  El color asociado a la línea.
   * @param tiempo El tiempo en minutos que tarda el tren en recorrer una parada.
   */
  private Linea(String color, double tiempo){
    this.color = color;
    this.tiempo = tiempo;
  }
  /**
   * Devuelve el tiempo que tarda el tren en recorrer una parada de la línea.
   * @return Tiempo en minutos que tarda el tren en recorrer una parada de la línea.
   */
  public double getTiempo(){
    return this.tiempo;
  }
  /**
   * Devuelve una representación en forma de cadena de texto de la línea.
   * Sobrescribe el método por defecto para incluir el color.
   * * @return Cadena con el formato "NombreLinea (color)".
   */
  @Override
  public String toString(){
    return this.name() + " (" +this.color+ ")";
  }
}
