package conversor;

import excepcion.ConversionErroneaException;
import unidad.Unidad;

/**
 * Interfaz que define los métodos base para la transformación de valores entre
 * distintas unidades.
 * 
 * @author Alejandro Seguido
 * @author Rubén García
 */
public interface Conversor {

  /** Unidad origen */
  Unidad getUnidadOrigen();

  /** Unidad destino */
  Unidad getUnidadDestino();

  /**
   * esta funcion recibe un valor y segun el tipo de conversor
   * hace unas operaciones u otras
   * 
   * @param valor
   * @return
   */
  double convertir(double valor);

  /**
   * El default es porque la interfaz no requiere, ya que estamos metiendo
   * codigo en una interfaz
   * 
   * Le pasamos a conversor compuesto nuestro conversor como el siguiente
   * 
   * @param siguiente
   * @return
   */
  default Conversor concatenar(Conversor siguiente) {

    if (this.getUnidadDestino() != siguiente.getUnidadOrigen()) {
      throw new ConversionErroneaException(this.getUnidadDestino(), siguiente.getUnidadOrigen());
    }

    return new ConversorCompuesto(this, siguiente);
  }
}
