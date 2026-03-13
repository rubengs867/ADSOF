public class EjemploPruebaMensaje {

  public static void main(String[] args) {
    // 1. PREPARAMOS EL ESCENARIO (Usuarios con diferentes exposiciones)
    Usuario juan = new Usuario("juan", 2, Exposicion.BAJA);
    Usuario alejandro = new Usuario("alejandro", 3, Exposicion.ALTA);
    Usuario ana = new Usuario("ana", 4, Exposicion.MEDIA);
    Usuario ronaldo = new Usuario("ronaldo", 1, Exposicion.VIRAL);

    // 2. CREAMOS ENLACES (Normales y un Señuelo)
    juan.addEnlace(new Enlace(juan, alejandro, 5)); // Normal: juan -> alejandro
    juan.addEnlace(new Enlace(juan, ana, 10)); // Normal: juan -> ana

    // Señuelo: juan -> ronaldo (Coste 5, Factor extra 2, Prob retorno 0.5)
    juan.addEnlace(new EnlaceSenuelo(juan, ronaldo, 5, 2, 0.5));

    System.out.println("PRUEBA 1: GETTERS Y SETTERS BÁSICOS");
    Mensaje mBasico = new Mensaje("¡Hola a todos!", 30, juan);

    // Probamos los Getters
    System.out.println("Autor del mensaje: " + mBasico.getUsuarioAutor());
    System.out.println("Usuario actual: @" + mBasico.getUsuarioActual().getNombre());
    System.out.println("Alcance original: " + mBasico.getAlcanceActual());

    // Probamos los Setters
    mBasico.setAlcanceActual(50);
    System.out.println("Alcance modificado con setter: " + mBasico.getAlcanceActual());
    System.out.println("Estado del mensaje: " + mBasico.toString());

    System.out.println("\nPRUEBA 2: MENSAJE CONTROLADO (Rigidez vs Exposición)");
    // Creamos un mensaje con Rigidez = 15
    // - Para entrar en ALTA se necesita >= 20.
    // - Para entrar en MEDIA se necesita >= 10.
    MensajeControlado mCtrl = new MensajeControlado("Confidencial", 100, juan, 15);
    System.out.println("Mensaje Controlado creado con rigidez 15.");

    // Intento 1: Hacia un usuario ALTA (Debería fallar porque 15 no es >= 20)
    Enlace enlaceHaciaalejandro = juan.getEnlace(alejandro);
    boolean exitoalejandro = mCtrl.difunde(enlaceHaciaalejandro);
    System.out.println("¿Difundido a @alejandro (Exp ALTA)? -> " + exitoalejandro);

    // Intento 2: Hacia un usuario MEDIA (Debería funcionar porque 15 es >= 10)
    Enlace enlaceHaciaana = juan.getEnlace(ana);
    boolean exitoana = mCtrl.difunde(enlaceHaciaana);
    System.out.println("¿Difundido a @ana (Exp MEDIA)? -> " + exitoana);

    if (exitoana) {
      System.out.println("   Usuario actual ahora es: @" + mCtrl.getUsuarioActual().getNombre());
    }

    System.out.println("\nPRUEBA 3: MENSAJE CONTROLADO VS ENLACE SEÑUELO");
    // Volvemos a poner el mensaje en juan para la prueba
    mCtrl.setUsuarioActual(juan);

    // Intento 3: Hacia un enlace señuelo (Debería fallar siempre, sin importar la
    // rigidez)
    Enlace enlaceSenuelo = juan.getEnlace(ronaldo);
    boolean exitoSenuelo = mCtrl.difunde(enlaceSenuelo);
    System.out.println("¿Difundido por el Enlace Señuelo? -> " + exitoSenuelo);
  }
}