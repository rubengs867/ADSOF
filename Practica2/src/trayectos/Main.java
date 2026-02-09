package trayectos;

public class Main {
  public static void main(String[] args) {
    TramoTrayecto[] trayecto = {
      new TramoTren("Sol Renfe", "Cantoblanco Renfe", Linea.C4, 4),
      new TramoAPie("Cantoblanco Renfe", "EPS", 2.6, Ritmo.RAPIDO),
    };


    for (TramoTrayecto tramo: trayecto)
      System.out.println(tramo);
  System.out.println("Tiempo de la línea C4: "+trayecto[0].tiempo()+" minutos");
  }
}
