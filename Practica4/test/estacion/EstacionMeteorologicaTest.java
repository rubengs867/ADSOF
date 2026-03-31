package Practica4.test.estacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Practica4.src.estacion.EstacionMeteorologica;
import Practica4.src.estacion.Ubicacion;
import Practica4.src.estrategia.EstrategiaAleatoria;
import Practica4.src.excepcion.SensorDuplicadoException;
import Practica4.src.sensor.Sensor;
import Practica4.src.sensor.SensorHumedad;
import Practica4.src.sensor.SensorPresion;
import Practica4.src.sensor.SensorTemperatura;
import Practica4.src.sensor.Unidad;

/**
 * Banco de pruebas para la clase EstacionMeteorologica.
 */
public class EstacionMeteorologicaTest {

  private EstacionMeteorologica estacion;
  private Ubicacion ubicacionTest;
  private static final long PERIODO_TEST = 100; // 100ms para pruebas rápidas
  private static final int MAX_LECTURAS_TEST = 2;

  @Before
  public void setUp() {
    ubicacionTest = new Ubicacion(40.41, -3.70);
    // Inicializamos con un periodo corto para testear la lógica temporal
    estacion = new EstacionMeteorologica("Estacion-Test", ubicacionTest, PERIODO_TEST, MAX_LECTURAS_TEST);
  }

  // PRUEBAS DE addSensor()

  @Test
  public void testAddSensor_Exito() throws SensorDuplicadoException {
    Sensor s = new SensorStub("TEMP-0001", "TEMP");
    estacion.addSensor(s);

    assertEquals(s, estacion.getSensor("TEMP-0001"));
  }

