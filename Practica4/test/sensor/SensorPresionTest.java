package sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import estrategia.EstrategiaAleatoria;
import estrategia.EstrategiaLectura;
import sensor.Sensor;
import sensor.SensorPresion;
import unidad.Unidad;
import unidad.UnidadHumedad;
import unidad.UnidadPresion;

public class SensorPresionTest {

  /**
   * Stub de EstrategiaLectura para garantizar valores deterministas.
   */
  private static class EstrategiaFija implements EstrategiaLectura {
    private double valorFijo;

    public EstrategiaFija(double valorFijo) {
      this.valorFijo = valorFijo;
    }

    @Override
    public double generarValor(Sensor s) {
      return valorFijo;
    }
  }

  private SensorPresion sensor;
  private EstrategiaFija estrategiaFija;

  @Before
  public void setUp() {
    estrategiaFija = new EstrategiaFija(1013.25);
    sensor = new SensorPresion(0.0, estrategiaFija);
    // Aseguramos estado calibrado para tests de medición
    sensor.calibrar(0.0);
  }

  // ==========================================
  // TESTS DE CONSTRUCTORES Y ESTADO INICIAL
  // ==========================================

  @Test
  public void testConstructorPorDefecto_CuandoSeInstancia_EntoncesValoresBaseCorrectos() throws Exception {
    SensorPresion sDefecto = new SensorPresion(5.0);

    assertEquals("El tipo debe ser PRES", "PRES", sDefecto.getTipo());
    assertEquals("El rango mínimo debe ser 300.0", 300.0, sDefecto.getMinRango(), 0.001);
    assertEquals("El rango máximo debe ser 1100.0", 1100.0, sDefecto.getMaxRango(), 0.001);
    assertEquals("La unidad debe ser HPA", UnidadPresion.HPA, sDefecto.getUnidad());
    assertTrue("El ID debe empezar por PRES-", sDefecto.getId().startsWith("PRES-"));

    // Verificación de la estrategia por defecto
    Field strategyField = Sensor.class.getDeclaredField("estrategia");
    strategyField.setAccessible(true);
    Object estrategiaObj = strategyField.get(sDefecto);
    assertTrue("Debe usar EstrategiaAleatoria por defecto", estrategiaObj instanceof EstrategiaAleatoria);
  }

  @Test
  public void testConstructorConEstrategia_CuandoSeInyecta_EntoncesSeUsaCorrectamente() throws Exception {
    // La estrategia fija devuelve 1013.25, offset 0 -> resultado 1013.25
    sensor.realizarMedicion();
    assertEquals(1013.25, sensor.getValorUltimaLectura(), 0.001);
  }

  // ======================
  // TESTS DE setUnidad
  // ======================

  @Test
  public void testSetUnidad_CuandoEsPresion_EntoncesNoLanzaExcepcion() {
    // UnidadPresion.HPA es válida (cumple isPresion())
    sensor.setUnidad(UnidadPresion.HPA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetUnidad_CuandoNoEsPresion_EntoncesLanzaExcepcion() {
    // Intentar asignar porcentaje (humedad) a un sensor de presión
    Unidad unidadInvalida = UnidadHumedad.PORCENTAJE;
    sensor.setUnidad(unidadInvalida);
  }

  // ==========================================
  // ==========================================

  @Test
  public void testRealizarMedicion_CuandoFlujoCorrecto_EntoncesAplicaOffsetYGestionaEstado() throws Exception {
    // Estrategia da 1013.25, Offset 13.25 -> Valor esperado = 1000.0
    sensor.calibrar(13.25);

    sensor.realizarMedicion();

    assertEquals(1000.0, sensor.getValorUltimaLectura(), 0.001);
    assertNotNull("La fecha de lectura debe actualizarse", sensor.getFechaUltimaLectura());
    assertEquals("Debe registrarse en el histórico", 1, sensor.getHistoricoLecturas().size());
    assertEquals(1000.0, sensor.getHistoricoLecturas().get(0), 0.001);
  }

  // ==========================================
  // ==========================================

  @Test
  public void testToString_CuandoSeHaMedido_EntoncesFormatoCorrecto() throws Exception {
    sensor.realizarMedicion(); // Valor 1013.25

    String resultado = sensor.toString();
    String fechaHoy = LocalDate.now().toString();

    // Formato esperado: "Sensor Presión (1013,25 hPa) última lectura: 2026-04-12"
    assertTrue(resultado.contains("Sensor Presión"));
    assertTrue(resultado.contains("1013"));
    assertTrue(resultado.contains("hPa"));
    assertTrue(resultado.contains(fechaHoy));
  }
}