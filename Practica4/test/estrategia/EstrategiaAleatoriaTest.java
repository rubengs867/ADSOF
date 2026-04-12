package estrategia;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sensor.Sensor;
import unidad.Unidad;

public class EstrategiaAleatoriaTest {

  /**
   * Stub de Sensor para proporcionar rangos controlados a la estrategia.
   */
  private static class SensorStub extends Sensor {
    public SensorStub(double minRango, double maxRango) {
      super("STUB", 0.0, null, minRango, maxRango, null);
    }

    @Override
    public void setUnidad(Unidad u) {
      // No necesario para este test
    }

    @Override
    public String toString() {
      return "SensorStub";
    }
  }

  // ==========================================
  // TESTS DE GENERACIÓN DE VALORES
  // ==========================================

  @Test
  public void testGenerarValor_CuandoProbFueraRangoEsCero_EntoncesSiempreDentroDeRango() {
    double min = 10.0;
    double max = 20.0;
    EstrategiaAleatoria estrategia = new EstrategiaAleatoria(0.0);
    SensorStub sensor = new SensorStub(min, max);

    // Ejecutamos múltiples iteraciones para asegurar que la aleatoriedad no sale
    // del rango
    for (int i = 0; i < 500; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("El valor " + valor + " debe ser >= " + min, valor >= min);
      assertTrue("El valor " + valor + " debe ser <= " + max, valor <= max);
    }
  }

  @Test
  public void testGenerarValor_CuandoProbFueraRangoEsUno_EntoncesSiempreFueraDeRango() {
    double max = 100.0;
    EstrategiaAleatoria estrategia = new EstrategiaAleatoria(1.0);
    SensorStub sensor = new SensorStub(0.0, max);

    for (int i = 0; i < 500; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("El valor " + valor + " debe ser estrictamente mayor que max (" + max + ")", valor > max);
    }
  }

  @Test
  public void testGenerarValor_CuandoSensorTieneRangosNegativos_EntoncesRespetaLimites() {
    double min = -100.0;
    double max = -50.0;
    EstrategiaAleatoria estrategia = new EstrategiaAleatoria(0.0);
    SensorStub sensor = new SensorStub(min, max);

    for (int i = 0; i < 100; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("Debe respetar límites negativos: " + valor, valor >= min && valor <= max);
    }
  }

  @Test
  public void testGenerarValor_CuandoRangoEsMuyAmplio_EntoncesValoresDentroDeLimites() {
    double min = 0.0;
    double max = 1000000.0;
    EstrategiaAleatoria estrategia = new EstrategiaAleatoria(0.0);
    SensorStub sensor = new SensorStub(min, max);

    for (int i = 0; i < 100; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("Debe funcionar con rangos grandes: " + valor, valor >= min && valor <= max);
    }
  }

  // ==========================================
  // TESTS DE COMPORTAMIENTO PROBABILÍSTICO
  // ==========================================

  @Test
  public void testGenerarValor_CuandoProbabilidadIntermedia_EntoncesProduceAmbosComportamientos() {
    double min = 0.0;
    double max = 10.0;
    // Probabilidad del 50%
    EstrategiaAleatoria estrategia = new EstrategiaAleatoria(0.5);
    SensorStub sensor = new SensorStub(min, max);

    boolean encontroDentro = false;
    boolean encontroFuera = false;

    // Realizamos suficientes intentos para que la probabilidad de no ver ambos sea
    // despreciable
    for (int i = 0; i < 100; i++) {
      double valor = estrategia.generarValor(sensor);
      if (valor >= min && valor <= max) {
        encontroDentro = true;
      } else if (valor > max) {
        encontroFuera = true;
      }

      if (encontroDentro && encontroFuera)
        break;
    }

    assertTrue("Debe haber generado al menos un valor dentro del rango", encontroDentro);
    assertTrue("Debe haber generado al menos un valor fuera del rango", encontroFuera);
  }
}