package trayectos;

public class TramoTren extends TramoTrayecto {
  private Linea linea;
  private int numParadas;

  public TramoTren(String origen, String destino, Linea linea, int numParadas) {
    super(origen, destino);
    this.linea = linea;
    this.numParadas = numParadas;
  }
  @Override
  public int tiempo(){
    return this.linea.getTiempo() * (this.numParadas);
  }

  @Override
  public String toString() {
    return "En tren de la línea "+this.linea+" "+super.toString();
  }
}