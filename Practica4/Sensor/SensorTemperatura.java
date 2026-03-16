package Practica4.Sensor;

public class SensorTemperatura extends Sensor {

  private double min = -273.15;
  private double max = 1000.0;
  // Constructor donde pasas la unidad
  public SensorTemperatura(String id, double offset, Unidad unidad, double lectura) {
    // 1. Llamamos al constructor de la clase padre
    super(id, offset, unidad, lectura);

    // 2. Comprobamos si la unidad es correcta usando el metodo del Enum
    if (!unidad.isTemperatura()) {
      throw new IllegalArgumentException("Error: Un sensor de temperatura no puede usar la unidad " + unidad);
    }

    if(lectura < min || lectura > max){
      throw new IllegalArgumentException("Error el valor está fuera de rango");
    }
  }

  // Constructor por defecto, con celsius como unidad por defecto
  public SensorTemperatura(String id, double offset, double lectura) {
    super(id, offset, Unidad.CELSIUS, lectura);
  }
}