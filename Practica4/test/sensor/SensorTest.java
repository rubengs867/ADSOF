package test.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import src.estrategia.EstrategiaLectura;
import src.excepcion.CambioBruscoException;
import src.excepcion.SensorDescalibradoException;
import src.excepcion.SensorDescalibradoPorCaducidadException;
import src.excepcion.SensorDescalibradoPorRangoException;
import src.sensor.Sensor;
import src.sensor.SensorHumedad;
import src.sensor.SensorTemperatura;
import src.unidad.Unidad;
import src.unidad.UnidadTemperatura;

public class SensorTest {

  /**
   * Stub de EstrategiaLectura para garantizar valores deterministas
   * en los tests y no depender de la aleatoriedad.
   */
  private static class EstrategiaFija implements EstrategiaLectura {
    private double valorFijo;

    public EstrategiaFija(double valorFijo) {
      this.valorFijo = valorFijo;
    }

    public void setValorFijo(double valorFijo) {
      this.valorFijo = valorFijo;
    }

    @Override
    public double generarValor(Sensor s) {
      return valorFijo;
    }
  }

  /**
   * Stub de la clase abstracta Sensor para poder instanciarla y probar
   * su lógica interna de forma aislada.
   */
  private static class SensorStub extends Sensor {
    public SensorStub(String tipo, double offset, double minRango, double maxRango, EstrategiaLectura estrategia) {
      super(tipo, offset, null, minRango, maxRango, estrategia);
    }

    @Override
    public void setUnidad(Unidad u) {
      // Implementación vacía para el stub
    }

    @Override
    public String toString() {
      return "SensorStub[" + getId() + "]";
    }
  }

  private SensorStub sensor;
  private EstrategiaFija estrategiaFija;

  @Before
  public void setUp() {
    estrategiaFija = new EstrategiaFija(10.0);
    // Inicializa un sensor genérico de rango 0.0 a 100.0, con offset inicial de 2.0
    sensor = new SensorStub("TEST", 2.0, 0.0, 100.0, estrategiaFija);
    sensor.setUmbralCambio(0.5);
  }

  // =======================
  // TESTS DEL CONSTRUCTOR
  // =======================

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_CuandoMinRangoMayorMaxRango_EntoncesLanzaExcepcion() {
    new SensorStub("ERR", 0.0, 50.0, 10.0, estrategiaFija);
  }

  // ============================
  // TESTS DE GENERACIÓN DE ID
  // ============================

