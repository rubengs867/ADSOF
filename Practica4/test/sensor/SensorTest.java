package Practica4.test.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Practica4.src.estrategia.EstrategiaLectura;
import Practica4.src.sensor.Sensor;
import Practica4.src.sensor.Unidad;

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
      // Se inyecta null en Unidad ya que la lógica probada no depende de ella
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
    sensor = new SensorStub("TEST", 2.0, 0.0, 100.0, estrategiaFija);
  }

  // F) Constructor: Rango inválido lanza excepción
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_MinRangoMayorQueMaxRango_LanzaExcepcion() {
    new SensorStub("ERR", 0.0, 50.0, 10.0, estrategiaFija);
  }

  // A) Generación de ID: Mismos tipos incrementan
  @Test
  public void testGeneracionId_MismoTipo_IncrementaSecuencialmente() {
    // Se usa un prefijo único para evitar interferencias del estado estático entre
    // tests
    SensorStub s1 = new SensorStub("INC", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub s2 = new SensorStub("INC", 0.0, 0.0, 100.0, estrategiaFija);

    String id1 = s1.getId();
    String id2 = s2.getId();

    int num1 = Integer.parseInt(id1.split("-")[1]);
    int num2 = Integer.parseInt(id2.split("-")[1]);

    assertEquals("Los IDs del mismo tipo deben ser consecutivos", num1 + 1, num2);
  }

  // A) Generación de ID: Distintos tipos contadores independientes
  @Test
  public void testGeneracionId_DistintosTipos_ContadoresIndependientes() {
    SensorStub sA = new SensorStub("TIPA", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub sB = new SensorStub("TIPB", 0.0, 0.0, 100.0, estrategiaFija);

    assertTrue(sA.getId().startsWith("TIPA-"));
    assertTrue(sB.getId().startsWith("TIPB-"));

    // Al ser tipos nuevos en este test, el contador de ambos debe empezar en 1
    // (0001)
    assertTrue("El primer ID del tipo A debe acabar en 0001", sA.getId().endsWith("0001"));
    assertTrue("El primer ID del tipo B debe acabar en 0001", sB.getId().endsWith("0001"));
  }

  // B) realizarMedicion(): Se invoca estrategia, aplica offset y actualiza datos
  @Test
  public void testRealizarMedicion_AplicaOffsetYActualizaEstado() throws Exception {
    // La estrategia genera 10.0, el offset del sensor en @Before es 2.0
    // valorFinal esperado = 10.0 - 2.0 = 8.0
    sensor.realizarMedicion();

    assertEquals(8.0, sensor.getValorUltimaLectura(), 0.001);
    assertNotNull(sensor.getFechaUltimaLectura());
    assertEquals(LocalDate.now(), sensor.getFechaUltimaLectura());

    List<Double> historico = sensor.getHistoricoLecturas();
    assertFalse(historico.isEmpty());
    assertEquals(8.0, historico.get(0), 0.001);
  }

  // C) primeraLectura(): Comportamiento antes y después de medir
  @Test
  public void testPrimeraLectura_CambiaEstadoCorrectamente() throws Exception {
    assertTrue("Debe ser true si no se han hecho lecturas", sensor.primeraLectura());

    sensor.realizarMedicion();

    assertFalse("Debe ser false tras realizar al menos una medición", sensor.primeraLectura());
  }

  // D) getHistoricoLecturas(): Es inmutable
  @Test(expected = UnsupportedOperationException.class)
  public void testHistoricoLecturas_EsInmutable() throws Exception {
    sensor.realizarMedicion();
    List<Double> historico = sensor.getHistoricoLecturas();

    // Intentar modificar la lista devuelta debe lanzar
    // UnsupportedOperationException (List.copyOf)
    historico.add(99.9);
  }

  // E) equals() y hashCode()
  @Test
  public void testEqualsYHashCode_MismoIdEsTrue() throws Exception {
    SensorStub s1 = new SensorStub("EQ", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub s2 = new SensorStub("EQ", 0.0, 0.0, 100.0, estrategiaFija);
    SensorStub s3 = new SensorStub("OTRO", 0.0, 0.0, 100.0, estrategiaFija);

    // 1. Distintos sensores generados naturalmente tienen distinto ID -> false
    assertFalse(s1.equals(s2));
    assertFalse(s1.equals(s3));
    assertFalse(s1.equals(null));
    assertFalse(s1.equals(new Object()));

    // 2. Reflexividad
    assertTrue(s1.equals(s1));

    // 3. Forzamos el mismo ID usando Reflection para probar la lógica base del
    // equals
    Field idField = Sensor.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(s2, s1.getId());

    // Ahora s1 y s2 tienen exactamente el mismo ID
    assertTrue("equals debe ser true si los IDs coinciden", s1.equals(s2));
    assertEquals("hashCode debe ser igual si los IDs coinciden", s1.hashCode(), s2.hashCode());
  }

  // G) Comportamiento acumulativo: Histórico crece
  @Test
  public void testRealizarMedicion_ComportamientoAcumulativo() throws Exception {
    // Primera lectura: Genera 15.0 - Offset 2.0 = 13.0
    estrategiaFija.setValorFijo(15.0);
    sensor.realizarMedicion();

    // Segunda lectura: Genera 25.0 - Offset 2.0 = 23.0
    estrategiaFija.setValorFijo(25.0);
    sensor.realizarMedicion();

    List<Double> historico = sensor.getHistoricoLecturas();

    assertEquals("El histórico debe tener 2 elementos", 2, historico.size());
    assertEquals(13.0, historico.get(0), 0.001);
    assertEquals(23.0, historico.get(1), 0.001);
  }
}