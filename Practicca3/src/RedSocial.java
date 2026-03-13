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

/**
 * Clase fachada que representa una red social de usuarios conectados mediante
 * enlaces por los que se pueden difundir mensajes.
 *
 * Esta clase simplifica el acceso a las clases
 * internas {@link Usuario}, {@link Enlace} y {@link Mensaje}.
 */
public class RedSocial {

  /** Lista de usuarios que el mensaje intentará visitar. */
  private List<Usuario> rutaDifusion = new ArrayList<>();

  /** Mensaje actual que se está difundiendo. */
  private Mensaje mensaje;

  /** Lista de usuarios que el mensaje intentará visitar. */
  private Map<String, Usuario> usuarios = new HashMap<>();

  /**
   * Construye una red social leyendo la información desde tres ficheros:
   * usuarios, enlaces y mensaje.
   *
   * Tras cargar la información, el mensaje se difunde automáticamente.
   *
   * @param ficheroUsuarios fichero que contiene la lista de usuarios
   * @param ficheroEnlaces  fichero que contiene los enlaces entre usuarios
   * @param ficheroMensaje  fichero que contiene el mensaje y la ruta de difusión
   * @throws IOException si ocurre algún error durante la lectura de los ficheros
   */
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

  // =========================
  // DIFUSIÓN
  // =========================

  /**
   * Difunde el mensaje actual siguiendo la ruta de difusión del mensaje.
   *
   * Para cada usuario destino se obtiene el enlace correspondiente y se
   * intenta difundir el mensaje. Si la difusión tiene éxito, el estado
   * del mensaje se imprime por pantalla.
   */
  public void difundirMensaje() {
    // Navego por todos los usuarios que el mensaje quiere difundir
    for (Usuario u : rutaDifusion) {

      // Obtengo el enlace que conecta el usuario actual con el siguiente usuario
      Enlace e = mensaje.getUsuarioActual().getEnlace(u);

      // Intento difunfir el mensaje
      if (mensaje.difunde(e)) {
        // Imprimo el mensaje en caso de haber sido difundido
        System.out.println(mensaje);
      }
    }
  }

  // =========================
  // CARGA DESDE FICHEROS
  // =========================

