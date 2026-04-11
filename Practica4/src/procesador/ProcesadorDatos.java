package Practica4.src.procesador;

import Practica4.src.conversor.*;
import Practica4.src.unidad.Unidad;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Clase encargada de gestionar el procesamiento de los datos generados por un sensor.
 * Almacena un historial de las lecturas  y permite
 * aplicar conversores para transformar las unidades de las lecturas antes de ser
 * guardadas. También proporciona las estadisticas basicas, minimo, maximo y media.
 */
public class ProcesadorDatos {

  /** Conversor aplicado a los datos antes de almacenarlos. */
  private Conversor conversor;

  /** * Historial de lecturas almacenadas. 
   * Utiliza un TreeMap para garantizar que las lecturas se mantengan ordenadas por fecha y hora.
   */
  private Map<LocalDateTime, Double> historial;

  /**
   * Constructor del procesador de datos.
   * Inicializa el historial vacío y configura un conversor identidad por defecto,
   *
   * @param unidadOrigen La unidad de medida original del sensor al que está asociado.
   */
  public ProcesadorDatos(Unidad unidadOrigen) {
    this.conversor = new ConversorIdentidad(unidadOrigen);
    // Usamos TreeMap para ordenar automáticamente por fechas
    this.historial = new TreeMap<>();
  }

  /**
   * Establece un nuevo conversor para procesar las lecturas futuras.
   *
   * @param conversor El nuevo conversor a aplicar (puede ser simple, compuesto o identidad).
   */
  public void setConversor(Conversor conversor) {
    this.conversor = conversor;
  }

  /**
   * Procesa una lectura obtenida del sensor, la convierte utilizando el conversor
   * actual y la almacena en el historial cronológico.
   *
   * @param fechaHora   La fecha y hora exacta en la que se realizó la medición.
   * @param valorLeido  El valor bruto leído por el sensor (ya con el offset aplicado).
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
    if(this.historial.isEmpty()) return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).min().getAsDouble();
  }

  /**
   * Obtiene el valor máximo registrado en el historial de lecturas procesadas.
   *
   * @return El valor máximo almacenado, o 0.0 si el historial está vacío.
   */
  public double getMaximo() {
    if(this.historial.isEmpty()) return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).max().getAsDouble();
  }

  /**
   * Calcula y obtiene la media aritmética de todos los valores en el historial.
   *
   * @return La media de los valores almacenados, o 0.0 si el historial está vacío.
   */
  public double getMedia() {
    if(this.historial.isEmpty()) return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).average().getAsDouble();
  }
}