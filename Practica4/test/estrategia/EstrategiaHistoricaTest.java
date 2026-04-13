package estrategia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sensor.Sensor;
import unidad.Unidad;

/**
 * Banco de pruebas completo para la clase EstrategiaHistorica.
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class EstrategiaHistoricaTest {

  /**
   * Stub de Sensor para controlar el histórico y los rangos.
   */
  private static class SensorStub extends Sensor {
    private List<Double> historicoSimulado = new ArrayList<>();
    private boolean esPrimeraLectura = false;

    public SensorStub(double min, double max) {
      super("STUB", 0.0, null, min, max, null);
    }

    public void setHistorico(List<Double> lecturas) {
      this.historicoSimulado = lecturas;
    }

    public void setEsPrimeraLectura(boolean b) {
      this.esPrimeraLectura = b;
    }

    @Override
    public List<Double> getHistoricoLecturas() {
      return historicoSimulado;
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
  private EstrategiaHistorica estrategia;
  private final double VARIACION = 0.1; // 10%

  @Before
  public void setUp() {
    sensor = new SensorStub(0.0, 100.0);
    estrategia = new EstrategiaHistorica(VARIACION);
  }

  // =====================
  // TESTS SIN HISTÓRICO
  // =====================

  @Test
  public void testGenerarValor_CuandoEsPrimeraLectura_EntoncesDevuelveMediaDelRango() {
    sensor.setEsPrimeraLectura(true);
    sensor.setHistorico(new ArrayList<>());

    double esperado = (sensor.getMinRango() + sensor.getMaxRango()) / 2.0;
    double resultado = estrategia.generarValor(sensor);

    assertEquals("Debe devolver el punto medio del rango del sensor", esperado, resultado, 0.001);
  }

  @Test
  public void testGenerarValor_CuandoHistoricoEstaVacio_EntoncesDevuelveMediaDelRango() {
    sensor.setEsPrimeraLectura(false);
    sensor.setHistorico(new ArrayList<>());

    double esperado = (sensor.getMinRango() + sensor.getMaxRango()) / 2.0;
    double resultado = estrategia.generarValor(sensor);

    assertEquals("Debe devolver el punto medio si el histórico está vacío aunque no sea la primera lectura",
        esperado, resultado, 0.001);
  }

  // =====================
  // TESTS CON HISTÓRICO
  // =====================

  @Test
  public void testGenerarValor_CuandoExisteHistorico_EntoncesValorDentroDeRangoVariacion() {
    List<Double> lecturas = List.of(10.0, 20.0, 30.0);
    sensor.setHistorico(lecturas);
    sensor.setEsPrimeraLectura(false);

    double mediaEsperada = 20.0;
    double deltaEsperado = mediaEsperada * VARIACION;
    double limInf = mediaEsperada - deltaEsperado;
    double limSup = mediaEsperada + deltaEsperado;

    // Ejecutamos múltiples veces para validar el componente aleatorio
    for (int i = 0; i < 100; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("El valor " + valor + " debe ser >= " + limInf, valor >= limInf);
      assertTrue("El valor " + valor + " debe ser <= " + limSup, valor <= limSup);
    }
  }

  @Test
  public void testGenerarValor_CuandoMediaEsNegativa_EntoncesCalculaDeltaCorrectamente() {
    // Media: (-10 - 20) / 2 = -15.0
    sensor.setHistorico(List.of(-10.0, -20.0));

    /*
     * Delta: abs(-15 * 0.1) = 1.5
     * Rango: [-15 - 1.5, -15 + 1.5] -> [-16.5, -13.5]
     */
    double limInf = -16.5;
    double limSup = -13.5;

    for (int i = 0; i < 50; i++) {
      double valor = estrategia.generarValor(sensor);
      assertTrue("Debe manejar medias negativas. Valor: " + valor, valor >= limInf && valor <= limSup);
    }
  }

  @Test
  public void testGenerarValor_CuandoVariacionEsCero_EntoncesSiempreDevuelveLaMedia() {
    estrategia = new EstrategiaHistorica(0.0);
    sensor.setHistorico(List.of(50.0, 100.0)); // Media 75.0

    for (int i = 0; i < 50; i++) {
      double valor = estrategia.generarValor(sensor);
      assertEquals("Si la variación es 0, debe devolver exactamente la media", 75.0, valor, 0.001);
    }
  }

  @Test
  public void testGenerarValor_CuandoHistoricoCambia_EntoncesLaMedidaSeAdapta() {
    // Primer histórico: Media 10
    sensor.setHistorico(List.of(10.0, 10.0));
    double v1 = estrategia.generarValor(sensor);
    assertTrue(v1 >= 9.0 && v1 <= 11.0);

    // Segundo histórico: Media 100
    sensor.setHistorico(List.of(100.0, 100.0));
    double v2 = estrategia.generarValor(sensor);
    assertTrue(v2 >= 90.0 && v2 <= 110.0);
  }
}