  @Test
  public void testGeneracionId_CuandoMismoTipo_EntoncesIncrementaSecuencialmente() {
    SensorStub s1 = new SensorStub("INC", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub s2 = new SensorStub("INC", 0.0, 0.0, 100.0, estrategiaFija);

    int num1 = Integer.parseInt(s1.getId().split("-")[1]);
    int num2 = Integer.parseInt(s2.getId().split("-")[1]);

    assertEquals(num1 + 1, num2);
  }

  @Test
  public void testGeneracionId_CuandoDistintosTipos_EntoncesContadoresIndependientes() {
    Sensor sTemp = new SensorTemperatura(0.0, UnidadTemperatura.CELSIUS, estrategiaFija);
    Sensor sHum = new SensorHumedad(0.0, estrategiaFija);

    assertTrue("El ID de Temperatura debe usar su prefijo", sTemp.getId().startsWith("TEMP-"));
    assertTrue("El ID de Humedad debe usar su prefijo", sHum.getId().startsWith("HUM-"));

    assertTrue("El primer ID de Temperatura debe acabar en 0001", sTemp.getId().endsWith("0001"));
    assertTrue("El primer ID de Humedad debe acabar en 0001", sHum.getId().endsWith("0001"));
  }

  // ==========================================
  // TESTS DE REALIZAR MEDICIÓN Y EXCEPCIONES
  // ==========================================

  @Test
  public void testRealizarMedicion_AplicaOffsetYGestionaEstado() throws Exception {
    sensor.calibrar(2.0);

    // Estrategia da 10.0 -> Offset es 2.0 -> Valor final debe ser 8.0
    sensor.realizarMedicion();

    assertEquals(8.0, sensor.getValorUltimaLectura(), 0.001);
    assertNotNull(sensor.getFechaUltimaLectura());
    assertEquals(LocalDate.now(), sensor.getFechaUltimaLectura());

    List<Double> historico = sensor.getHistoricoLecturas();
    assertFalse(historico.isEmpty());
    assertEquals(8.0, historico.get(0), 0.001);
  }

  @Test(expected = SensorDescalibradoException.class)
  public void testRealizarMedicion_CuandoNoCalibrado_EntoncesLanzaExcepcion() throws Exception {
    sensor.setCalibrado(false);

    sensor.realizarMedicion();
  }

  @Test(expected = SensorDescalibradoPorCaducidadException.class)
  public void testRealizarMedicion_CuandoCalibracionCaducada_EntoncesLanzaExcepcion() throws Exception {
    sensor.calibrar(2.0);
    // Devuelve true (provocando la excepción) cuando los días son negativos.
    sensor.setCaducacionCalibracion(-1);
    sensor.realizarMedicion();
  }

  @Test(expected = SensorDescalibradoPorRangoException.class)
  public void testRealizarMedicion_CuandoFueraDeRango_EntoncesLanzaExcepcion() throws Exception {
    sensor.calibrar(2.0);

    // Max rango es 100.0. Forzamos 150.0 - 2.0(offset) = 148.0
    estrategiaFija.setValorFijo(150.0);
    sensor.realizarMedicion();
  }

  @Test(expected = CambioBruscoException.class)
  public void testRealizarMedicion_CuandoCambioBrusco_EntoncesLanzaExcepcion() throws Exception {
    sensor.calibrar(2.0);
    sensor.setUmbralCambio(0.5); // 50% de umbral

    // 1ª Lectura: 10.0 - 2.0 = 8.0
    estrategiaFija.setValorFijo(10.0);
    sensor.realizarMedicion();

    // 2ª Lectura: 25.0 - 2.0 = 23.0
    // Diferencia: 15.0 -> % Cambio: 15.0 / 8.0 = 1.875 (187.5% > 50%)
    estrategiaFija.setValorFijo(25.0);
    sensor.realizarMedicion();
  }

  // ==========================================
  // TESTS DE PRIMERA LECTURA E HISTÓRICO
  // ==========================================

  @Test
  public void testPrimeraLectura_CuandoSinMedicionesYConMediciones_EntoncesActualizaEstado() throws Exception {
    assertTrue("Debe ser true si no se han hecho lecturas", sensor.primeraLectura());

    sensor.calibrar(2.0);
    sensor.realizarMedicion();

    assertFalse("Debe ser false tras realizar al menos una medición", sensor.primeraLectura());
  }

  @Test
  public void testHistoricoLecturas_CuandoNuevasMediciones_EntoncesCreceAcumulativo() throws Exception {
    sensor.calibrar(2.0);

    // Primera lectura
    estrategiaFija.setValorFijo(12.0); // Final: 10.0
    sensor.realizarMedicion();

    // Segunda lectura (evitamos cambio brusco, < 50% de 10 es 5)
    estrategiaFija.setValorFijo(16.0); // Final: 14.0si está calibrado;
    sensor.realizarMedicion();

    List<Double> historico = sensor.getHistoricoLecturas();

    assertEquals("El histórico debe tener 2 elementos", 2, historico.size());
    assertEquals(10.0, historico.get(0), 0.001);
    assertEquals(14.0, historico.get(1), 0.001);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testHistoricoLecturas_CuandoIntentaModificar_EntoncesLanzaUnsupportedOperationException()
      throws Exception {
    sensor.calibrar(2.0);
    sensor.realizarMedicion();

    List<Double> historico = sensor.getHistoricoLecturas();
    historico.add(99.9);
  }

  // =======================
  // TESTS DE CALIBRACIÓN
  // ======================

  @Test
  public void testCalibrar_CuandoSeInvoca_EntoncesActualizaEstadoYOffset() {
    // Estado inicial de un sensor está descalibrado
    sensor.setCalibrado(false);

    // Calibramos con offset 5.5 y caducidad 30 días
    sensor.calibrar(5.5, 30);

    assertTrue("El sensor debe quedar marcado como calibrado", sensor.getCalibrado());
    assertEquals(5.5, sensor.getOffset(), 0.001);

    // Verificamos que la fecha se actualizó mediante comprobación de caducidad
    assertFalse(sensor.estaCalibracionCaducada());
  }

  @Test
  public void testEstaCalibrado_CuandoEstaCalibradoYNoHaCaducado_EntoncesDevuelveTrue() {
    // Se calibra con 365 días de validez
    sensor.calibrar(2.0, 365);

    assertTrue("Debe devolver true si se ha calibrado y la fecha aún es válida", sensor.estaCalibrado());
  }

  @Test
  public void testEstaCalibrado_CuandoNoEstaCalibrado_EntoncesDevuelveFalse() {
    // Marcamos explícitamente como no calibrado
    sensor.setCalibrado(false);
    // La fecha de caducidad no se ha cumplido
    sensor.setCaducacionCalibracion(365);

    assertFalse("Debe devolver false porque la bandera de calibración es false", sensor.estaCalibrado());
  }

  @Test
  public void testEstaCalibrado_CuandoEstaCalibradoPeroHaCaducado_EntoncesDevuelveFalse() {
    // Lo calibramos para que la bandera 'calibrado' sea true
    sensor.calibrar(2.0);

    // Forzamos que la calibración esté caducada
    sensor.setCaducacionCalibracion(-10);

    assertFalse("Debe devolver false porque, aunque se calibró, la fecha ha caducado", sensor.estaCalibrado());
  }

  // ================================
  // TESTS DE IGUALDAD Y HASHCODE
  // ================================

  @Test
  public void testEqualsYHashCode_CuandoIdsCoinciden_EntoncesSonIguales() throws Exception {
    SensorStub s1 = new SensorStub("EQ", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub s2 = new SensorStub("EQ", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub s3 = new SensorStub("OTRO", 0.0, 0.0, 100.0, estrategiaFija);

    // Distintos sensores generados naturalmente tienen distinto ID
    assertFalse(s1.equals(s2));
    assertFalse(s1.equals(s3));
    assertFalse(s1.equals(null));
    assertFalse(s1.equals(new Object()));

    // Reflexividad
    assertTrue(s1.equals(s1));

    // Forzamos el mismo ID usando Reflection para aislar y probar la lógica pura de
    // equals()
    Field idField = Sensor.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(s2, s1.getId());

    assertTrue("equals debe ser true si los IDs coinciden", s1.equals(s2));
    assertEquals("hashCode debe ser igual si los IDs coinciden", s1.hashCode(), s2.hashCode());
  }
}