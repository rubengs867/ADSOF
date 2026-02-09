package trayectos;

public class TramoAPie extends TramoTrayecto {
  private Ritmo ritmo;
  private double km;

  public TramoAPie(String origen, String destino, double km) {
    super(origen, destino);
    this.km = km;
    this.ritmo = Ritmo.MODERADO;
  }

  public TramoAPie(String origen, String destino, double km, Ritmo ritmo) {
    super(origen, destino);
    this.km = km;
    this.ritmo = ritmo;
  }

  public double tiempo() {
    return (this.km * (double)this.ritmo);
  }
}