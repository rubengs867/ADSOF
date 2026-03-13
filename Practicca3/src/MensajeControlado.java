public class MensajeControlado extends Mensaje {

  private int rigidez;

  /**
   * Constructor del mensaje constrolado
   * la diferencia con un mensaje normal esta en la rigidez
   * 
   * @param autor
   * @param alcance
   * @param usuario
   * @param rigidez
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
  protected boolean puedeDifundirPor(Enlace e) {
    // Si el enlace es un señuelo, no puede difundir
    if (e instanceof EnlaceSenuelo) {
      return false;
    }
    // Si no es un señuelo, comprobamos si tiene alcance suficiente (lógica de la
    // clase padre)
    return super.puedeDifundirPor(e);
  }

  /**
   * Un mensaje controlado solo es aceptado si la exposición del usuario
   * supera el umbral de rigidez.
   * 
   * @param usuario recibe un usuario
   */
  @Override
  protected boolean aceptadoPor(Usuario u) {
    // Obtenemos la exposición del usuario destino
    Exposicion exp = u.getExposicion();

    switch (exp) {
      case OCULTA:
        return true;
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