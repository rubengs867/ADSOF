package Practica4.sensor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
  protected Sensor(String tipo, double offset, Unidad unidad, double minRango, double maxRango) {
    if (minRango > maxRango) {
      throw new IllegalArgumentException("El rango mínimo no puede ser mayor que el máximo");
    }

    this.tipo = tipo;
    this.offset = offset;
    this.unidad = unidad;
    this.minRango = minRango;
    this.maxRango = maxRango;

    // Generación de ID por tipo
    int valorID = contadoresTipo.getOrDefault(tipo, 0) + 1;
    contadoresTipo.put(tipo, valorID);

    // Formato TIPO-NNNN: relleno con ceros a la izquierda hasta 4 dígitos
    this.id = String.format("%s-%04d", tipo, valorID);

    this.fechaUltimaLectura = null;
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
   * Reprsentación textual de un sensor. Formato:
   * <p>
   * (< VALOR >< UNIDAD >) útlima lectura: < FECHA >
   * </p>
   */
  @Override
  public String toString() {
    return "(" + this.valorUltimaLectura + this.unidad.getTexto() + ") útlima lectura: " + this.fechaUltimaLectura;
  }

  /**
   * Dos sensores son iguales si tienen el mismo identificador.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Sensor))
      return false;

    Sensor s = (Sensor) obj;
    return this.id.equals(s.getId());
  }
}