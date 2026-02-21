package trayectos;

/**
 * Clase principal que ejecuta y prueba la aplicación de trayectos de movilidad sostenible.
 * Define un trayecto compuesto por varios tramos y muestra su información por pantalla.
 * * @author Alejandro Seguido y Ruben Garcia
 * @version 1.0
 */
public class Main {

  /**
   * Constructor por defecto de la clase Main.
   * Se incluye de forma explícita para poder añadir su correspondiente documentación Javadoc.
   */
  public Main() {
  }

  /**
   * Método principal y punto de entrada de la aplicación.
   * @param args Argumentos de la línea de comandos (no se utilizan en este programa).
   */
  public static void main(String[] args) {
    TramoTrayecto[] trayecto = {
      new TramoAPie("Hotel Puerta del Sol", "Sol Renfe", 1),
      new TramoTren("Sol Renfe", "Cantoblanco Renfe", Linea.C4, 4),
      new TramoAPie("Cantoblanco Renfe", "EPS", 2.6, Ritmo.RAPIDO),
    };

    for (TramoTrayecto tramo : trayecto) {
      System.out.println(tramo);
    }
    
    // Corregido: trayecto[1] es el TramoTren de la C4, trayecto[0] era el TramoAPie
    System.out.println("Tiempo de la línea C4: " + trayecto[1].tiempo() + " minutos");
  }
}