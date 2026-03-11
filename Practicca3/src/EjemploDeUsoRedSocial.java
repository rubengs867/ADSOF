import java.io.IOException;

public class EjemploDeUsoRedSocial {
  public static void main(String[] args) {
    try {
      String usuario = "USUARIOS.txt";
      String enlace = "ENLACES.txt";
      String mensaje = "MENSAJE.txt";
      RedSocial s;
      s = new RedSocial(usuario,
          enlace,
          mensaje);
      s.guardarRedSocial("usu.txt", "enl.txt", "men.txt");
      s = new RedSocial(usuario,
          enlace,
          "MENSAJE2.txt");
    } catch (IOException e) {
      System.out.println("Error en archivos");
    }
  }
}
