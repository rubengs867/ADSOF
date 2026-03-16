/**
 * Representa un mensaje con restricciones de difusión avanzadas.
 */
public class MensajeControlado extends Mensaje {

  /** Nivel de restricción de difusión */
  private int rigidez;

  /**
   * Constructor del mensaje constrolado
   * la diferencia con un mensaje normal esta en la rigidez
   * 
   * @param autor   el contenido textual del mensaje
   * @param alcance el alcance inicial del mensaje
   * @param usuario el usuario en el que se origina o encuentra el mensaje
   * @param rigidez el nivel de restricción de difusión
   */
  public MensajeControlado(String autor, int alcance, Usuario usuario, int rigidez) {
    super(autor, alcance, usuario);
    this.rigidez = rigidez;
  }

  /**
   * Un mensaje controlado no se propaga por enlaces señuelo.
   * 
   * @param enlace se mira de que tipo es el enlace
   */
  @Override
  protected boolean puedeDifundirPor(Enlace enlace) {
    // Si el enlace es un señuelo, no puede difundir
    if (enlace instanceof EnlaceSenuelo) {
      return false;
    }
    // Si no es un señuelo, comprobamos si tiene alcance suficiente (lógica de la
    // clase padre)
    return super.puedeDifundirPor(enlace);
  }

  /**
   * Un mensaje controlado solo es aceptado si la exposición del usuario
   * supera el umbral de rigidez.
   * 
   * @param usuario recibe un usuario
   */
  @Override
  protected boolean aceptadoPor(Usuario usuario) {
    // Obtenemos la exposición del usuario destino
    Exposicion exp = usuario.getExposicion();

    switch (exp) {
      case OCULTA:
        return true; // Siempre aceptado en modo oculto
      case BAJA:
        return this.rigidez >= 5;
      case MEDIA:
        return this.rigidez >= 10;
      case ALTA:
        return this.rigidez >= 20;
      case VIRAL:
        return this.rigidez >= 50;
      default:
        return false;
    }
  }
}