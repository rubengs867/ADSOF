package sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import estrategia.EstrategiaAleatoria;
import estrategia.EstrategiaLectura;
import unidad.Unidad;
import unidad.UnidadHumedad;
import unidad.UnidadTemperatura;

/**
 * Banco de pruebas completo para la clase SensorHumedad.
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class SensorHumedadTest {

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

  private EstrategiaFija estrategiaFija;
  private SensorHumedad sensor;

  @Before
  public void setUp() {
    estrategiaFija = new EstrategiaFija(50.0);
    sensor = new SensorHumedad(0.0, UnidadHumedad.PORCENTAJE, estrategiaFija);
    // Aseguramos estado calibrado para tests de medición
    sensor.calibrar(0.0);
  }

  // ==========================================
  // TESTS DE CONSTRUCTORES Y ESTADO INICIAL
  // ==========================================

  @Test
  public void testConstructorPorDefecto_CuandoSeInstancia_EntoncesValoresBaseCorrectos() throws Exception {
    SensorHumedad sDefecto = new SensorHumedad(2.0);

    assertEquals("El tipo debe ser HUM", "HUM", sDefecto.getTipo());
    assertEquals("El rango mínimo debe ser 0", 0.0, sDefecto.getMinRango(), 0.001);
    assertEquals("El rango máximo debe ser 100", 100.0, sDefecto.getMaxRango(), 0.001);
    assertEquals("La unidad debe ser PORCENTAJE", UnidadHumedad.PORCENTAJE, sDefecto.getUnidad());

    // Verificación de estrategia por defecto
    Field strategyField = Sensor.class.getDeclaredField("estrategia");
    strategyField.setAccessible(true);
    Object estrategia = strategyField.get(sDefecto);
    assertTrue("Debe usar EstrategiaAleatoria por defecto", estrategia instanceof EstrategiaAleatoria);
  }

  @Test
  public void testConstructorConEstrategia_CuandoSeInyecta_EntoncesSeAsignaCorrectamente() {
    assertEquals("Debe usar la unidad de humedad", UnidadHumedad.PORCENTAJE, sensor.getUnidad());
    assertTrue("El ID debe empezar por HUM-", sensor.getId().startsWith("HUM-"));
  }

  // ===================
  // TESTS DE setUnidad
  // ===================

  @Test
  public void testSetUnidad_CuandoUnidadEsDeHumedad_EntoncesNoLanzaExcepcion() {
    // UnidadHumedad.PORCENTAJE es válida
    sensor.setUnidad(UnidadHumedad.PORCENTAJE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetUnidad_CuandoUnidadNoEsDeHumedad_EntoncesLanzaExcepcion() {
    // Intentar asignar Celsius a un sensor de humedad
    Unidad unidadInvalida = UnidadTemperatura.CELSIUS;
    sensor.setUnidad(unidadInvalida);
  }

  // ==========================================
  // ==========================================

  @Test
  public void testRealizarMedicion_CuandoEstrategiaFija_EntoncesAplicaOffsetCorrectamente() throws Exception {
    // Estrategia da 50.0, Offset 5.0 -> Resultado 45.0
    sensor.calibrar(5.0);
    sensor.realizarMedicion();

    assertEquals(45.0, sensor.getValorUltimaLectura(), 0.001);
    assertEquals(1, sensor.getHistoricoLecturas().size());
  }

  // ==========================================
  // ==========================================

  @Test
  public void testToString_CuandoExistenLecturas_EntoncesFormatoCorrecto() throws Exception {
    sensor.calibrar(0.0);
    sensor.realizarMedicion(); // Valor 50.0

    String resultado = sensor.toString();
    String fechaEsperada = LocalDate.now().toString();

    // Formato esperado: "Sensor Humedad (50,00%) última lectura: 2026-04-12"
    assertTrue(resultado.contains("Sensor Humedad"));
    assertTrue(resultado.contains("50"));
    assertTrue(resultado.contains("%"));
    assertTrue(resultado.contains(fechaEsperada));
  }
}