package Practica4.estacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Practica4.excepcion.SensorDuplicadoException;
import Practica4.sensor.Sensor;

/**
 * Clase que representa una estación meteorológica.
 * <p>
 * Esta clase gestiona un conjunto de sensores y realizar búsquedas sobre ellos.
 * </p>
 */
public class EstacionMeteorologica {

  /** Nombre de la estación. */
  private String nombre;

  /** Ubicación geográfica de la estación. */
  private Ubicacion ubicacion;

  /** Mapa principal de sensores indexados por su ID. */
  private Map<String, Sensor> sensores;

  /** Agrupa sensores por su tipo (ej: HUM). */
  private Map<String, List<Sensor>> sensoresPorTipo;

  /** Registro de la fecha de instalación de cada sensor (por ID). */
  private Map<String, LocalDate> fechasPorID;

  /**
   * Constructor que crea una nueva estación meteorológica.
   *
   * @param nombre    Nombre de la estación.
   * @param ubicacion Ubicación geográfica de la estación.
   */
  public EstacionMeteorologica(String nombre, Ubicacion ubicacion) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.sensores = new HashMap<>();
    this.sensoresPorTipo = new HashMap<>();
    this.fechasPorID = new HashMap<>();
  }

  /**
   * Añade un nuevo sensor a la estación meteorológica.
   * <p>
   * 1. Mapa principal de sensores.
   * 2. Índice de sensores por tipo.
   * 3. Registro de fecha de instalación.
   * </p>
   *
   * @param nuevoSensor Sensor que se desea registrar.
   * @throws SensorDuplicadoException si ya existe un sensor con el mismo ID.
   */
  public void añadirSensor(Sensor nuevoSensor) throws SensorDuplicadoException {
    String id = nuevoSensor.getId();
    String tipoClase = nuevoSensor.getTipo();

    if (sensores.containsKey(id)) {
      throw new SensorDuplicadoException(sensores.get(id), nuevoSensor);
    }

    // Guardar en el mapa principal
    sensores.put(id, nuevoSensor);

    // Registrar fecha de instalación (fecha actual)
    fechasPorID.put(id, LocalDate.now());

    // Actualizar el índice por tipo
    if (!sensoresPorTipo.containsKey(tipoClase)) {
      sensoresPorTipo.put(tipoClase, new ArrayList<>());
    }

    sensoresPorTipo.get(tipoClase).add(nuevoSensor);
  }

  /**
   * Obtiene un sensor a partir de su identificador.
   *
   * @param id Identificador del sensor.
   * @return Sensor asociado al ID, o {@code null} si no existe.
   */
  public Sensor obtenerSensor(String id) {
    return sensores.get(id);
  }

  /**
   * Devuelve una lista de sensores de un tipo específico.
   * <p>
   * Se devuelve una copia de la lista para evitar modificaciones externas.
   * </p>
   *
   * @param tipoClase representación textual del tipo de sensor.
   * @return Lista de sensores de ese tipo. Si no existen, devuelve una lista
   *         vacía.
   */
  public List<Sensor> obtenerSensoresPorTipo(String tipoClase) {
    List<Sensor> resultado = sensoresPorTipo.get(tipoClase);
    return (resultado != null) ? new ArrayList<>(resultado) : new ArrayList<>();
  }

  /**
   * Obtiene la fecha de instalación de un sensor.
   *
   * @param id Identificador del sensor.
   * @return Fecha de instalación, o {@code null} si el sensor no existe.
   */
  public LocalDate obtenerFechaInstalacion(String id) {
    return fechasPorID.get(id);
  }

  /**
   * Genera el listado de los sensores de la estación.
   * <p>
   * Formato final: [ID (desde: FECHA_INST): SENSOR, ... ]
   * </p>
   * 
   * @return lista de todos los sensores.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("[");
    for (Sensor sensor : sensores.values()) {
      String id = sensor.getId();
      LocalDate fecha = fechasPorID.get(id);

      sb.append(id)
          .append(" (desde: ")
          .append(fecha)
          .append("): ")
          .append(sensor.toString());
    }
    sb.append("]\n");

    return sb.toString();
  }
}