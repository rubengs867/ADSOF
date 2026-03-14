import java.io.IOException;

/**
 * Clase de ejemplo que muestra cómo utilizar la fachada {@link RedSocial}.
 *
 * Este ejemplo sirve para comprobar el funcionamiento de la lectura,
 * difusión del mensaje y guardado de la red social.
 */
public class EjemploDeUsoRedSocial {

  /**
   * Método principal del programa.
   *
   * Crea una instancia de {@link RedSocial} leyendo la información desde
   * ficheros de texto, guarda la red social en nuevos ficheros y finalmente
   * vuelve a cargar la red social con un segundo mensaje.
   *
   * @param args argumentos de la línea de comandos (no utilizados)
   */
  public static void main(String[] args) {

    try {
      String usuario = "../txt/USUARIOS.txt";
      String enlace = "../txt/ENLACES.txt";
      String mensaje = "../txt/MENSAJE.txt";

      RedSocial s;

      // Crear la red social leyendo los ficheros
      s = new RedSocial(usuario, enlace, mensaje);

      // Creo varios usuarios y los añado a la red social
      s.addUsuario("jaime", -90);
      s.addUsuario("pablo", 8);

      // Creo un enlace y lo añado a la red social
      s.addEnlace("jaime", "pablo", 3);

      // Creo un mensaje nuevo
      s.addMensaje("Te puedo molestar?", 4, "jaime");
      
      // Creo una ruta de difusión nueva para el mensaje
      s.limpiarRutaMensaje();
      s.addDestinoMensaje("pablo");

      // Guardar el estado de la red social en nuevos ficheros
      s.guardarRedSocial("../txt/usu.txt", "../txt/enl.txt", "../txt/men.txt");

      // Difundo el mensaje nuevo
      s.difundirMensaje();

      /*
       * Crear otra red social con el mismo conjunto de usuarios y enlaces
       * pero utilizando un mensaje diferente
       */
      s = new RedSocial(usuario, enlace, "../txt/MENSAJE2.txt");

    } catch (IOException e) {

      // Se captura cualquier error de lectura o escritura de ficheros
      System.out.println("Error en archivos");
    }
  }
}