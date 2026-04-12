package src.estacion;

/**
 * Clase que representa una ubicación geográfica mediante coordenadas.
 * <p>
 * Una ubicación se define a partir de:
 * </p>
 * <ul>
 *   <li>Latitud: posición norte-sur.</li>
 *   <li>Longitud: posición este-oeste.</li>
 * </ul>
 * <p>
 * Estas coordenadas se utilizan, por ejemplo, para situar una estación
 * meteorológica en un punto concreto del mapa.
 * </p>
 */
public class Ubicacion {

  /** Coordenada de latitud (en grados). */
  private double latitud;

  /** Coordenada de longitud (en grados). */
  private double longitud;

  /**
   * Constructor que crea una nueva ubicación geográfica.
   *
   * @param latitud Coordenada de latitud.
   * @param longitud Coordenada de longitud.
   */
  public Ubicacion(double latitud, double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  /**
   * Devuelve la latitud de la ubicación.
   *
   * @return valor de latitud.
   */
  public double getLatitud() {
    return latitud;
  }

  /**
   * Devuelve la longitud de la ubicación.
   *
   * @return valor de longitud.
   */
  public double getLongitud() {
    return longitud;
  }

  /** 
   * Representación textual de una Ubicación.
   */
  @Override
  public String toString() {
    return latitud + ", " + longitud;
  }

  
}