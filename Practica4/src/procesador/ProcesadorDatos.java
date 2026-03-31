package Practica4.src.procesador;

import Practica4.src.conversor.*;
import Practica4.src.sensor.Unidad;
import java.time.LocalDateTime;
import java.util.*;

public class ProcesadorDatos {

  private Conversor conversor;

  private Map<LocalDateTime, Double> historial;

  public ProcesadorDatos(Unidad unidadOrigen) {
    this.conversor = new ConversorIdentidad(unidadOrigen);
    //usamos treemap para ordenar por fechas
    this.historial = new TreeMap<>();
  }

  public void setConversor(Conversor conversor) {
    this.conversor = conversor;
  }

  /**
   * Convierte el dato y lo guarda en el mapa usando la fecha como clave.
   */
  public void procesarLectura(LocalDateTime fechaHora, double valorLeido) {
    double valorConvertido = this.conversor.convertir(valorLeido);
    this.historial.put(fechaHora, valorConvertido);
  }

  public double getMinimo(){
    if(this.historial.isEmpty()) return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).min().getAsDouble();
  }

  public double getMaximo(){
    if(this.historial.isEmpty()) return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).max().getAsDouble();
  }

  public double getMedia(){
    if(this.historial.isEmpty()) return 0.0;
    return this.historial.values().stream().mapToDouble(u -> u).average().getAsDouble();
  }
}
