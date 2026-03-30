package Practica4.Procesador;

import Practica4.Conversor.*;
import Practica4.sensor.Unidad;
import java.time.LocalDateTime;
import java.util.*;
public class ProcesadorDatos {
  
  private Conversor conversor;

  private Map <LocalDateTime, Double> historial;

  public ProcesadorDatos(Unidad unidadOrigen){
    this.conversor = new ConversorIdentidad(unidadOrigen);
    this.historial = new HashMap<>();
  }
}
