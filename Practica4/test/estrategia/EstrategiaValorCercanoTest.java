package estrategia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sensor.Sensor;
import unidad.Unidad;

public class EstrategiaValorCercanoTest {

  /**
   * Stub de Sensor para controlar el estado de la última lectura y los rangos.
   */
  private static class SensorStub extends Sensor {
    private double valorUltimaLecturaSimulado;
    private boolean esPrimeraLectura = false;

    public SensorStub(double min, double max) {
      super("STUB", 0.0, null, min, max, null);
    }

    public void setValorUltimaLectura(double valor) {
      this.valorUltimaLecturaSimulado = valor;
    }

    public void setEsPrimeraLectura(boolean b) {
      this.esPrimeraLectura = b;
    }

    @Override
    public double getValorUltimaLectura() {
      return valorUltimaLecturaSimulado;
    }

    @Override
    public boolean primeraLectura() {
      return esPrimeraLectura;
    }

    @Override
    public void setUnidad(Unidad u) {
    }

    @Override
    public String toString() {
      return "SensorStub";
    }
  }

  private SensorStub sensor;
  private EstrategiaValorCercano estrategia;
  private final double VARIACION = 0.1; // 10%

  @Before
  public void setUp() {
    sensor = new SensorStub(10.0, 50.0);
    estrategia = new EstrategiaValorCercano(VARIACION);
  }

  // =====================
  // TEST PRIMERA LECTURA
  // =====================

  @Test
  public void testGenerarValor_CuandoEsPrimeraLectura_EntoncesDevuelveMediaDelRango() {
    sensor.setEsPrimeraLectura(true);

    // (10 + 50) / 2 = 30.0
    double esperado = 30.0;
    double resultado = estrategia.generarValor(sensor);

    assertEquals("En la primera lectura debe devolver el punto medio del rango", esperado, resultado, 0.001);
  }

  // =========================
  // TESTS CON LECTURA PREVIA
  // =========================

  @Test
  public void testGenerarValor_CuandoExisteLecturaPrevia_EntoncesValorDentroDeRangoVariacion() {
    double valorPrevio = 100.0;
    sensor.setEsPrimeraLectura(false);
    sensor.setValorUltimaLectura(valorPrevio);

    /*
     * Delta = |100 * 0.1| = 10.0
     * Rango esperado: [90.0, 110.0]
     */
    double limInf = 90.0;
    double limSup = 110.0;

    // Ejecutamos múltiples veces para validar el componente aleatorio
    for (int i = 0; i < 100; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("El valor " + valor + " debe ser >= " + limInf, valor >= limInf);
      assertTrue("El valor " + valor + " debe ser <= " + limSup, valor <= limSup);
    }
  }

  @Test
  public void testGenerarValor_CuandoValorAnteriorEsNegativo_EntoncesCalculaLimitesCorrectamente() {
    double valorPrevio = -50.0;
    sensor.setEsPrimeraLectura(false);
    sensor.setValorUltimaLectura(valorPrevio);

    /*
     * Delta = |-50 * 0.1| = 5.0
     * Rango esperado: [-55.0, -45.0]
     */
    double limInf = -55.0;
    double limSup = -45.0;

    for (int i = 0; i < 100; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("Debe manejar valores previos negativos. Valor: " + valor, valor >= limInf && valor <= limSup);
    }
  }

  @Test
  public void testGenerarValor_CuandoVariacionEsCero_EntoncesSiempreDevuelveValorAnterior() {
    estrategia = new EstrategiaValorCercano(0.0);
    double valorPrevio = 75.5;
    sensor.setEsPrimeraLectura(false);
    sensor.setValorUltimaLectura(valorPrevio);

    for (int i = 0; i < 50; i++) {
      double valor = estrategia.generarValor(sensor);
      assertEquals("Si la variación es 0, debe devolver exactamente el valor anterior", valorPrevio, valor, 0.001);
    }
  }

  // ==========================================
  // ==========================================

  @Test
  public void testGenerarValor_CuandoNoEsPrimeraLectura_EntoncesIgnoraRangoDelSensor() {
    /*
     * Sensor con rango [0, 10], pero última lectura 500.
     * El valor generado debe orbitar sobre 500, no sobre el rango del sensor.
     */
    sensor = new SensorStub(0.0, 10.0);
    sensor.setEsPrimeraLectura(false);
    sensor.setValorUltimaLectura(500.0);

    double valor = estrategia.generarValor(sensor);

    // Delta = 50. Rango [450, 550]
    assertTrue("El valor debe basarse en la última lectura, ignorando el rango min/max",
        valor >= 450.0 && valor <= 550.0);
  }
}