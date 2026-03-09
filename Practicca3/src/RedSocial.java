import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class RedSocial {
  private List<Usuario> usuariosMensaje = new ArrayList<>();
  private Mensaje mensaje;
  private Map<String, Usuario> usuarios = new HashMap<>();


  public RedSocial(String ficheroUsuarios,
      String ficheroEnlaces,
      String ficheroMensaje) throws IOException {

    // Lee ficheros y construye la red social
    procesarFicheroUsuarios(ficheroUsuarios);
    procesarFicheroEnlaces(ficheroEnlaces);
    procesarFicheroMensaje(ficheroMensaje);

    // Difunde mensaje
    difundirMensaje();
  }

  private void difundirMensaje() throws IOException {
    for (int i = 0; i < usuariosMensaje.size(); i++) {
      boolean ret = mensaje.difunde(usuarios.get(mensaje.getUsuarioActual().getNombre())
          .getEnlace(usuariosMensaje.get(i)));
      if (ret == true)
        System.out.println(mensaje.toString());
    }
  }

  private void procesarFicheroUsuarios(String fichero) throws IOException {
    try (BufferedReader buffer = new BufferedReader(
        new InputStreamReader(
            new FileInputStream(fichero)))) {

      // Leo por líneas el fichero
      String line = null;
      while ((line = buffer.readLine()) != null) {
        String[] partes = line.split("\\s+"); // Separo las palabras por espacios

        String nombre = partes[0]; // Nombre usuario
        int capacidadAmplificacion = Integer.parseInt(partes[1]); // Cap Amplificacion

        /* Añado el usuario a la red social */
        this.usuarios.put(nombre, new Usuario(nombre, capacidadAmplificacion));
      }
    }
  }

  private void procesarFicheroEnlaces(String fichero) throws IOException {
    try (BufferedReader buffer = new BufferedReader(
        new InputStreamReader(
            new FileInputStream(fichero)))) {

      // Leo por líneas el fichero
      String line = null;
      while ((line = buffer.readLine()) != null) {
        String[] partes = line.split("\\s+"); // Separo las palabras por espacios

        String origen = partes[0]; // Nombre usuario origen
        /* Compruebo si existe el usuario en la red social */
        Usuario usuarioOrigen = usuarios.get(origen);

        String destino = partes[1]; // Nombre usuario destino
        /* Compruebo si existe el usuario en la red social */
        Usuario usuarioDestino = usuarios.get(destino);

        int coste = Integer.parseInt(partes[2]); // Coste enlace
        
        /* Añado el enlace a la red social */
        usuarioOrigen.addEnlace(new Enlace(usuarioOrigen, usuarioDestino, coste));
      }
    }
  }

  private void procesarFicheroMensaje(String fichero) throws IOException {
    String mensaje = "", usuarioOrigen = "";
    int alcance = 0;

    try (BufferedReader buffer = new BufferedReader(
        new InputStreamReader(
            new FileInputStream(fichero)))) {

      String line = buffer.readLine();
      // Leer mensaje fichero
      if (line != null) {
        String[] partes = line.split("\\s+");
        mensaje = partes[0].split("\"")[1]; // Texto mensaje
        alcance = Integer.parseInt(partes[1]); // Alcance
        usuarioOrigen = partes[2]; // Nombre usuario inicial
      }

      // Creo el mensaje
      this.mensaje = new Mensaje(mensaje, alcance, usuarios.get(usuarioOrigen));

      // Añado los usuarios que el mensaje intentará visitar
      while ((line = buffer.readLine()) != null) {
        if (usuariosMensaje.contains(usuarios.get(line)) == false)
          usuariosMensaje.add(usuarios.get(line));
      }

      buffer.close();
    }
  }

  //Metodos de la fachada

  public void agregarUsuario(String nombre, int capacidadAmplificacion){
    if(!usuarios.containsKey(nombre)){
      this.usuarios.put(nombre, new Usuario(nombre, capacidadAmplificacion));
    }
  }

  public void agregarEnlace(String nombreOrigen, String nombreDestino, int coste) {
    // 1. Buscamos los usuarios
    Usuario origen = this.usuarios.get(nombreOrigen);
    Usuario destino = this.usuarios.get(nombreDestino);

    // 2. Si existen, le decimos al usuario origen que lo añada
    if (origen != null && destino != null) {
      // El método addEnlace del Usuario ya se encarga de que no se repitan
      origen.addEnlace(new Enlace(origen, destino, coste));
    }
  }
}
