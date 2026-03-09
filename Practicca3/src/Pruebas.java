/**
 * Clase de pruebas para comprobar el funcionamiento de:
 * - Enlace
 * - Usuario
 * - Mensaje
 */
public class Pruebas {

  public static void main(String[] args) {

    // Variable auxiliar para comprobar el resultado de métodos booleanos
    boolean ret;

    // Creación de usuarios con distinta capacidad de amplificación
    Usuario ana = new Usuario("ana", 31);
    Usuario luis = new Usuario("luis", 85);
    Usuario carmen = new Usuario("carmen");
    Usuario jose = new Usuario("jose");

    // Creación de enlaces entre usuarios
    Enlace e1 = new Enlace(ana, luis);
    Enlace e = null;

    System.out.println("\n============== Pruebas Enlaces ===================\n");
    System.out.println("Impresión de atributos");

    // Pruebas de los métodos getter del enlace
    System.out.println("Enlace 1");
    System.out.println("Usuario origen: " + e1.getUsuarioOrigen());
    System.out.println("Usuario destino: " + e1.getUsuarioDestino());
    System.out.println("Coste: " + e1.getCoste());
    System.out.println("Coste acumulado: " + Enlace.getCosteTotalAcumulado());
    System.out.println("Coste especial: " + e1.costeEspecial());
    System.out.println("Coste real (Coste + Coste especial): " + e1.costeReal());

    System.out.println("\nImpresión enlaces y comprobación de coste acumulado");

    // Impresión del enlace mediante toString()
    System.out.println(e1.toString() + "\n");

    // Creación de más enlaces
    Enlace e2 = new Enlace(carmen, luis, 4);
    System.out.println(e2.toString());

    // Comprobación de que el coste acumulado se actualiza
    System.out.println("Coste acumulado: " + Enlace.getCosteTotalAcumulado() + "\n");

    Enlace e3 = new Enlace(luis, ana, -1);
    System.out.println(e3.toString());
    System.out.println("Coste acumulado: " + Enlace.getCosteTotalAcumulado() + "\n");

    System.out.println("\nCambiar destino enlace 3 " +
        "y su coste");

    // Cambio del destino del enlace e3 y su coste
    e3.cambiarDestino(carmen, 0);
    System.out.println("Nuevo destino: " + e3.getUsuarioDestino().getNombre());
    System.out.println(e3.toString());

    e3.cambiarDestino(luis, -1);
    System.out.println("Nuevo destino: " + e3.getUsuarioDestino().getNombre());
    System.out.println(e3.toString());

    e3.cambiarDestino(carmen, 10);
    System.out.println("Nuevo destino: " + e3.getUsuarioDestino().getNombre());
    System.out.println(e3.toString());

    // Comprobar actualización del coste acumulado
    System.out.println("Coste acumulado: " + Enlace.getCosteTotalAcumulado() + "\n");

    System.out.println("\n============== Pruebas Usuarios ===================\n");

    // Pruebas de los métodos getter del usuario
    System.out.println("Impresion atributos");
    System.out.println("Nombre: " + ana.getNombre());
    System.out.println("Capacidad de amplificación: " + ana.getCapacidadAmplificacion());
    System.out.println("Número enlaces: " + ana.getNumEnlaces());

    System.out.println("\nPruebas de enlaces en usuario origen Ana (false = ERROR)");

    // Pruebas de inserción de enlaces en el usuario
    ret = ana.addEnlace(e1);
    System.out.println("Añado enlace Ana -> Luis: " + ret);

    ret = ana.addEnlace(carmen, 2);
    System.out.println("Añado enlace Ana -> Carmen: " + ret);

    // Añadir enlace con destino duplicado (debería fallar)
    ret = ana.addEnlace(carmen, 20);
    System.out.println("Añado enlace Ana -> Carmen: " + ret);

    // Prueba de enlace a sí mismo (debería fallar)
    ret = ana.addEnlace(ana, 2);
    System.out.println("Añado enlace Ana -> Ana: " + ret);

    // Añadir enlace cuyo origen no es Ana (debería fallar)
    ret = ana.addEnlace(e2);
    System.out.println("Añado enlace Carmen -> Luis: " + ret);

    // Añadir enlace cuyo origen no es Ana, pero su destino si (debería fallar)
    ret = ana.addEnlace(e3);
    System.out.println("Añado enlace Luis -> Ana: " + ret + "\n");

    // Añadir enlace null (debería fallar)
    ret = ana.addEnlace(null);
    System.out.println("Añado enlace null: " + ret + "\n");

    // Pruebas de obtención de enlaces por usuario destino
    e = ana.getEnlace(carmen);
    System.out.println("Obtener enlace con destino Carmen: " +
        ((e == null) ? "ERROR" : e.toString()));

    e = ana.getEnlace(jose);
    System.out.println("Obtener enlace con destino Jose (ERROR): " +
        ((e == null) ? "ERROR" : e.toString()));

    e = ana.getEnlace(null);
    System.out.println("Obtener enlace con destino null (ERROR): " +
        ((e == null) ? "ERROR" : e.toString()));

    // Obtener enlaces por índice
    e = ana.getEnlace(0);
    System.out.println("Enlace con índice 0: " +
        ((e == null) ? "ERROR" : e.toString()));

    e = ana.getEnlace(1);
    System.out.println("Enlace con índice 1: " +
        ((e == null) ? "ERROR" : e.toString()));

    e = ana.getEnlace(2);
    System.out.println("Enlace con índice 2 (ERROR): " +
        ((e == null) ? "ERROR" : e.toString()));

    e = ana.getEnlace(-1);
    System.out.println("Enlace con índice -1 (ERROR): " +
        ((e == null) ? "ERROR" : e.toString()));

    System.out.println("Número enlaces: " + ana.getNumEnlaces());

    // Pruebas del método toString de Usuario
    System.out.println("\nPrueba toString");
    System.out.println("Usuario con varios enlaces:");
    ret = carmen.addEnlace(ana, 8);
    System.out.println("Añado enlace: " + carmen.getEnlace(ana));
    System.out.println(ana.toString());
    System.out.println("\nUsuario con 1 enlace: ");
    carmen.addEnlace(e2);
    System.out.println(carmen.toString());

    luis.addEnlace(e3);
    System.out.println(luis.toString());
    System.out.println("\nUsuario con 0 enlaces: ");
    System.out.println(jose.toString());

    System.out.println("\n============== Pruebas Mensajes ===================\n");

    // Creación de un mensaje
    Mensaje m = new Mensaje("¿Qué tal?", 10, carmen);

    // Pruebas de getters
    System.out.println("Imprimir mensaje.\n" + m);
    System.out.println("\nImprimir atributos.");
    System.out.println("Texto: " + m.getUsuarioAutor());
    System.out.println("Usuario actual: " + m.getUsuarioActual());
    System.out.println("Alcance: " + m.getAlcanceActual());

    // Pruebas de setters
    m.setUsuarioActual(luis);
    System.out.println("\nSet Usuario Actual - Luis: " + m);

    m.setAlcanceActual(33);
    System.out.println("Set Alcance - 33: " + m);

    // Pruebas del método difunde
    System.out.println("\nPruebas difunde.");

    ret = m.difunde(e3);
    System.out.println("\nDifundir luis -> Carmen: " + ret + " - " + m);

    ret = m.difunde(luis, carmen, ana, luis);
    System.out.println("Difundir Carmen -> Luis -> Carmen -> Ana -> luis: " +
        ret + " - " + m);

    ret = m.difunde(luis, jose, ana, carmen);
    System.out.println("Difundir Carmen -> Jose -> Ana -> luis " +
        "(ERROR, no hay enlace para jose): " + ret + " - " + m);

    // Nuevo mensaje con menor alcance
    Mensaje m2 = new Mensaje("Bien", 2, ana);
    System.out.println("\nCreo mensaje: " + m2);

    ret = m2.difunde(jose);
    System.out.println("Difunde ana -> jose (ERROR, no hay enlace): " + ret);

    ret = m2.difunde(carmen);
    System.out.println("Difunde ana -> carmen: " + ret + " - " + m2);

    ret = m2.difunde(luis);
    System.out.println("Difunde carmen -> luis (ERROR, no alcanza): " + ret);

    ret = m2.difunde(e1);
    System.out.println("Difunde ana -> luis (ERROR, usuario origen enlace " +
        "distinto de usuario actual): " + ret);

    ret = m2.difunde((Enlace) null);
    System.out.println("Difunde ana -> null (ERROR): " + ret);

    ret = m2.difunde();
    System.out.println("Difunde ana -> lista vacia de usuarios: " + ret);
  }
}