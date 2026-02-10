package trayectos;

public enum Linea {
  C1("azul claro", 5),
  C4("azul oscuro", 10),
  C5("amarilla", 30);

  private String color;
  private double tiempo;
  /**
   *En este constructor le pasamos tanto el color como el tiempo. Por tanto el enum contiene dos piezas de información */
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
  public String toString(){
    return this.name() + " (" +this.color+ ")";
  }
}
