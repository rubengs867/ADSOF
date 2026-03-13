public class EjemploPruebaUsuario {

  public static void main(String[] args) {

    // Creamos usuarios normales con distintas exposiciones
    Usuario pepe = new Usuario("pepe", 2, Exposicion.BAJA);
    Usuario juan = new Usuario("juan", 3, Exposicion.MEDIA);
    Usuario marina = new Usuario("marina", 5, Exposicion.ALTA);
    Usuario jaime = new Usuario("jaime", 10, Exposicion.VIRAL);

    // Creamos nuestro Usuario Interesado
    UsuarioInteresado usuario1 = new UsuarioInteresado("usuario1", 5);

    // Añadimos enlaces IMPORTA EL ORDEN DE CREACIÓN
    usuario1.addEnlace(new Enlace(usuario1, juan, 10)); // 1º - Destino MEDIA
    usuario1.addEnlace(new Enlace(usuario1, marina, 20)); // 2º - Destino ALTA <--- (Debería elegir este)
    usuario1.addEnlace(new Enlace(usuario1, pepe, 5)); // 3º - Destino BAJA
    usuario1.addEnlace(new Enlace(usuario1, jaime, 50)); // 4º - Destino VIRAL (Nunca llegará porque Marina está antes)

    System.out.println("Enlaces de @usuario1 creados en orden: juan, marina, pepe, jaime.");

    System.out.println("\nPRUEBA 1: COMPORTAMIENTO INTERESADO (DESVÍO)");
    System.out.println("Intentamos pedirle a @usuario1 su enlace hacia @pepe (BAJA).");

    // Aquí ocurre la magia del polimorfismo. Le pedimos a Pepe, pero nos debe
    // devolver a Marina.
    Enlace enlaceObtenido = usuario1.getEnlace(pepe);

    System.out.println("Enlace devuelto por el método: " + enlaceObtenido.toString());
    System.out.println("¿el sistema devolvio @marina (ALTA)? -> " + enlaceObtenido.getUsuarioDestino());

    System.out.println("\nPRUEBA 2: COMPORTAMIENTO ESTÁNDAR (FALLBACK)");
    // Vamos a crear otro interesado pero que no conoce a nadie famoso
    UsuarioInteresado usuario2 = new UsuarioInteresado("usuario2", 2);
    usuario2.addEnlace(new Enlace(usuario2, juan, 10)); // Destino MEDIA
    usuario2.addEnlace(new Enlace(usuario2, pepe, 5)); // Destino BAJA

    System.out.println("Intentamos pedirle a @usuario2 (sin amigos famosos) su enlace hacia @pepe.");

    // Como no hay destinos ALTA o VIRAL en su lista, debe devolver el de Pepe
    // (comportamiento normal).
    Enlace enlaceNormal = usuario2.getEnlace(pepe);

    System.out.println("Enlace devuelto por el método: " + enlaceNormal.toString());
    System.out.println("¿devolvió a @pepe? -> " + enlaceNormal.getUsuarioDestino());
  }
}