  /**
   * Procesa el fichero de usuarios y los añade a la red social.
   *
   * @param fichero ruta del fichero de usuarios
   * @throws IOException si ocurre un error surante la lectura del fichero
   */
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
        addUsuario(nombre, capacidadAmplificacion);
      }
    }
  }

  /**
   * Procesa el fichero de enlaces y los añade a la red social.
   *
   * @param fichero ruta del fichero de enlaces
   * @throws IOException si ocurre un error durante la lectura del fichero
   */
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
        addEnlace(origen, destino, coste);
      }
    }
  }

  /**
   * Procesa el fichero que contiene el mensaje y la ruta de difusión.
   *
   * @param fichero ruta del fichero del mensaje
   * @throws IOException si ocurre un error durante la lectura del fichero
   */
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

        // Agrego el mensaje a la Red Social
        addMensaje(mensaje, alcance, usuarioOrigen);
      }

      // Añado los usuarios que el mensaje intentará visitar
      while ((line = buffer.readLine()) != null) {
        addDestinoMensaje(line);
      }
    }
  }

  // =========================
  // MÉTODOS DE LA FACHADA
  // =========================

  /**
   * Añade un nuevo usuario a la red social.
   *
   * @param nombre                 nombre del usuario
   * @param capacidadAmplificacion capacidad de amplificación del usuario
   * @return {@code true} si el usuario se añadió correctamente,
   *         {@code false} si el nombre es nulo o el usuario ya existe
   */
  public boolean addUsuario(String nombre, int capacidadAmplificacion) {
    // Control de errores
    if (nombre == null || usuarios.containsKey(nombre))
      return false;

    // En caso de no contener al usuario lo crea y lo añade
    usuarios.put(nombre, new Usuario(nombre, capacidadAmplificacion));
    return true;
  }

  /**
   * Añade un enlace entre dos usuarios existentes de la red social.
   *
   * @param nombreOrigen  nombre del usuario origen
   * @param nombreDestino nombre del usuario destino
   * @param coste         coste del enlace
   * @return {@code true} si el enlace se añadió correctamente,
   *         {@code false} si alguno de los usuarios no existe
   */
  public boolean addEnlace(String nombreOrigen, String nombreDestino, int coste) {
    // Control de errores
    if (nombreOrigen == null || nombreDestino == null)
      return false;

    // Buscamos los usuarios
    Usuario origen = this.usuarios.get(nombreOrigen);
    Usuario destino = this.usuarios.get(nombreDestino);
    // En caso de no existir alguno de los usarios no crea el enlace
    if (origen == null || destino == null)
      return false;

    // El método addEnlace del Usuario ya se encarga de que no se repitan
    return origen.addEnlace(new Enlace(origen, destino, coste));
  }

  /**
   * Crea y establece el mensaje que se difundirá por la red.
   *
   * @param autor         texto del mensaje
   * @param alcance       alcance inicial del mensaje
   * @param usuarioActual nombre del usuario inicial
   * @return {@code true} si el mensaje se creó correctamente,
   *         {@code false} si el usuario inicial no existe
   */
  public boolean addMensaje(String autor, int alcance, String usuarioActual) {
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

  /**
   * Vacía la ruta de difusión del mensaje.
   */
  public void limpiarRutaMensaje() {
    rutaDifusion.clear();
  }

  /**
   * Añade un usuario al final de la ruta de difusión del mensaje.
   *
   * El usuario solo se añade si existe en la red social y no estaba
   * previamente en la lista de difusión.
   *
   * @param nombre nombre del usuario destino
   */
  public void addDestinoMensaje(String nombre) {
    Usuario u = usuarios.get(nombre); // Obtiene el usuario

    /*
     * Añade el usuario en caso de exitir en la red social y
     * no estar en la lista de difusión del mensaje
     */
    if (u != null && !rutaDifusion.contains(u))
      rutaDifusion.add(u);
  }

  // =========================
  // GUARDADO EN FICHEROS
  // =========================

  /**
   * Guarda el estado completo de la red social en tres ficheros.
   *
   * @param ficheroUsuarios fichero donde se guardarán los usuarios
   * @param ficheroEnlaces  fichero donde se guardarán los enlaces
   * @param ficheroMensaje  fichero donde se guardará el mensaje
   * @throws IOException si ocurre un error durante la escritura
   */
  public void guardarRedSocial(String ficheroUsuarios,
      String ficheroEnlaces,
      String ficheroMensaje) throws IOException {

    // Guarda los usuarios, enlaces, o mensajes
    guardarUsuarios(ficheroUsuarios);
    guardarEnlaces(ficheroEnlaces);
    guardarMensaje(ficheroMensaje);
  }

  /**
   * Guarda la lista de usuarios en un fichero.
   *
   * @param ficheroUsuarios ruta del fichero de salida
   * @throws IOException si ocurre un error durante la escritura
   */
  private void guardarUsuarios(String ficheroUsuarios) throws IOException {
    try (PrintWriter output = new PrintWriter(new FileOutputStream(ficheroUsuarios))) {

      for (Usuario u : usuarios.values()) {
        // Imprimo <NOMBRE> <CAPACIDAD_AMPLIFICACION> para cada usuario
        output.println(u.getNombre() + " " + u.getCapacidadAmplificacion());
      }
    }
  }

  /**
   * Guarda todos los enlaces de la red social en un fichero.
   *
   * @param ficheroEnlaces ruta del fichero de salida
   * @throws IOException si ocurre un error durante la escritura
   */
  private void guardarEnlaces(String ficheroEnlaces) throws IOException {
    try (PrintWriter output = new PrintWriter(new FileOutputStream(ficheroEnlaces))) {

      for (Usuario u : usuarios.values()) {

        // Obtengo todos los enlaces del usuario
        for (int i = 0; i < u.getNumEnlaces(); i++) {
          Enlace e = u.getEnlace(i);

          // Imprimo <NOMBRE_ORIGEN> <NOMBRE_DESTINO> <COSTE>
          output.println(
              u.getNombre() + " " +
                  e.getUsuarioDestino().getNombre() + " " +
                  e.getCoste());
        }
      }
    }
  }

  /**
   * Guarda la información del mensaje y la lista de usuarios destino.
   *
   * @param ficheroMensaje ruta del fichero de salida
   * @throws IOException si ocurre un error durante la escritura
   */
  private void guardarMensaje(String ficheroMensaje) throws IOException {
    try (PrintWriter output = new PrintWriter(new FileOutputStream(ficheroMensaje))) {

      // Imprimo "<TEXTO>" <ALCANCE> <NOMBRE_USUARIO_ACTUAL>
      output.println(
          "\"" + mensaje.getUsuarioAutor() + "\" " +
              mensaje.getAlcanceActual() + " " +
              mensaje.getUsuarioActual().getNombre());

      // Imprimo los nombres de los usuarios que el mensaje intentará visitar
      for (Usuario u : rutaDifusion) {
        output.println(u.getNombre());
      }
    }
  }
}
