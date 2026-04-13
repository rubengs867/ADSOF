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

  /**
   * Obtiene la unidad de medida a partir de la cual se realiza la conversión.
   * 
   * @return La unidad de origen.
   */
  Unidad getUnidadOrigen();

  /**
   * Obtiene la unidad de medida resultante tras aplicar la conversión.
   * 
   * @return La unidad de destino.
   */
  Unidad getUnidadDestino();

  /**
   * Aplica la lógica de conversión sobre un valor numérico.
   *
   * @param valor El valor original en la unidad de origen.
   * @return El valor transformado a la unidad de destino.
   */
  double convertir(double valor);

  /**
   * Crea un conversor compuesto encadenando este conversor con otro.
   * <p>
   * Permite realizar conversiones en cadena siempre que la unidad de destino
   * de este conversor coincida con la de origen del siguiente.
   * </p>
   *
   * @param siguiente El conversor que se aplicará a continuación.
   * @return Una nueva instancia de {@code ConversorCompuesto} que une ambos.
   * @throws ConversionErroneaException Si las unidades no son compatibles para el
   *                                    encadenamiento.
   */
  default Conversor concatenar(Conversor siguiente) throws ConversionErroneaException {
    if (this.getUnidadDestino() != siguiente.getUnidadOrigen()) {
      throw new ConversionErroneaException(this.getUnidadDestino(), siguiente.getUnidadOrigen());
    }

    return new ConversorCompuesto(this, siguiente);
  }
}
