package apartado4;

/**
 * Representa una condición meteorológica compuesta por estado del cielo
 * y temperatura.
 */
public class Weather {

  private WeatherCondition condition;
  private Temperature temperature;

  /**
   * Crea una instancia meteorológica.
   *
   * @param condition   estado del cielo
   * @param temperature nivel de temperatura
   */
  public Weather(WeatherCondition condition, Temperature temperature) {
    this.condition = condition;
    this.temperature = temperature;
  }

  /**
   * Devuelve la condición atmosférica.
   *
   * @return condición meteorológica
   */
  public WeatherCondition getCondition() {
    return condition;
  }

  /**
   * Devuelve la temperatura asociada.
   *
   * @return temperatura registrada
   */
  public Temperature getTemperature() {
    return temperature;
  }

  /**
   * Representación textual del objeto.
   *
   * @return cadena descriptiva
   */
  @Override
  public String toString() {
    return "Weather(" + condition + ", " + temperature + ")";
  }
}