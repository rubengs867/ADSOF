package conversor;

import unidad.Unidad;

/**
 * Conversor neutro que devuelve el valor de entrada sin aplicar ninguna
 * transformación.
 * <p>
 * Se utiliza cuando las unidades de origen y destino son idénticas, actuando
 * como un elemento identidad en el sistema de procesamiento.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorIdentidad implements Conversor {

  /** Guardamos la unidad aunque devolvamos la misma unidad */
  private Unidad unidad;

  /**
   * Constructor que recibe la unidad original del sensor.
   * 
   * @param unidad La unidad en la que está midiendo el sensor por defecto.
   */
  public ConversorIdentidad(Unidad unidad) {
    this.unidad = unidad;
  }

  /**
   * Como es la identidad devuelve su propia unidad
   * 
   * @return unidad del conversor
   */
  @Override
  public Unidad getUnidadOrigen() {
    return this.unidad;
  }

  /**
   * Como es la identidad devuelve su propia unidad
   * 
   * @return unidad del conversor
   */
  @Override
  public Unidad getUnidadDestino() {
    return this.unidad;
  }

  /**
   * Devuelve el mismo valor, es como multiplicar por la matriz identidad
   * 
   * @param valor valor de entrada
   * @return double el mismo valor que entra
   */
  @Override
  public double convertir(double valor) {
    return valor;
  }
}