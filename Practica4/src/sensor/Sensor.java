package sensor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import estrategia.EstrategiaLectura;
import excepcion.CambioBruscoException;
import excepcion.SensorDescalibradoException;
import excepcion.SensorDescalibradoPorCaducidadException;
import excepcion.SensorDescalibradoPorRangoException;
import procesador.ProcesadorDatos;
import unidad.Unidad;

/**
 * Clase abstracta que representa el comportamiento base de cualquier tipo de
 * sensor.
 * @author Alejandro Seguido
 * @author Rubén García
 */
public abstract class Sensor {

  /** Identificador único del sensor (formato: TIPO-NNNN). */
  private String id;

  /** Tipo de sensor. */
  private String tipo;

  /** Valor de calibración. */
  private double offset;

  /**
   * Unidad de medida del sensor. Protegido para poder ser accedido desde las
   * clases hijas
   */
  protected Unidad unidad;

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

  /**
   * Indica si el offset del sensor no está calibrado.
   */
  private boolean calibrado;

  /** Porcentaje máximo de cambio permitido entre lecturas (por defecto 50%) */
  private double umbralCambio;

  /** El Historial de lecturas del sensor va a pasar al procesador de datos */
  private List<Double> historial;

  /**
   * Mapa que mantiene un contador independiente por cada tipo de sensor.
   * <p>
   * La clave representa el prefijo del sensor y el valor indica cuántas
   * instancias de ese tipo se han creado.
   * </p>
   */
  private static Map<Class<? extends Sensor>, Integer> contadoresTipo = new HashMap<>();

  /**
   * Constructor protegido para ser utilizado por las clases hijas.
   * <p>
   * Este constructor inicializa los atributos comunes del sensor y genera
   * automáticamente un identificador único basado en el tipo.
   * </p>
   * Se establece un procesador con el conversor Identidad por defecto
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
    this.historial = new ArrayList<>();
    this.procesador = new ProcesadorDatos(unidad);
    this.fechaCalibracion = LocalDate.now();
    this.fechaUltimaLectura = LocalDate.now();
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
    // Obtenemos la clase que se está instanciando
    Class<? extends Sensor> claseHija = this.getClass();

    // Buscamos en el mapa usando la clase
    int valorID = contadoresTipo.getOrDefault(claseHija, 0) + 1;
    contadoresTipo.put(claseHija, valorID);

    this.id = String.format("%s-%04d", tipo, valorID);
  }

  /**
   * Ejecuta el proceso completo de medición del sensor, validando su estado de
   * calibración y la integridad de los datos obtenidos.
   * *
   * <p>
   * El método sigue esta secuencia lógica:
   * 1. Verifica si la calibración ha expirado o si el sensor está marcado como
   * descalibrado.
   * 2. Obtiene un valor simulado a través de la {@link EstrategiaLectura}
   * configurada.
   * 3. Aplica el offset de calibración al valor obtenido.
   * 4. Valida que el valor final esté dentro del rango operativo
   * ({@code minRango} - {@code maxRango}).
   * 5. Comprueba si existe un cambio brusco respecto a la última lectura (por
   * defecto > 50%).
   * 6. Si todas las validaciones pasan, actualiza el estado interno y notifica
   * al {@link ProcesadorDatos}.
   * </p>
   *
   * @throws SensorDescalibradoPorCaducidadException Si la fecha actual supera el
   *                                                 periodo de validez
   *                                                 de la última calibración.
   * @throws SensorDescalibradoPorRangoException     Si el valor medido está fuera
   *                                                 de los límites permitidos.
   *                                                 Esta excepción marca
   *                                                 automáticamente el sensor
   *                                                 como no calibrado.
   * @throws SensorDescalibradoException             Si el sensor no tiene un
   *                                                 <i>offset</i> válido o ha
   *                                                 sido
   *                                                 invalidado manualmente.
   * @throws CambioBruscoException                   Si la diferencia porcentual
   *                                                 entre la lectura actual y la
   *                                                 anterior
   *                                                 supera el
   *                                                 {@code umbralCambio}
   *                                                 configurado.
   */
  public void realizarMedicion() throws SensorDescalibradoException, CambioBruscoException {

    // Comprobación de caducación de la calibración del sensor
    if (estaCalibracionCaducada()) {
      throw new SensorDescalibradoPorCaducidadException(this);

    // Descalibrado por alguna lectura anterior
    } else if (!this.calibrado) {
      throw new SensorDescalibradoException(this, "Offset del sensor descalibrado");
    }

    // Obtenemos el valor de la estrategia
    double valor = estrategia.generarValor(this);

    // Aplicamos el offset
    double valorFinal = valor - this.offset;

    // ¿Medición dentro del rango del sensor?
    if (valorFinal < minRango || valorFinal > maxRango) {
      double desviacion = calcularPorcentajeDesviacion(valorFinal, minRango, maxRango);
      throw new SensorDescalibradoPorRangoException(this, desviacion);
    }

    // Comprobación de un cambio brusco en la medición
    boolean cambioBrusco = false;
    double valorAnteriorTemporal = this.valorUltimaLectura;

    if (!primeraLectura()) {
      double diferencia = Math.abs(valorFinal - valorAnteriorTemporal);

      // Nos protegemos con la division por cero
      double porcentajeCambio = 0.0;
      if (valorAnteriorTemporal != 0) {
        porcentajeCambio = diferencia / Math.abs(valorAnteriorTemporal);
      }
      if (porcentajeCambio > this.umbralCambio) {
        cambioBrusco = true;
      }
    }
    // Registramos la lectura
    this.valorUltimaLectura = valorFinal;
    this.fechaUltimaLectura = LocalDate.now();
    this.historial.add(valorFinal);
    this.procesador.procesarLectura(this.fechaUltimaLectura.atStartOfDay(), valorFinal);

    // lanzamos excecpion
    if (cambioBrusco) {
      throw new CambioBruscoException(this, valorAnteriorTemporal, valorFinal);
    }
  }

