package Practica4.src.conversor;

import Practica4.src.excepcion.ConversionErroneaException;
import Practica4.src.sensor.Unidad;

public interface Conversor {
  
  Unidad getUnidadOrigen();

  Unidad getUnidadDestino();

  /**
   * esta funcion recibe un valor y segun el tipo de conversor
   * hace unas operaciones u otras
   * @param valor
   * @return
   */
  double convertir(double valor);

  /**
   * El default es porque la interfaz no requiere, ya que estamos metiendo
   * codigo en una interfaz
   * 
   * Le pasamos a conversor compuesto nuestro conversor como el siguiente
   * @param siguiente
   * @return
   */
  default Conversor concatenar(Conversor siguiente){
      

    if(this.getUnidadDestino() != siguiente.getUnidadOrigen()){
      throw new ConversionErroneaException(this.getUnidadDestino(), siguiente.getUnidadOrigen()); 
    }
    

    return new ConversorCompuesto(this, siguiente);
  }
}
