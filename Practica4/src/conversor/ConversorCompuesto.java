package conversor;

import unidad.Unidad;

/**
 * Clase que permite encadenar dos conversores para realizar transformaciones
 * múltiples.
 * <p>
 * Facilita la creación de rutas de conversión indirectas siempre que la unidad
 * de salida del primer conversor coincida con la de entrada del segundo.
 * </p>
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class ConversorCompuesto implements Conversor {

  /** Guardamos el primer conversor */
  private Conversor primero;

  /** Guardamos el segundo conversor */
  private Conversor segundo;

  /**
   * Constructor del conversor compuesto, recibe dos conversores
   * 
   * @param primero conversor con el primer set de unidades de cambio
   * @param segundo coonversor con el segundo set de unidades de cambio
   *                En este constructor no se comprueba si el cambio es posible,
   *                eso se hace en los metodos
   */
  public ConversorCompuesto(Conversor primero, Conversor segundo) {
    this.primero = primero;
    this.segundo = segundo;
  }

  /**
   * Devuelve la unidad de origen del primer conversor
   * 
   * @return Unidad del origen
   */
  @Override
  public Unidad getUnidadOrigen() {
    return primero.getUnidadOrigen();
  }

  /**
   * Devuelve la unidad destino del segundo conversor
   * 
   * @return Unidad del conversor destino
   */
  @Override
  public Unidad getUnidadDestino() {
    return segundo.getUnidadDestino();
  }

  /**
   * Convierte sucesivamente, yendo a traves de los conversores
   * 
   * @param double valor a convertir
   * @return double el valor convertido
   */
  @Override
  public double convertir(double valor) {
    // Convertimos el valor del primer conversor
    double valorIntermedio = primero.convertir(valor);

    // repetimos con lo mismo pero con el valor intermedio
    return segundo.convertir(valorIntermedio);
  }
}