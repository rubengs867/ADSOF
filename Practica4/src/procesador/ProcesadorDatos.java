package Practica4.src.procesador;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import Practica4.src.conversor.Conversor;
import Practica4.src.conversor.ConversorIdentidad;
import Practica4.src.sensor.Unidad;

public class ProcesadorDatos {

  private Conversor conversor;

  private Map<LocalDateTime, Double> historial;

  public ProcesadorDatos(Unidad unidadOrigen) {
    this.conversor = new ConversorIdentidad(unidadOrigen);
    this.historial = new HashMap<>();
  }
}
