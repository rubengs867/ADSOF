package Practica4.excepcion;

import Practica4.sensor.Unidad; // Necesario para el segundo constructor

public class ConversionErronea extends RuntimeException {
  
    // Constructor básico con un mensaje de texto
    public ConversionErronea(String mensaje) {
        super(mensaje);
    }


    public ConversionErronea(Unidad origen, Unidad destino) {
        super("Error de compatibilidad: No se puede conectar una salida en " + origen + 
              " con una entrada en " + destino + ".");
    }
}