import java.util.*;

public class RedSocial {
  private List<Usuario> usuarios;
  private List<Enlace> enlaces;
  private List<Mensaje> mensajes;
  
  public RedSocial(String ficheroUsuarios, String ficheroEnlaces, String ficheroMensaje){
    procesarFicheroUsuarios(ficheroUsuarios);
    procesarFicheroEnlaces(ficheroEnlaces);
    procesarFicheroMensaje(ficheroMensaje);
  }

  private void procesarFicheroUsuarios(String fichero){
    Scanner sc = null;
    try {
      /* Parto en tokens el fichero usando como separador el espacio en blanco */
      sc = new Scanner(fichero);
      while (sc.hasNext()) {
        /* Obtengo el nombre y capacidad de amplificación de cada usuario */
        String nombre = sc.next();
        int capacidadAmplificacion = sc.nextInt();
        /* Añado el usuario a la red social */
        this.addUsuario(new Usuario(nombre, capacidadAmplificacion));
      }
    } finally {
      if(sc != null)
        sc.close();
    }
  }
  
  private void procesarFicheroEnlaces(String fichero){
    Scanner sc = null;
    try {
      /* Parto en tokens el fichero usando como separador el espacio en blanco */
      sc = new Scanner(fichero);
      while (sc.hasNext()) {
        /* Obtengo el nombre nombre del usuario origen */
        String origen = sc.next();
        /* Compruebo si existe el usuario en la red social */
        Usuario usuarioOrigen = this.getUsuario(origen);
        if(usuarioOrigen == null) return;

        /* Obtengo el nombre nombre del usuario origen */
        String destino = sc.next();
        /* Compruebo si existe el usuario en la red social */
        Usuario usuarioDestino = this.getUsuario(destino);
        if(usuarioDestino == null) return;

        /* Obtengo el coste del enlace */
        int coste = sc.nextInt();

        /* Añado el enlace a la red social */
        this.addEnlace(new Enlace(usuarioOrigen, usuarioDestino, coste));
      }
    } finally {
      if(sc != null)
        sc.close();
    }
  }

  private void procesarFicheroMensaje(String fichero) {
    Scanner sc = null;
    try {
      /* Parto en tokens el fichero usando como separador el espacio en blanco */
      sc = new Scanner(fichero);
      while (sc.hasNext()) {
        /* Obtengo el mensaje nombre del usuario origen */
        String origen = sc.next();
        /* Compruebo si existe el usuario en la red social */
        Usuario usuarioOrigen = this.getUsuario(origen);
        if(usuarioOrigen == null) return;

        /* Obtengo el nombre nombre del usuario origen */
        String destino = sc.next();
        /* Compruebo si existe el usuario en la red social */
        Usuario usuarioDestino = this.getUsuario(destino);
        if(usuarioDestino == null) return;

        /* Obtengo el coste del enlace */
        int coste = sc.nextInt();

        /* Añado el enlace a la red social */
        this.addEnlace(new Enlace(usuarioOrigen, usuarioDestino, coste));
      }
    } finally {
      if(sc != null)
        sc.close();
    }
  }

  private void addUsuario(Usuario usuario) {
    if(usuario != null && this.usuarios.contains(usuario) == false)
      this.usuarios.add(usuario);
  }

  private void addEnlace(Enlace enlace) {
    if(enlace != null && this.enlaces.contains(enlace) == false)
      this.enlaces.add(enlace);
  }

  private void addMensaje(Mensaje mensaje) {
    if(mensaje != null && this.mensajes.contains(mensaje) == false)
      this.mensajes.add(mensaje);
  }

  private Usuario getUsuario(String origen) {
    if(origen != null)
      return null;

    for(int i = 0; i < usuarios.size(); i++) {
      if(usuarios.get(i).getNombre().equals(origen))
        return usuarios.get(i);
    }
    return null;
  }
}
