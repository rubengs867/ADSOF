package excepcion;

import sensor.Sensor;

/**
 * Excepción que se lanza cuando se intenta registrar un sensor
 * cuyo identificador ya existe en el sistema.
 * <p>
 * Esta excepción proporciona acceso tanto al sensor previamente
 * registrado como al sensor que ha causado el conflicto, lo que
 * permite gestionar el error con mayor detalle.
 * </p>
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class SensorDuplicadoException extends Exception {

  /** Sensor que ya estaba registrado previamente. */
  private Sensor existente;

  /** Sensor que se intentó registrar y provocó el conflicto. */
  private Sensor duplicado;

  /**
   * Constructor que crea una nueva excepción de sensor duplicado.
   * <p>
   * Genera un mensaje descriptivo indicando el conflicto de ID.
   * </p>
   *
   * @param existente Sensor ya presente en el sistema.
   * @param duplicado Sensor que se intentó añadir y que tiene el mismo ID.
   */
  public SensorDuplicadoException(Sensor existente, Sensor duplicado) {
    super("Conflicto de ID: El sensor " + duplicado.getId() + " ya está registrado.");
    this.existente = existente;
    this.duplicado = duplicado;
  }

  /**
   * Devuelve el sensor que ya estaba registrado.
   *
   * @return Sensor existente en el sistema.
   */
  public Sensor getExistente() {
    return existente;
  }

  /**
   * Devuelve el sensor que provocó el conflicto.
   *
   * @return Sensor duplicado que se intentó registrar.
   */
  public Sensor getDuplicado() {
    return duplicado;
  }
}