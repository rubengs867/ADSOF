package Practica4.src.excepcion;

import Practica4.src.sensor.Unidad;

public class ConversionErroneaException extends RuntimeException {
  
    // Constructor básico con un mensaje de texto
    public ConversionErroneaException(String mensaje) {
        super(mensaje);
    }


    public ConversionErroneaException(Unidad origen, Unidad destino) {
        super("Error de compatibilidad: No se puede conectar una salida en " + origen + 
              " con una entrada en " + destino + ".");
    }
}