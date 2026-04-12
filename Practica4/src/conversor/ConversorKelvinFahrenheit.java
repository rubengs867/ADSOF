package conversor;

import unidad.Unidad;
import unidad.UnidadTemperatura;

/**
 * Clase que convierte de kelvin a Fahrenheit
 */
public class ConversorKelvinFahrenheit implements Conversor {
    /**
     * Implementa la funcion de la interfaz
     * 
     * @return Unidad de origen, Kelvin
     */
    @Override
    public Unidad getUnidadOrigen() {
        return UnidadTemperatura.KELVIN;
    }

    /**
     * Implementa la funcion de la interfaz
     * 
     * @return Unidad de destino, Fahrenheit
     */
    @Override
    public Unidad getUnidadDestino() {
        return UnidadTemperatura.FAHRENHEIT;
    }

    /**
     * Dado un valor el kelvin, te lo devuelve en fahrenheit
     * Formula mirada en internet
     * 
     * @return double el valor en fahrenheit
     */
    @Override
    public double convertir(double valor) {
        return (valor - 273.15) * 9 / 5 + 32;
    }
}