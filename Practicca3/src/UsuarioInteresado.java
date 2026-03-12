/**
 * Representa un tipo de usuario especial, UsuarioInteresado 
 */
public class UsuarioInteresado extends Usuario {

  /**
   * Construye un usuario interesado con nombre y capacidad de amplificación especificados.
   * Utiliza la lista heredada de la clase padre para mantener el orden de inserción de enlaces.
   * * @param nombre El nombre identificador del usuario.
   * @param capacidadAmplificacion La capacidad de amplificación asignada.
   */
  public UsuarioInteresado(String nombre, int capacidadAmplificacion) {
    super(nombre, capacidadAmplificacion);
  }

  /**
   * Construye un usuario interesado definiendo explícitamente su nivel de exposición inicial.
   * * @param nombre El nombre identificador del usuario.
   * @param capacidadAmplificacion La capacidad de amplificación asignada.
   * @param exposicion El nivel de exposición pública inicial del usuario.
   */
  public UsuarioInteresado(String nombre, int capacidadAmplificacion, Exposicion exposicion) {
    super(nombre, capacidadAmplificacion, exposicion);
  }

  /**
   * Construye un usuario interesado con el nombre indicado y la capacidad de 
   * amplificación por defecto.
   * * @param nombre El nombre identificador del usuario.
   */
  public UsuarioInteresado(String nombre) {
    super(nombre);
  }

  /**
   * Busca un enlace saliente para propagar un mensaje. 
   * sobreescribimos el metodo del padre, este método busca primero en orden 
   * de creación un enlace cuyo destino tenga exposición ALTA o VIRAL, ignorando 
   * el destino solicitado inicialmente. Si no encuentra ninguno, aplica el 
   * comportamiento estándar (búsqueda estricta por destino).
   * * @param destUsuario El usuario destino al que teóricamente va dirigido el mensaje.
   * @return El primer enlace hacia un usuario con exposición ALTA/VIRAL, o el metodo del padre
   * 
   */
  @Override
  public Enlace getEnlace(Usuario destUsuario) {

    for (int i = 0; i < this.getNumEnlaces(); i++) {
      Enlace enlaceActual = this.getEnlace(i);
      Usuario destino = enlaceActual.getUsuarioDestino();
      
      Exposicion exp = destino.getExposicion(); 
      
      if (exp == Exposicion.ALTA || exp == Exposicion.VIRAL) {
        return enlaceActual; 
      }
    }

    return super.getEnlace(destUsuario);
  }
}