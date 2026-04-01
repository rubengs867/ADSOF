package Practica4.src.conversor;

import Practica4.src.sensor.Unidad;

public class ConversorKelvinFahrenheit implements Conversor {
    @Override
    public Unidad getUnidadOrigen() {
        return Unidad.KELVIN;
    }

    @Override
    public Unidad getUnidadDestino() {
        return Unidad.FAHRENHEIT;
    }

    @Override
    public double convertir(double valor) {
        return (valor - 273.15) * 9 / 5 + 32;
    }
}