  /**
   * Calcula el porcentaje de desviación de un valor respecto al rango permitido.
   * El porcentaje representa qué parte de la amplitud total del rango supone el
   * exceso.
   * 
   * @param valor El valor medido.
   * @param min   El límite inferior.
   * @param max   El límite superior.
   * @return Porcentaje de desviación (0.0 si está dentro del rango).
   */
  private double calcularPorcentajeDesviacion(double valor, double min, double max) {
    // Si está dentro del rango, la desviación es cero
    if (valor >= min && valor <= max) {
      return 0.0;
    }

    double amplitud = max - min;

    // Evitamos división por cero si el rango es un punto único
    if (amplitud == 0) {
      return 100.0;
    }

    double exceso = (valor < min) ? (min - valor) : (valor - max);

    return (exceso / amplitud) * 100.0;
  }

  /**
   * Comprueba si el sensor ha realizado alguna lectura mirando su historial
   * * @return {@code true} si NO ha realizado lecturas;
   * {@code false} en caso contrario.
   */
  public boolean primeraLectura() {
    return this.historial.isEmpty();
  }

  /**
   * Devuelve una lista inmutable del historial de lecturas del sensor.
   * * @return lista con los valores en bruto registrados por el sensor.
   */
  public List<Double> getHistoricoLecturas() {
    // Devuelve su propia lista, protegida contra modificaciones externas
    return List.copyOf(this.historial);
  }

  /**
   * Devuelve el procesador de datos del sensor.
   * 
   * @return objeto {@link ProcesadorDatos}
   */
  public ProcesadorDatos getProcesadorDatos() {
    return this.procesador;
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
   * Devuelve la duración de la calibración en días.
   * 
   * @return número de días
   */
  public int getDuracionCalibracionDias() {
    return duracionCalibracionDias;
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
   * Resetea el contador de tipos de sensores que se usa en la generación del ID
   */
  public static void resetContador() {
    contadoresTipo.clear();
  }

  /**
   * Calibra el sensor estableciendo un nuevo offset y reiniciando el periodo de
   * validez.
   * <p>
   * Por defecto, la calibración tendrá una duración de 365 días.
   * </p>
   * 
   * @param offset Nuevo valor de ajuste para las mediciones.
   */
  public void calibrar(double offset) {
    this.calibrar(offset, 365);
  }

  /**
   * Calibra el sensor estableciendo un nuevo offset y una duración específica de
   * validez.
   * <p>
   * Establece el sensor como calibrado y actualiza la fecha de referencia.
   * </p>
   * 
   * @param offset      Nuevo offset de calibración.
   * @param diasValidez Número de días que la calibración será considerada válida.
   */
  public void calibrar(double offset, int diasValidez) {
    this.fechaCalibracion = LocalDate.now();
    this.offset = offset;
    this.duracionCalibracionDias = diasValidez;
    this.calibrado = true;
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
   * Comprueba si la calibración del sensor está caducada o si directamente no
   * está calibrado.
   * 
   * @return {@code true} si está calibrado;
   *         {@code false} si la fecha de caducación ya ha pasado o ha realizado
   *         una medición fuera del rango.
   */
  public boolean estaCalibrado() {
    return calibrado && !estaCalibracionCaducada();
  }

  /**
   * Comprueba si la fecha de caducación de la calibración ya ha pasado.
   * 
   * @return {@code true} si la fecha de caducación ya ha pasado;
   *         {@code false} en caso contrario
   */
  public boolean estaCalibracionCaducada() {
    // Comprueba si ha pasado la fecha de caducidad
    LocalDate fechaCaducidad = this.fechaCalibracion.plusDays(this.duracionCalibracionDias);
    if (LocalDate.now().isAfter(fechaCaducidad))
      return true;

    return false;
  }

  /**
   * Establece el estado de la calibración de un sensor. Si el offset está
   * caducado o no.
   * 
   * @param estado
   */
  public void setCalibrado(boolean estado) {
    this.calibrado = estado;
  }

  /**
   * Indica si el offset del sensor está descalibrado.
   * 
   * @return {@code true} si está calibrado;
   *         {@code false} en caso contrario
   */
  public boolean getCalibrado() {
    return this.calibrado;
  }

  /**
   * Establece el umbral de cambio permitido entre lecturas para detectar cambios
   * 
   * @param umbralCambio
   */
  public void setUmbralCambio(double umbralCambio) {
    this.umbralCambio = umbralCambio;
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
   * Representación textual del sensor con el procesador.
   */
  public String procesadorDatoString() {
    return this.id + " ("+this.unidad.getTexto()+"): " + procesador.toString();
  }

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