package procesador;

import conversor.*;
import excepcion.ConversionErroneaException;
import unidad.Unidad;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Clase encargada de gestionar el procesamiento de los datos generados por un
 * sensor.
 * Almacena un historial de las lecturas y permite
 * aplicar conversores para transformar las unidades de las lecturas antes de
 * ser
 * guardadas. También proporciona las estadisticas basicas, minimo, maximo y
 * media.
 */
public class ProcesadorDatos {

  /** Conversor aplicado a los datos antes de almacenarlos. */
  private Conversor conversor;

  /** Unidad base de lectura, debe corresponder con el sensor al que se asocia */
  private Unidad unidadBase;

  /**
   * Historial de lecturas almacenadas.
   * Utiliza un TreeMap para garantizar que las lecturas se mantengan ordenadas
   * por fecha y hora.
   */
  private Map<LocalDateTime, Double> historial;

  /**
   * Constructor del procesador de datos.
   * Inicializa el historial vacío y configura un conversor identidad por defecto,
   *
   * @param unidadOrigen La unidad de medida original del sensor al que está
   *                     asociado.
   */
  public ProcesadorDatos(Unidad unidadOrigen) {
    this.unidadBase = unidadOrigen;
    this.conversor = new ConversorIdentidad(unidadOrigen);
    // Usamos TreeMap para ordenar automáticamente por fechas
    this.historial = new TreeMap<>();
  }

  /**
   * Establece un nuevo conversor para procesar las lecturas futuras.
   * Se comprueba que se pueda convertir dicha unidad
   * 
   * @param conversor El nuevo conversor a aplicar (puede ser simple, compuesto o
   *                  identidad).
   */
  public void setConversor(Conversor conversor) throws ConversionErroneaException {
    if (!conversorCompatible(conversor)) {
      throw new ConversionErroneaException(this.unidadBase, conversor.getUnidadOrigen());
    }
    this.conversor = conversor;
  }

  /**
   * Determina si un conversor es comptible con el procesador
   * 
   * @param conversor Conversor a evaluar
   * @return {@code true} es comptible;
   *         {@code false} en caso contrario.
   */
  private boolean conversorCompatible(Conversor conversor) {
    return conversor.getUnidadOrigen() == unidadBase;
  }

  /**
   * Procesa una lectura obtenida del sensor, la convierte utilizando el conversor
   * actual y la almacena en el historial cronológico.
   *
   * @param fechaHora  La fecha y hora exacta en la que se realizó la medición.
   * @param valorLeido El valor bruto leído por el sensor (ya con el offset
   *                   aplicado).
   */
  public void procesarLectura(LocalDateTime fechaHora, double valorLeido) {
    double valorConvertido = this.conversor.convertir(valorLeido);
    this.historial.put(fechaHora, valorConvertido);
  }

  /**
   * Obtiene el valor mínimo registrado en el historial de lecturas procesadas.
   *
   * @return El valor mínimo almacenado, o 0.0 si el historial está vacío.
   */
  public double getMinimo() {
    if (this.historial.isEmpty())
      return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).min().getAsDouble();
  }

  /**
   * Obtiene el valor máximo registrado en el historial de lecturas procesadas.
   *
   * @return El valor máximo almacenado, o 0.0 si el historial está vacío.
   */
  public double getMaximo() {
    if (this.historial.isEmpty())
      return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).max().getAsDouble();
  }

  /**
   * Calcula y obtiene la media aritmética de todos los valores en el historial.
   *
   * @return La media de los valores almacenados, o 0.0 si el historial está
   *         vacío.
   */
  public double getMedia() {
    if (this.historial.isEmpty())
      return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).average().getAsDouble();
  }

  /**
   * Comprueba si el historial de lecturas está vacío.
   * 
   * @return true si no hay lecturas procesadas.
   */
  public boolean estaVacio() {
    return this.historial.isEmpty();
  }

  /**
   * Devuelve una lista con los valores del historial (sin las fechas).
   * 
   * @return Lista de valores convertidos.
   */
  public Collection<Double> getValores() {
    // Devuelve una vista protegida de los valores del mapa
    return Collections.unmodifiableCollection(this.historial.values());
  }

  /**
   * Representación textual de un procesador de datos siguiendo el formato:
   * [val1, val2, ...] -- MIN: x.xx MAX: x.xx AVG: x.xx
   */
  @Override
  public String toString() {
    // Obtenemos los valores del historial y los formateamos a 2 decimales
    List<String> valoresFormateados = this.historial.values().stream()
        .map(v -> String.format("%.2f", v))
        .toList();

    // Construimos la cadena final
    return String.format("%s -- MIN: %.2f MAX: %.2f AVG: %.2f",
        valoresFormateados.toString(), // Esto genera el formato [20.50, 20.50, ...]
        getMinimo(),
        getMaximo(),
        getMedia());
  }
}