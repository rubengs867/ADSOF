public class EjemploPruebaEnlace {

  public static void main(String[] args) {
    
    Usuario madrid = new Usuario("madrid", 5);
    Usuario tokio = new Usuario("tokio", 5);

    System.out.println("\nPRUEBA 1: ENLACE NORMAL");
    Enlace normal = new Enlace(madrid, tokio, 10);
    
    System.out.println("Coste base: " + normal.getCoste());
    System.out.println("Coste especial: " + normal.costeEspecial()); // Debería ser 0
    System.out.println("Coste real total: " + normal.costeReal());   // Debería ser 10
    System.out.println("Destino: @" + normal.getUsuarioDestino().getNombre());

    System.out.println("\n=== PRUEBA 2: ENLACE SEÑUELO (Cálculo de costes) ===");
    // Creamos señuelo: coste base 10, factor extra 3, probabilidad engaño 0.8 (80%)
    EnlaceSenuelo senuelo = new EnlaceSenuelo(madrid, tokio, 10, 3, 0.8);
    
    System.out.println("Coste base original: " + senuelo.getCoste());          // Debería ser 10
    System.out.println("Coste especial (10 * 3): " + senuelo.costeEspecial()); // Debería ser 30
    System.out.println("Coste real total (10 + 30): " + senuelo.costeReal());  // Debería ser 40

    System.out.println("\nPRUEBA 3: ENLACE SEÑUELO (Simulación de engaño al 80%)");
    int retornosOrigen = 0;
    int retornosDestino = 0;
    
    // Hacemos 100 peticiones de destino para ver la estadística real
    for (int i = 0; i < 100; i++) {
      Usuario destinoObtenido = senuelo.getUsuarioDestino();
      if (destinoObtenido == madrid) {
        retornosOrigen++;  // Nos ha engañado y devuelto al origen
      } else {
        retornosDestino++; // Se ha portado bien y ha devuelto el destino real
      }
    }
    
    System.out.println("Resultados tras 100 intentos de cruzar el enlace:");
    System.out.println(" -> Veces que ENGAÑÓ (devolvió origen @madrid): " + retornosOrigen + " (Aprox. 80)");
    System.out.println(" -> Veces que fue NORMAL (devolvió destino @tokio): " + retornosDestino + " (Aprox. 20)");
  }
}