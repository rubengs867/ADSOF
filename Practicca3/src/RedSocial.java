import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  private void difundirMensaje() {
    for (int i = 0; i < usuariosMensaje.size(); i++) {
      // Difundo el mensaje persona por persona según la lista de usuarios guardados
      boolean ret = mensaje.difunde(usuarios.get(mensaje.getUsuarioActual().getNombre())
          .getEnlace(usuariosMensaje.get(i)));
      if (ret == true)
        // En caso de haber sido difundido exitosamente el mensaje, se imprime
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
        agregarUsuario(nombre, capacidadAmplificacion);
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
        String destino = partes[1]; // Nombre usuario destino
        int coste = Integer.parseInt(partes[2]); // Coste enlace

        /* Añado el enlace a la red social */
        agregarEnlace(origen, destino, coste);
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

      // Agrego el mensaje a la Red Social
      agregarMensaje(mensaje, alcance, usuarioOrigen);

      // Añado los usuarios que el mensaje intentará visitar
      while ((line = buffer.readLine()) != null) {
        if (usuariosMensaje.contains(usuarios.get(line)) == false)
          usuariosMensaje.add(usuarios.get(line));
      }
    }
  }

  // Metodos de la fachada

  public boolean agregarUsuario(String nombre, int capacidadAmplificacion) {
    // Control de errores
    if (nombre == null)
      return false;

    // En caso de no contener al usuario lo crea y lo añade
    if (usuarios.containsKey(nombre) == false) {
      this.usuarios.put(nombre, new Usuario(nombre, capacidadAmplificacion));
    }
    return true;
  }

  public boolean agregarEnlace(String nombreOrigen, String nombreDestino, int coste) {
    // Control de errores
    if (nombreDestino == null || nombreDestino == null)
      return false;

    // Buscamos los usuarios
    Usuario origen = this.usuarios.get(nombreOrigen);
    Usuario destino = this.usuarios.get(nombreDestino);
    // En caso de no existir alguno de los usarios no crea el enlace
    if (origen == null || destino == null)
      return false;

    // El método addEnlace del Usuario ya se encarga de que no se repitan
    origen.addEnlace(new Enlace(origen, destino, coste));

    return true;
  }

  public boolean agregarMensaje(String autor, int alcance, String usuarioActual) {
    // Control de errores
    if (autor == null || usuarioActual == null)
      return false;

    // Obtengo el usuario
    Usuario usuario = this.usuarios.get(usuarioActual);
    if (usuario == null)
      return false;

    // Creo el mensaje y lo añado a la Red Social
    this.mensaje = new Mensaje(autor, alcance, usuario);
    return true;
  }

  public void guardarRedSocial(String ficheroUsuarios,
      String ficheroEnlaces,
      String ficheroMensaje) throws IOException {
    // Guarda los usuarios, enlaces, o mensajes
    guardarUsuarios(ficheroUsuarios);
    guardarEnlaces(ficheroEnlaces);
    guardarMensaje(ficheroMensaje);
  }

  private void guardarUsuarios(String ficheroUsuarios) throws IOException {
    try (PrintWriter output = new PrintWriter(new FileOutputStream(ficheroUsuarios))) {

      for (Usuario u : usuarios.values()) {
        // Imprimo <NOMBRE> <CAPACIDAD_AMPLIFICACION> para cada usuario
        output.println(u.getNombre() + " " + u.getCapacidadAmplificacion());
      }
    }
  }

  private void guardarEnlaces(String ficheroEnlaces) throws IOException {
    try (PrintWriter output = new PrintWriter(new FileOutputStream(ficheroEnlaces))) {
      int i = 0;
      for (Usuario u : usuarios.values()) {
        Enlace enlace = u.getEnlace(i); // Obtengo el i-ésimo enlace del usuario
        // Imprimo <NOMBRE_ORIGEN> <NOMBRE_DESTINO> <COSTE>
        if (enlace != null) {
          output.println(u.getNombre() + " " + enlace.getUsuarioDestino().getNombre() + " " +
              enlace.getCoste());
        }
        i++;
      }
    }
  }

  private void guardarMensaje(String ficheroMensaje) throws IOException {
    try (PrintWriter output = new PrintWriter(new FileOutputStream(ficheroMensaje))) {
      // Imprimo "<TEXTO>" <ALCANCE> <NOMBRE_USUARIO_ACTUAL>
      output.println("\"" + mensaje.getUsuarioAutor() + "\" " + mensaje.getAlcanceActual() + " " +
          mensaje.getUsuarioActual().getNombre());
      // Imprimo los nombres de los usuarios que el mensaje intentará visitar
      for (Usuario u : usuariosMensaje) {
        output.println(u.getNombre());
      }
    }
  }
}
