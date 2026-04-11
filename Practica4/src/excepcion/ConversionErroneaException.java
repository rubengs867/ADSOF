package Practica4.src.excepcion;

import Practica4.src.unidad.Unidad;

/**
 * Esta excepcion salta cuando se intentan transformar unidades que no pueden
 * por ejemplo de celsius a pascales
 */
public class ConversionErroneaException extends RuntimeException {
  
    /**
     * Recibe un mensaje y lo manda al constructor padre
     * @param mensaje de error que se quiere enviar
     */
    public ConversionErroneaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Mensaje completo, con el error descrito en el mensaje y las unidades que se querian cambiar
     * @param origen Unidad origen de cambio
     * @param destino Unidad destino de cambio
     */
    public ConversionErroneaException(Unidad origen, Unidad destino) {
        super("Error de compatibilidad: No se puede conectar una salida en " + origen + 
              " con una entrada en " + destino + ".");
    }
}