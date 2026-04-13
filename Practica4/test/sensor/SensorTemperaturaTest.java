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
import unidad.Unidad;
import unidad.UnidadHumedad;
import unidad.UnidadTemperatura;

/**
 * Banco de pruebas completo para la clase SensorTemperatura.
 * @author Alejandro Seguido
 * @author Rubén García
 */
public class SensorTemperaturaTest {

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

  private SensorTemperatura sensor;
  private EstrategiaFija estrategiaFija;

  @Before
  public void setUp() {
    estrategiaFija = new EstrategiaFija(25.0);
    sensor = new SensorTemperatura(0.0, UnidadTemperatura.CELSIUS, estrategiaFija);
    // Aseguramos estado calibrado para tests de medición
    sensor.calibrar(0.0);
  }

  // ==========================================
  // TESTS DE CONSTRUCTORES Y ESTADO INICIAL
  // ==========================================

  @Test
  public void testConstructorPorDefecto_CuandoSeInstancia_EntoncesValoresBaseCorrectos() throws Exception {
    // Probamos el constructor que solo recibe offset
    SensorTemperatura sDefecto = new SensorTemperatura(2.0);

    assertEquals("El tipo debe ser TEMP", "TEMP", sDefecto.getTipo());
    assertEquals("El rango mínimo debe ser -273.15", -273.15, sDefecto.getMinRango(), 0.001);
    assertEquals("El rango máximo debe ser 1000.0", 1000.0, sDefecto.getMaxRango(), 0.001);
    assertEquals("La unidad por defecto debe ser CELSIUS", UnidadTemperatura.CELSIUS, sDefecto.getUnidad());
    assertTrue("El ID debe empezar por TEMP-", sDefecto.getId().startsWith("TEMP-"));

    // Verificación de la estrategia por defecto
    Field strategyField = Sensor.class.getDeclaredField("estrategia");
    strategyField.setAccessible(true);
    Object estrategiaObj = strategyField.get(sDefecto);
    assertTrue("Debe usar EstrategiaAleatoria por defecto", estrategiaObj instanceof EstrategiaAleatoria);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorCompleto_CuandoUnidadInvalida_EntoncesLanzaExcepcion() {
    // Intentar crear sensor de temperatura con unidad de humedad
    new SensorTemperatura(0.0, UnidadHumedad.PORCENTAJE, estrategiaFija);
  }

  @Test
  public void testConstructorConEstrategia_CuandoSeInyecta_EntoncesSeUsaCorrectamente() throws Exception {
    // La estrategia fija devuelve 25.0, offset 0 -> resultado 25.0
    sensor.realizarMedicion();
    assertEquals(25.0, sensor.getValorUltimaLectura(), 0.001);
  }

  // ======================
  // TESTS DE setUnidad
  // ======================

  @Test
  public void testSetUnidad_CuandoEsTemperatura_EntoncesNoLanzaExcepcion() {
    // Kelvin es una unidad de temperatura válida
    sensor.setUnidad(UnidadTemperatura.KELVIN);
    assertEquals(UnidadTemperatura.KELVIN, sensor.getUnidad());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetUnidad_CuandoNoEsTemperatura_EntoncesLanzaExcepcion() {
    // Intentar asignar porcentaje a un sensor de temperatura
    Unidad unidadInvalida = UnidadHumedad.PORCENTAJE;
    sensor.setUnidad(unidadInvalida);
  }

  // ==========================================
  // ==========================================

  @Test
  public void testRealizarMedicion_CuandoFlujoCorrecto_EntoncesAplicaOffsetYGestionaEstado() throws Exception {
    // Estrategia da 25.0, Offset 5.0 -> Valor esperado = 20.0
    sensor.calibrar(5.0);

    sensor.realizarMedicion();

    assertEquals(20.0, sensor.getValorUltimaLectura(), 0.001);
    assertNotNull("La fecha de lectura debe actualizarse", sensor.getFechaUltimaLectura());
    assertEquals("Debe registrarse en el histórico", 1, sensor.getHistoricoLecturas().size());
    assertEquals(20.0, sensor.getHistoricoLecturas().get(0), 0.001);
  }

  // ==========================================
  // ==========================================

  @Test
  public void testToString_CuandoSeHaMedido_EntoncesFormatoCorrecto() throws Exception {
    sensor.realizarMedicion(); // Valor 25.0

    String resultado = sensor.toString();
    String fechaHoy = LocalDate.now().toString();

    // Formato esperado: "Sensor Temperatura (25,00°C) última lectura: 2026-04-12"
    assertTrue(resultado.contains("Sensor Temperatura"));
    assertTrue(resultado.contains("25"));
    assertTrue(resultado.contains("ºC"));
    assertTrue(resultado.contains(fechaHoy));
  }
}