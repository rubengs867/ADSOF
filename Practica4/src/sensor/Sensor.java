package Practica4.src.sensor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Practica4.src.estrategia.EstrategiaLectura;
import Practica4.src.excepcion.CambioBruscoException;
import Practica4.src.excepcion.SensorDescalibradoException;
import Practica4.src.procesador.ProcesadorDatos;
import Practica4.src.unidad.Unidad;

/**
 * Clase abstracta que representa el comportamiento base de cualquier tipo de
 * sensor.
 */
public abstract class Sensor {

  /** Identificador único del sensor (formato: TIPO-NNNN). */
  private String id;

  /** Tipo de sensor. */
  private String tipo;

  /** Valor de calibración. */
  private double offset;

  /** Unidad de medida del sensor. */
  private Unidad unidad;

  /** Fecha en la que se realizó la última lectura. */
  private LocalDate fechaUltimaLectura;

  /** Valor registrado en la última lectura. */
  private double valorUltimaLectura;

  /** Límite inferior del rango operativo del sensor. */
  private double minRango;

  /** Límite superior del rango operativo del sensor. */
  private double maxRango;

  /** Estrategia usada para realizar lecturas */
  private EstrategiaLectura estrategia;

  /** Procesador asociado al sensor (Apartado 3) */
  private ProcesadorDatos procesador;

  /** Fecha de la última vez que se calibró el sensor */
  private LocalDate fechaCalibracion;

  /** Duración de la calibración en días (por defecto 365) */
  private int duracionCalibracionDias;

  /** Porcentaje máximo de cambio permitido entre lecturas (por defecto 50%) */
  private double umbralCambio;

  /** Historial de lecturas del sensor */
  private List<Double> historicoLecturas = new ArrayList<>();

  /**
   * Mapa que mantiene un contador independiente por cada tipo de sensor.
   * <p>
   * La clave representa el prefijo del sensor y el valor indica cuántas
   * instancias de ese tipo se han creado.
   * </p>
   */
  private static Map<String, Integer> contadoresTipo = new HashMap<>();

  /**
   * Constructor protegido para ser utilizado por las clases hijas.
   * <p>
   * Este constructor inicializa los atributos comunes del sensor y genera
   * automáticamente un identificador único basado en el tipo.
   * </p>
   *
   * @param tipo     Prefijo identificador del tipo de sensor.
   *                 Se utiliza para generar el ID único.
   * @param offset   Ajuste de calibración.
   * @param unidad   Unidad de medida asociada al sensor.
   * @param minRango Valor mínimo permitido dentro del rango operativo.
   * @param maxRango Valor máximo permitido dentro del rango operativo.
   *
   * @throws IllegalArgumentException si {@code minRango} es mayor que
   *                                  {@code maxRango}.
   */
  protected Sensor(String tipo, double offset, Unidad unidad, double minRango, double maxRango,
      EstrategiaLectura estrategia) {
    if (minRango > maxRango) {
      throw new IllegalArgumentException("El rango mínimo no puede ser mayor que el máximo");
    }

    this.tipo = tipo;
    this.offset = offset;
    this.unidad = unidad;
    this.minRango = minRango;
    this.maxRango = maxRango;
    this.estrategia = estrategia;
    generarValorID(tipo);
    this.fechaUltimaLectura = null;

    this.procesador = new ProcesadorDatos(unidad);
    this.fechaCalibracion = LocalDate.now();
    this.duracionCalibracionDias = 365;
    this.umbralCambio = 0.5;
  }

  protected Sensor(String tipo, double offset, Unidad unidad, double minRango, double maxRango,
      EstrategiaLectura estrategia, int duracionCalibracionDias, double umbralCambio) {
    this(tipo, offset, unidad, minRango, maxRango, estrategia);
    this.duracionCalibracionDias = duracionCalibracionDias;
    this.umbralCambio = umbralCambio;
  }

  /**
   * Genera el valor del ID en función del tipo de sensor que sea y el número de
   * sensores de ese tipo que se hayan creado.
   * <p>
   * Formato: TIPO-NNNN (ej. TEMP-0001)
   * </p>
   * 
   * @param tipo
   */
  private void generarValorID(String tipo) {
    // Generación de ID por tipo
    int valorID = contadoresTipo.getOrDefault(tipo, 0) + 1;
    contadoresTipo.put(tipo, valorID);

    // Formato TIPO-NNNN: relleno con ceros a la izquierda hasta 4 dígitos
    this.id = String.format("%s-%04d", tipo, valorID);
  }

