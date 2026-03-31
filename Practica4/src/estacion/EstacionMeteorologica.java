package Practica4.src.estacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Practica4.src.excepcion.SensorDuplicadoException;
import Practica4.src.sensor.Sensor;

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
   * Periodo configurable en milisegundos, tras el cual se realizarán las lecturas
   */
  private long periodo;

  /** Número máximo de mediciones a realizar en las lecturas periódicas */
  private int maxLecturas;

  /** Fecha de la última lectura periódica */
  private LocalDateTime ultimaLectura;

  /**
   * Constructor que crea una nueva estación meteorológica.
   * <p>
   * Se configura por defecto sin periodo de tal forma que no se realicen lecturas
   * periódicas.
   * </p>
   *
   * @param nombre      Nombre de la estación.
   * @param ubicacion   Ubicación geográfica de la estación.
   * @param periodo     Periodo de tiempo en <b>milisegundos</b> tras el cual se
   *                    debe realizar una
   *                    lectura.
   * @param maxLecturas Número máximo de lecturas a realizar en una lectura
   *                    periódica.
   */
  public EstacionMeteorologica(String nombre, Ubicacion ubicacion, long periodo, int maxLecturas) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.sensores = new HashMap<>();
    this.sensoresPorTipo = new HashMap<>();
    this.fechasPorID = new HashMap<>();
    this.periodo = periodo;
    this.maxLecturas = maxLecturas;
    this.ultimaLectura = null;
  }

  /**
   * Constructor que crea una nueva estación meteorológica.
   * <p>
   * Se configura por defecto sin periodo de tal forma que no se realicen lecturas
   * periódicas. Además, por defecto {@code maxLecturas} se inicializa a -1 para
   * realizar las mediciones de todos los sensores.
   * </p>
   *
   * @param nombre    Nombre de la estación.
   * @param ubicacion Ubicación geográfica de la estación.
   */
  public EstacionMeteorologica(String nombre, Ubicacion ubicacion) {
    this(nombre, ubicacion, 0, -1);
  }

  /**
   * Realiza una lectura puntual en un número determinado de sensores.
   * 
   * @param numSensores Número de sensores a medir.
   *                    Si es -1, se miden todos los sensores registrados.
   */
  public void realizarLecturaPuntual(int numSensores) {
    List<Sensor> lista = new ArrayList<>(sensores.values());
    int total = 0;

    // Determinamos cuántos sensores van a realizar la medición
    if (numSensores == -1 || numSensores > lista.size())
      total = lista.size();
    else
      total = numSensores;

    for (int i = 0; i < total; i++) {
      lista.get(i).realizarMedicion();
    }
  }

  /**
   * Comprueba si se cumplen las condiciones para realizar la siguiente medición
   * periódica. En caso de cumplirse el periodo, realiza las mediciones de hasta
   * {@code maxLecturas} sensores.
   * 
   * @return {@code true} si se ha realizado una medición en esta llamada;
   *         {@code false} en caso constrario.
   */
  public boolean realizarLecturasPeriodicas() {

    LocalDateTime ahora = LocalDateTime.now();

    // Comprobar si es la primera vez o si ya pasó el periodo desde la última
    if (ultimaLectura == null ||
        ChronoUnit.MILLIS.between(ultimaLectura, ahora) >= periodo) {

      // Realizar la medición
      realizarLecturaPuntual(maxLecturas);

      // Actualizar el estado de la estación
      this.ultimaLectura = ahora;
      return true;
    }

    return false;
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
  public void addSensor(Sensor nuevoSensor) throws SensorDuplicadoException {
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
  public Sensor getSensor(String id) {
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
  public List<Sensor> getSensoresPorTipo(String tipoClase) {
    List<Sensor> resultado = sensoresPorTipo.get(tipoClase);
    return (resultado != null) ? new ArrayList<>(resultado) : new ArrayList<>();
  }

  /**
   * Obtiene la fecha de instalación de un sensor.
   *
   * @param id Identificador del sensor.
   * @return Fecha de instalación, o {@code null} si el sensor no existe.
   */
  public LocalDate getFechaInstalacion(String id) {
    return fechasPorID.get(id);
  }

  /**
   * Devuelve el número de mediciones máximo a realizar en una lectura periódica.
   * 
   * @return valor con el número máximo de mediciones.
   */
  public int getMaxLecturas() {
    return maxLecturas;
  }

  /**
   * Establece el periodo de tiempo que debe pasar para realizar la siguiente
   * lectura periódica.
   * 
   * @param periodo perido de tiempo en <b>milisegundos</b>
   */
  public void setPeriodo(long periodo) {
    this.periodo = periodo;
  }

  /**
   * Establece el número máximo de lecturas que se van a realizar en una lectura
   * periódica. En caso de introducir un valor menor que -1, no se modfica el
   * atributo.
   * <p>
   * <b>Nota:</b> establecer a -1 para realizar la medición en todos los sensores
   * </p>
   * 
   * @param maxLecturas número máximo de lecturas.
   */
  public void setMaxLecturas(int maxLecturas) {
    if (maxLecturas >= -1)
      this.maxLecturas = maxLecturas;
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

    List<Sensor> listaSensores = new ArrayList<>(sensores.values());

    for (int i = 0; i < listaSensores.size(); i++) {
      Sensor sensor = listaSensores.get(i);
      String id = sensor.getId();
      LocalDate fecha = fechasPorID.get(id);

      sb.append(id)
          .append(" (desde: ")
          .append(fecha)
          .append("): ")
          .append(sensor.toString());

      // Si NO es el último elemento, añadimos una coma y un espacio
      if (i < listaSensores.size() - 1) {
        sb.append(", ");
      }
    }

    sb.append("]\n");

    return sb.toString();
  }
}