  @Test
  public void testAddSensor_VariosTipos() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("TEMP-01", "TEMP"));
    estacion.addSensor(new SensorStub("HUM-01", "HUM"));

    assertEquals(1, estacion.getSensoresPorTipo("TEMP").size());
    assertEquals(1, estacion.getSensoresPorTipo("HUM").size());
  }

  @Test(expected = SensorDuplicadoException.class)
  public void testAddSensor_Duplicado_LanzaExcepcion() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("TEMP-0001", "TEMP"));
    // Intentar añadir otro sensor (o el mismo) con el mismo ID
    estacion.addSensor(new SensorStub("TEMP-0001", "OTRO"));
  }

  @Test
  public void testAddSensor_GuardaFechaInstalacion() throws SensorDuplicadoException {
    String id = "PRES-01";
    estacion.addSensor(new SensorStub(id, "PRES"));

    assertEquals("La fecha debe ser la del día de hoy",
        LocalDate.now(), estacion.getFechaInstalacion(id));
  }

  // PRUEBAS DE getSensoresPorTipo()

  @Test
  public void testGetSensoresPorTipo_DevuelveCopia() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("TEMP-01", "TEMP"));
    List<Sensor> lista = estacion.getSensoresPorTipo("TEMP");

    // Modificamos la lista devuelta
    lista.clear();

    // La lista interna de la estación no debería haberse visto afectada
    assertFalse(estacion.getSensoresPorTipo("TEMP").isEmpty());
  }

  @Test
  public void testGetSensoresPorTipo_Inexistente_ListaVacia() {
    List<Sensor> lista = estacion.getSensoresPorTipo("TIPO_FANTASMA");
    assertNotNull(lista);
    assertTrue(lista.isEmpty());
  }

  // PRUEBAS DE realizarLecturaPuntual()

  @Test
  public void testLecturaPuntual_TodosConMenosUno() throws SensorDuplicadoException {
    SensorStub s1 = new SensorStub("T1", "T");
    SensorStub s2 = new SensorStub("T2", "T");
    estacion.addSensor(s1);
    estacion.addSensor(s2);

    estacion.realizarLecturaPuntual(-1);

    assertEquals(1, s1.contadorMediciones);
    assertEquals(1, s2.contadorMediciones);
  }

  @Test
  public void testLecturaPuntual_NumeroLimitado() throws SensorDuplicadoException {
    SensorStub s1 = new SensorStub("T1", "T");
    SensorStub s2 = new SensorStub("T2", "T");
    estacion.addSensor(s1);
    estacion.addSensor(s2);

    estacion.realizarLecturaPuntual(1);

    // Solo el primero de la lista debería haber medido
    int totalMediciones = s1.contadorMediciones + s2.contadorMediciones;
    assertEquals(1, totalMediciones);
  }

  @Test
  public void testLecturaPuntual_ExcesoSensores_MideTodos() throws SensorDuplicadoException {
    SensorStub s1 = new SensorStub("T1", "T");
    estacion.addSensor(s1);

    // Pedimos medir 100 aunque solo hay 1
    estacion.realizarLecturaPuntual(100);
    assertEquals(1, s1.contadorMediciones);
  }

  // PRUEBAS DE realizarLecturasPeriodicas()

  @Test
  public void testLecturaPeriodica_PrimeraVez_SiempreEjecuta() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("T1", "T"));
    assertTrue(estacion.realizarLecturasPeriodicas());
  }

  @Test
  public void testLecturaPeriodica_SinTiempo_NoEjecuta() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("T1", "T"));

    estacion.realizarLecturasPeriodicas(); // Primera vez
    boolean resultadoSegunda = estacion.realizarLecturasPeriodicas(); // Inmediatamente después

    assertFalse(resultadoSegunda);
  }

  @Test
  public void testLecturaPeriodica_PasadoPeriodo_Ejecuta() throws SensorDuplicadoException, InterruptedException {
    estacion.addSensor(new SensorStub("T1", "T"));

    estacion.realizarLecturasPeriodicas();

    // Esperamos el periodo (100ms) + margen
    Thread.sleep(PERIODO_TEST + 20);

    assertTrue(estacion.realizarLecturasPeriodicas());
  }

  @Test
  public void testLecturaPeriodica_RespetaMaxLecturas() throws SensorDuplicadoException {
    // Configuramos maxLecturas a 1
    estacion.setMaxLecturas(1);
    SensorStub s1 = new SensorStub("T1", "T");
    SensorStub s2 = new SensorStub("T2", "T");
    estacion.addSensor(s1);
    estacion.addSensor(s2);

    estacion.realizarLecturasPeriodicas();

    int totalMediciones = s1.contadorMediciones + s2.contadorMediciones;
    assertEquals(1, totalMediciones);
  }

  @Test
  public void testLectura_SinSensores_NoFalla() {
    // Estación vacía
    estacion.realizarLecturaPuntual(-1);
    boolean res = estacion.realizarLecturasPeriodicas();

    // Debe devolver true aunque no haya sensores que medir
    assertTrue(res);
  }

  // PRUEBAS DE SETTER

  @Test
  public void testSetMaxLecturas_IgnoraValoresNoPositivos() {
    estacion.setMaxLecturas(5);
    estacion.setMaxLecturas(-10); // Debería ignorarse

    assertEquals(5, estacion.getMaxLecturas());
  }

  // PRUEBAS DE toString()

  @Test
  public void testToString_FormatoCorrecto() throws SensorDuplicadoException {
    estacion.addSensor(new SensorTemperatura(0.05));
    estacion.addSensor(new SensorHumedad(0.05));
    estacion.addSensor(new SensorPresion(0.05));

    estacion.realizarLecturaPuntual(-1);
    String res = estacion.toString();
    LocalDate ahora = LocalDate.now();

    assertTrue(res.contains("TEMP-0001 (desde: " + ahora + "): Sensor Temperatura ("));
    assertTrue(res.contains("ºC) última lectura: " + ahora));

    assertTrue(res.contains("HUM-0001 (desde: " + ahora + "): Sensor Humedad ("));
    assertTrue(res.contains("%) última lectura: " + ahora));

    assertTrue(res.contains("PRES-0001 (desde: " + ahora + "): Sensor Presión ("));
    assertTrue(res.contains(" hPa) última lectura: " + ahora));
  }

  // CLASE AUXILIAR
  private static class SensorStub extends Sensor {
    private String id;
    private String tipo;
    public int contadorMediciones = 0;
    private String nombre;

    public SensorStub(String id, String tipo) {
      this(id, tipo, "Sensor");
    }

    public SensorStub(String id, String tipo, String nombre) {
      super(tipo, 0.5, Unidad.PORCENTAJE, 0, 100, new EstrategiaAleatoria(0.05));
      this.id = id;
      this.tipo = tipo;
      this.nombre = nombre;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public String getTipo() {
      return this.tipo;
    }

    @Override
    public void realizarMedicion() {
      this.contadorMediciones++;
    }

    @Override
    public void setUnidad(Unidad u) {

    }

    @Override
    public String toString() {
      return String.format("Sensor %s (%.2f%s) última lectura: %s",
          nombre,
          getValorUltimaLectura(),
          getUnidad().getTexto(),
          getFechaUltimaLectura());
    }
  }
}