  /**
   * Realiza la medición del sensor, actualizando su estado y registrando la nueva
   * lectura en el histórico.
   */
  public void realizarMedicion() throws SensorDescalibradoException, CambioBruscoException {

    // primera excepcion
    if (estaCaducadaCalibracion()) {
      throw new SensorDescalibradoException(this, "la fecha de hoy excede la fecha de calibracion");
    }
    // Obtenemos el valor de la estrategia
    double valor = estrategia.generarValor(this);

    // Aplicamos el offset
    double valorFinal = valor - this.offset;

    // segunda excepcion
    if (valorFinal < minRango || valorFinal > maxRango) {
      throw new SensorDescalibradoException(this, "el valor final excede los valores permitidos");
    }

    // tercera excepcion
    boolean cambioBrusco = false;
    if (!primeraLectura()) {
      double diferencia = Math.abs(valorFinal - this.valorUltimaLectura);

      // nos protegemos con la division por cero
      double porcentajeCambio = 0.0;
      if (this.valorUltimaLectura != 0) {
        porcentajeCambio = diferencia / Math.abs(this.valorUltimaLectura);
      }
      if (porcentajeCambio > this.umbralCambio) {
        cambioBrusco = true;
      }

    }

    if (cambioBrusco) {
      throw new CambioBruscoException(this, this.valorUltimaLectura, valorFinal);
    }

    // Registramos la lectura
    this.valorUltimaLectura = valorFinal;
    this.fechaUltimaLectura = LocalDate.now();

    // Guardamos en el histórico
    this.historicoLecturas.add(valorFinal);

  }

  /**
   * Comprueba si el sensor ha realizado alguna lectura.
   * 
   * @return {@code true} si NO ha realizado lecturas;
   *         {@code false} en caso contrario.
   */
  public boolean primeraLectura() {
    return historicoLecturas.isEmpty();
  }

  /**
   * Devuelve una lista inmutable del historial de lecturas del sensor.
   * 
   * @return lista con los valores de las lecturas.
   */
  public List<Double> getHistoricoLecturas() {
    return List.copyOf(this.historicoLecturas);
  }

  /**
   * Devuelve el identificador único del sensor.
   *
   * @return ID del sensor en formato PREFIJO-XXXX.
   */
  public String getId() {
    return id;
  }

  /**
   * Devuelve la representación textual del tipo de sensor.
   * 
   * @return tipo de sensor (ej: HUM, PRES)
   */
  public String getTipo() {
    return tipo;
  }

  /**
   * Devuelve el valor de offset aplicado al sensor.
   *
   * @return offset de calibración.
   */
  public double getOffset() {
    return offset;
  }

  /**
   * Devuelve el límite inferior del rango operativo.
   *
   * @return valor mínimo permitido.
   */
  public double getMinRango() {
    return minRango;
  }

  /**
   * Devuelve el límite superior del rango operativo.
   *
   * @return valor máximo permitido.
   */
  public double getMaxRango() {
    return maxRango;
  }

  /**
   * Devuelve el valor de la última lectura del sensor.
   * 
   * @return valor guardado de la última lectura.
   */
  public double getValorUltimaLectura() {
    return valorUltimaLectura;
  }

  /**
   * Devuelve la fecha de la última lectura del sensor.
   * 
   * @return fecha de la última lectura
   */
  public LocalDate getFechaUltimaLectura() {
    return fechaUltimaLectura;
  }

  /**
   * Devuelve la unidad de medida que usa el sensor.
   * 
   * @return objeto {@link Unidad}
   */
  public Unidad getUnidad() {
    return unidad;
  }

  /**
   * Establece la unidad de medida que usa el sensor.
   * 
   * @param u unidad de medida a establecer
   */
  public abstract void setUnidad(Unidad u);

  /**
   * Establece el offset que va a usar el sensor para calibrar el sensor.
   * Establece el sensor como calibrado y actualiza la última fecha de
   * calibración.
   * 
   * @param offset nuevo offset de calibración
   */
  public void calibrar(double offset) {
    fechaCalibracion = LocalDate.now();
    this.offset = offset;
  }

  /**
   * Establece el número de días que deben pasar para que caduque la calibración
   * 
   * @param duracionCalibracionDias nueva duración de la calibración en días
   */
  public void setCaducacionCalibracion(int duracionCalibracionDias) {
    this.duracionCalibracionDias = duracionCalibracionDias;
  }

  /**
   * Comprueba si la calibración del sensor ha caducado
   * 
   * @return {@code true} si ha caducado;
   *         {@code false} en caso contrario
   */
  public boolean estaCaducadaCalibracion() {
    LocalDate fechaCaducidad = this.fechaCalibracion.plusDays(this.duracionCalibracionDias);
    if (LocalDate.now().isAfter(fechaCaducidad))
      return true;
    return false;
  }

  /**
   * Reprsentación textual de un sensor. Formato:
   * <p>
   * (< VALOR >< UNIDAD >) útlima lectura: < FECHA >
   * </p>
   */
  @Override
  public abstract String toString();

  /**
   * Compara este sensor con otro objeto para determinar su igualdad.
   * <p>
   * Dos sensores se consideran iguales si y solo si tienen el mismo
   * identificador (ID).
   * </p>
   * 
   * @param obj Objeto con el que se desea comparar.
   * @return {@code true} si los IDs coinciden o si es la misma instancia de
   *         memoria;
   *         {@code false} en caso contrario o si el objeto es nulo/de otra clase.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || !(obj instanceof Sensor))
      return false;
    Sensor other = (Sensor) obj;
    return this.getId().equals(other.getId());
  }

  /**
   * Genera un código hash para el sensor basado en su identificador único.
   * 
   * @return Un valor entero que representa el código hash del ID del sensor.
   */
  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }
}