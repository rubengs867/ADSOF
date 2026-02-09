package trayectos;

public enum Ritmo{
  SUAVE(15),
  MODERADO(10),
  RAPIDO(8);

  private int ritmo;

  private Ritmo(int ritmo) {
    this.ritmo = ritmo;
  }

  @Override
  public String toString() {
    return "(ritmo "+ this.name()+")";
  }
}