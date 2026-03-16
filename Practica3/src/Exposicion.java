/**
 * Define los niveles de exposición pública a los que se expone un usuario.
 * Cuan visible o accesible es para la recepción de mensajes.
 */
public enum Exposicion {
  /** Visibilidad mínima */
  OCULTA,
  /** Visibilidad baja */
  BAJA,
  /** Visibilidad media */
  MEDIA,
  /** Visibilidad por defecto */ 
  ALTA,
  /** Visibilidad máxima */
  VIRAL;
}
