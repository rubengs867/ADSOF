package Practica4.test.main;

import Practica4.src.estacion.EstacionMeteorologica;
import Practica4.src.estacion.Ubicacion;
import Practica4.src.sensor.Sensor;
import Practica4.src.unidad.Unidad;
import Practica4.src.unidad.UnidadTemperatura;
import Practica4.src.estrategia.EstrategiaLectura;
import Practica4.src.formateador.FormateadorHTML;
import Practica4.src.formateador.FormateadorMarkdown;
import Practica4.src.documento.IFormateador;

public class Main {

  public static void main(String[] args) {
    try {
      Ubicacion ubicacion = new Ubicacion(40.4168, -3.7038);
      EstacionMeteorologica estacion = new EstacionMeteorologica("Madrid Centro", ubicacion);

      EstrategiaLectura lecturaNormal = sensor -> 22.5;
      EstrategiaLectura lecturaPeligrosa = sensor -> 105.0; // Provocará alerta por salir del rango

      Sensor temp1 = new Sensor("TEMP", 0.0, UnidadTemperatura.CELSIUS, -10.0, 50.0, lecturaNormal) {
        @Override
        public void setUnidad(Unidad u) {
        }

        @Override
        public String toString() {
          return "TEMP-0001 (ºC): [22.50] -- MIN: 22.50 MAX: 22.50 AVG: 22.50";
        }
      };

      Sensor hum1 = new Sensor("HUM", 0.0, UnidadTemperatura.CELSIUS, 0.0, 100.0, lecturaPeligrosa) {
        @Override
        public void setUnidad(Unidad u) {
        }

        @Override
        public String toString() {
          // Si está fuera de rango no medirá, simulamos que antes tenía una lectura buena
          return "HUM-0001 (%): [45.00] -- MIN: 45.00 MAX: 45.00 AVG: 45.00";
        }
      };

      estacion.addSensor(temp1);
      estacion.addSensor(hum1);


      System.out.println("Realizando mediciones...\n");
      estacion.realizarLecturaPuntual(-1);

      //Probamos el formateo a HTML
      IFormateador html = new FormateadorHTML();
      System.out.println("          SALIDA EN FORMATO HTML         ");
      System.out.println(html.formatear(estacion));

      //Probamos el formateo a Markdown
      IFormateador md = new FormateadorMarkdown();
      System.out.println("        SALIDA EN FORMATO MARKDOWN       ");
      System.out.println(md.formatear(estacion));

    } catch (Exception e) {
      System.err.println("Error grave durante la simulación: " + e.getMessage());
    }
  }
}