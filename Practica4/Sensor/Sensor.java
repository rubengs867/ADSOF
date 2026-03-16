package Practica4.Sensor;

import java.time.*;

public abstract class Sensor {
  private String id;
  private double offset;
  private Unidad unidad;
  private LocalDate fecha;
  private double ultima_lectura;

  public Sensor(String id, double offset, Unidad unidad, double lectura){
    this.id = id;
    this.offset = offset;
    this.unidad = unidad;
    this.fecha = LocalDate.now();
    this.ultima_lectura = lectura;
  }

  public String getId(){
    return this.id;
  }

  public double getOffset(){
    return this.offset;
  }

  public Unidad getUnidad(){
    return this.unidad;
  }

  public double getUltimaLectura(){
    return this.ultima_lectura;
  }
}
