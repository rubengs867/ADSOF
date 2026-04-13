package alerta;

import java.time.LocalDateTime;

/**
 * Representa una alerta generada por un sensor de la estación meteorológica.
 * <p>
 * Esta clase encapsula toda la información necesaria para identificar qué
 * sensor falló, en qué momento exacto ocurrió y cuál fue el motivo.
 * </p>
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class Alerta {

  /** Momento exacto en el que se produjo la alerta. */
  private final LocalDateTime fecha;

  /** Identificador único del sensor que originó la alerta. */
  private final String idSensor;

  /** Mensaje descriptivo del error o anomalía detectada. */
  private final String mensaje;

  /** Excepción que ha generado la alerta */
  private final Exception exception;

  /**
   * Crea una nueva instancia de Alerta.
   *
   * @param fecha     Fecha y hora de la incidencia.
   * @param idSensor  Identificador del sensor.
   * @param mensaje   Descripción detallada del error.
   * @param exception Excepción que ha generado la alerta.
   */
  public Alerta(LocalDateTime fecha, String idSensor, String mensaje, Exception exception) {
    this.fecha = fecha;
    this.idSensor = idSensor;
    this.mensaje = mensaje;
    this.exception = exception;
  }

  /**
   * Obtiene la fecha y hora en la que se registró la alerta.
   * 
   * @return objeto {@link LocalDateTime} con la marca temporal.
   */
  public LocalDateTime getFecha() {
    return fecha;
  }

  /**
   * Obtiene el identificador del sensor asociado a la alerta.
   * <p>
   * Este método es fundamental para las operaciones de filtrado y
   * limpieza de alertas durante la calibración.
   * </p>
   * 
   * @return ID del sensor en formato String.
   */
  public String getIdSensor() {
    return idSensor;
  }

  /**
   * Obtiene el mensaje descriptivo del error.
   * 
   * @return String con la descripción del problema detectado.
   */
  public String getMensaje() {
    return mensaje;
  }

  /**
   * Obtiene la excepción que ha generado la alerta
   * 
   * @return objeto {@Exception}
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Devuelve una representación textual legible de la alerta.
   * <p>
   * El formato devuelto es: {@code [FECHA] Alerta en ID_SENSOR: MENSAJE}
   * </p>
   * 
   * @return representación en cadena de la alerta.
   */
  @Override
  public String toString() {
    return String.format("[%s] Alerta en %s: %s",
        fecha.toString(),
        idSensor,
        mensaje);
  }
}