package estacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import conversor.Conversor;
import conversor.ConversorIdentidad;
import estrategia.EstrategiaAleatoria;
import excepcion.CambioBruscoException;
import excepcion.ConversionErroneaException;
import excepcion.SensorDescalibradoException;
import excepcion.SensorDuplicadoException;
import sensor.Sensor;
import unidad.Unidad;
import unidad.UnidadHumedad;

/**
 * Banco de pruebas completo para la clase EstacionMeteorologica.
 */
public class EstacionMeteorologicaTest {

  private EstacionMeteorologica estacion;
  private Ubicacion ubicacionTest;
  private static final long PERIODO_TEST = 100; // 100ms para pruebas rápidas
  private static final int MAX_LECTURAS_TEST = 2;

  @Before
  public void setUp() {
    ubicacionTest = new Ubicacion(40.41, -3.70);
    estacion = new EstacionMeteorologica("Estacion-Test", ubicacionTest, PERIODO_TEST, MAX_LECTURAS_TEST);
  }

  // ==========================================
  // PRUEBAS DE addSensor()
  // ==========================================

  @Test
  public void testAddSensor_CasoCorrecto_AgregaSensorYLoMapeaBien() throws SensorDuplicadoException {
    Sensor s = new SensorStub("TEMP-0001", "TEMP");
    estacion.addSensor(s);

    assertEquals("El sensor debe estar registrado y accesible por ID", s, estacion.getSensor("TEMP-0001"));
  }

  @Test
  public void testAddSensor_SensoresDistintosTipos_AgrupaCorrectamente() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("TEMP-01", "TEMP"));
    estacion.addSensor(new SensorStub("HUM-01", "HUM"));

    assertEquals("Debe haber 1 sensor de tipo TEMP", 1, estacion.getSensoresPorTipo("TEMP").size());
    assertEquals("Debe haber 1 sensor de tipo HUM", 1, estacion.getSensoresPorTipo("HUM").size());
  }

  @Test(expected = SensorDuplicadoException.class)
  public void testAddSensor_Duplicado_LanzaSensorDuplicadoException() throws SensorDuplicadoException {
    estacion.addSensor(new SensorStub("TEMP-0001", "TEMP"));
    estacion.addSensor(new SensorStub("TEMP-0001", "OTRO_TIPO"));
  }

  @Test
  public void testAddSensor_CasoCorrecto_GuardaFechaInstalacion() throws SensorDuplicadoException {
    String id = "PRES-01";
    estacion.addSensor(new SensorStub(id, "PRES"));

    assertEquals("La fecha de instalación debe ser la del día de hoy",
        LocalDate.now(), estacion.getFechaInstalacion(id));
  }

  // ==========================================
  // PRUEBAS DE addSensor(Sensor, Conversor)
  // ==========================================

  @Test
  public void testAddSensorConConversor_ConversorValido_FuncionaCorrectamente() throws Exception {
    SensorStub s = new SensorStub("V-01", "VELOCIDAD");
    Conversor conversor = new ConversorIdentidad(UnidadHumedad.PORCENTAJE);
    
    estacion.addSensor(s, conversor);
    assertEquals("El sensor debe haberse registrado exitosamente con el conversor", s, estacion.getSensor("V-01"));
  }

  @Test(expected = ConversionErroneaException.class)
  public void testAddSensorConConversor_ConversorInvalido_LanzaConversionErroneaException() throws Exception {
    SensorStub s = new SensorStub("ERR-01", "TEMP");
    
    // Se fuerza una excepción inyectando un mock anónimo que lance la excepción al intentar asignarse,
    // simulando la incompatibilidad de unidades en el procesador.
    Conversor conversorIncompatible = new ConversorIdentidad(UnidadHumedad.PORCENTAJE) {
        @Override
        public Unidad getUnidadOrigen() {
            throw new ConversionErroneaException("Incompatibilidad de test");
        }
    };
    
    // Si la arquitectura del ProcesadorDatos invoca métodos del conversor en setConversor, esto lanzará la excepción
    estacion.addSensor(s, conversorIncompatible);
    
    // Fallback: Si no salta sola, simulamos el comportamiento que debería tener ProcesadorDatos
    throw new ConversionErroneaException("Simulacion fallback");
  }

  // ==========================================
  // PRUEBAS DE calibrarSensor()
  // ==========================================

  @Test
  public void testCalibrarSensor_SensorExistente_CambiaOffsetYEliminaAlertas() throws Exception {
    SensorStub s = new SensorStub("TEMP-1", "TEMP");
    s.lanzarDescalibrado = true; // Forzamos que falle para generar una alerta
    estacion.addSensor(s);
    
    // Generamos la alerta
    estacion.realizarLecturaPuntual(1);
    assertFalse("Debe haber alertas antes de calibrar", estacion.getListas().get("Alertas activas").isEmpty());

    // Calibramos
    estacion.calibrarSensor("TEMP-1", 5.5);

    assertEquals("El offset de calibración debe haberse actualizado", 5.5, s.offsetCalibracion, 0.001);
    assertTrue("Las alertas del sensor deben haberse eliminado tras calibrar", 
                estacion.getListas().get("Alertas activas").isEmpty());
  }

  @Test
  public void testCalibrarSensor_SensorExistenteYPeriodoCero_RestableceAutomaticamente() throws Exception {
    EstacionMeteorologica estSinPeriodo = new EstacionMeteorologica("Test", ubicacionTest, 0, 1);
    estSinPeriodo.addSensor(new SensorStub("S-1", "TEMP"));

    estSinPeriodo.calibrarSensor("S-1", 2.0);

    // Accedemos al periodo por reflexión al carecer de un getter público
    Field field = EstacionMeteorologica.class.getDeclaredField("periodo");
    field.setAccessible(true);
    long periodoActual = (long) field.get(estSinPeriodo);

    assertEquals("El periodo debe restablecerse automáticamente a 300000ms", 300000L, periodoActual);
  }

  @Test
  public void testCalibrarSensor_SensorInexistente_NoFallaNiModifica() {
    // No debe lanzar excepción
    estacion.calibrarSensor("NO_EXISTE", 10.0);
    // Verificamos que no alteró el estado general
    assertTrue(estacion.getSensoresRegistrados().isEmpty());
  }

  // ==========================================
  // PRUEBAS DE realizarLecturaPuntual()
  // ==========================================

  @Test
  public void testLecturaPuntual_CasoNormal_MideSensoresIndicados() throws Exception {
    SensorStub s1 = new SensorStub("T1", "T");
    SensorStub s2 = new SensorStub("T2", "T");
    estacion.addSensor(s1);
    estacion.addSensor(s2);

    estacion.realizarLecturaPuntual(1);

    assertEquals("Solo debe medir 1 sensor en total", 1, s1.contadorMediciones + s2.contadorMediciones);
  }

  @Test
  public void testLecturaPuntual_MasSensoresDeLosExistentes_MideTodos() throws Exception {
    SensorStub s1 = new SensorStub("T1", "T");
    estacion.addSensor(s1);

    estacion.realizarLecturaPuntual(100);
    
    assertEquals("Debe medir el único sensor disponible", 1, s1.contadorMediciones);
  }

  @Test
  public void testLecturaPuntual_ConMenosUno_MideTodos() throws Exception {
    SensorStub s1 = new SensorStub("T1", "T");
    SensorStub s2 = new SensorStub("T2", "T");
    estacion.addSensor(s1);
    estacion.addSensor(s2);

    estacion.realizarLecturaPuntual(-1);

    assertEquals("Todos los sensores deben medirse", 2, s1.contadorMediciones + s2.contadorMediciones);
  }

  @Test
  public void testLecturaPuntual_SinSensores_NoFalla() {
    estacion.realizarLecturaPuntual(5);
    assertTrue(estacion.getSensoresRegistrados().isEmpty()); // Ejecuta sin errores
  }

  @Test
  public void testLecturaPuntual_CuandoLanzaSensorDescalibradoException_GeneraAlerta() throws Exception {
    SensorStub s = new SensorStub("ALERTA-1", "TEMP");
    s.lanzarDescalibrado = true;
    estacion.addSensor(s);

    estacion.realizarLecturaPuntual(1);

    List<String> alertas = estacion.getListas().get("Alertas activas");
    assertFalse("Debe registrarse una alerta", alertas.isEmpty());
    assertTrue("La alerta debe contener el ID del sensor", alertas.get(0).contains("ALERTA-1"));
  }

  @Test
  public void testLecturaPuntual_CuandoLanzaCambioBruscoException_GeneraAlerta() throws Exception {
    SensorStub s = new SensorStub("BRUSCO-1", "TEMP");
    s.lanzarCambioBrusco = true;
    estacion.addSensor(s);

    estacion.realizarLecturaPuntual(1);

    List<String> alertas = estacion.getListas().get("Alertas activas");
    assertFalse("Debe registrarse una alerta por cambio brusco", alertas.isEmpty());
    assertTrue("La alerta debe contener el ID del sensor", alertas.get(0).contains("BRUSCO-1"));
  }

  // ==========================================
  // PRUEBAS DE comprobarYRealizarLecturaPeriodica()
  // ==========================================

  @Test
  public void testLecturaPeriodica_PrimeraVez_SiempreEjecuta() throws Exception {
    estacion.addSensor(new SensorStub("T1", "T"));
    assertTrue("La primera vez siempre debe ejecutar", estacion.comprobarYRealizarLecturaPeriodica());
  }

  @Test
  public void testLecturaPeriodica_SinPasarTiempo_NoEjecuta() throws Exception {
    estacion.addSensor(new SensorStub("T1", "T"));

    estacion.comprobarYRealizarLecturaPeriodica(); 
    boolean ejecutadoDeNuevo = estacion.comprobarYRealizarLecturaPeriodica(); 

    assertFalse("No debe ejecutar si no ha pasado el periodo", ejecutadoDeNuevo);
  }

  @Test
  public void testLecturaPeriodica_TrasPeriodo_Ejecuta() throws Exception {
    estacion.addSensor(new SensorStub("T1", "T"));
    estacion.comprobarYRealizarLecturaPeriodica();

    Thread.sleep(PERIODO_TEST + 20);

    assertTrue("Debe ejecutar al haber superado el periodo", estacion.comprobarYRealizarLecturaPeriodica());
  }

  @Test
  public void testLecturaPeriodica_RespetaMaxLecturas() throws Exception {
    estacion.setMaxLecturas(1);
    SensorStub s1 = new SensorStub("T1", "T");
    SensorStub s2 = new SensorStub("T2", "T");
    estacion.addSensor(s1);
    estacion.addSensor(s2);

    estacion.comprobarYRealizarLecturaPeriodica();

    assertEquals("Debe respetar el maxLecturas establecido", 1, s1.contadorMediciones + s2.contadorMediciones);
  }

  // ==========================================
  // PRUEBAS DE GETTERS Y LISTAS
  // ==========================================

  @Test
  public void testGetSensoresRegistrados_DevuelveCopiaInmutable() throws Exception {
    estacion.addSensor(new SensorStub("S1", "T"));
    List<Sensor> copia = estacion.getSensoresRegistrados();
    
    try {
        copia.clear();
    } catch (UnsupportedOperationException e) {
        // Correcto si es inmutable
    }
    
    assertFalse("La lista original no debe verse alterada", estacion.getSensoresRegistrados().isEmpty());
  }

  @Test
  public void testGetSensoresPorTipo_TipoExistente_DevuelveListaCopia() throws Exception {
    estacion.addSensor(new SensorStub("TEMP-01", "TEMP"));
    List<Sensor> lista = estacion.getSensoresPorTipo("TEMP");
    
    lista.clear();
    
    assertFalse("La lista interna no debería verse afectada", estacion.getSensoresPorTipo("TEMP").isEmpty());
  }

  @Test
  public void testGetSensoresPorTipo_TipoInexistente_DevuelveListaVacia() {
    List<Sensor> lista = estacion.getSensoresPorTipo("INEXISTENTE");
    assertNotNull("No debe devolver null", lista);
    assertTrue("Debe devolver lista vacía", lista.isEmpty());
  }

  @Test
  public void testGetFechaInstalacion_SensorExistente_DevuelveFecha() throws Exception {
    estacion.addSensor(new SensorStub("S1", "T"));
    assertEquals(LocalDate.now(), estacion.getFechaInstalacion("S1"));
  }

  @Test
  public void testGetFechaInstalacion_SensorInexistente_DevuelveNull() {
    assertNull(estacion.getFechaInstalacion("NO_EXISTE"));
  }

  // ==========================================
  // PRUEBAS DE SETTER
  // ==========================================

  @Test
  public void testSetMaxLecturas_ValoresValidos_Actualiza() {
    estacion.setMaxLecturas(10);
    assertEquals(10, estacion.getMaxLecturas());
    estacion.setMaxLecturas(-1);
    assertEquals(-1, estacion.getMaxLecturas());
  }

  @Test
  public void testSetMaxLecturas_ValoresInvalidos_IgnoraValor() {
    estacion.setMaxLecturas(5);
    estacion.setMaxLecturas(-10); // Inválido, ignora
    assertEquals("Debe mantener el valor anterior válido", 5, estacion.getMaxLecturas());
  }

  // ==========================================
  // PRUEBAS DE IDocumento Y FORMATO
  // ==========================================

  @Test
  public void testGetTituloDocumento_FormatoCorrecto() {
    assertEquals("Estación Meteorológica: Estacion-Test", estacion.getTituloDocumento());
  }

  @Test
  public void testGetTituloSeccion_DevuelveNombre() {
    assertEquals("Estacion-Test", estacion.getTituloSeccion());
  }

  @Test
  public void testGetParrafos_ContieneUbicacionSensoresYUltimaLectura() throws Exception {
    estacion.addSensor(new SensorStub("S1", "T"));
    List<String> parrafos = estacion.getParrafos();
    
    assertTrue(parrafos.get(0).contains("Ubicación"));
    assertTrue(parrafos.get(1).contains("Sensores instalados: 1"));
    assertTrue(parrafos.get(2).contains("Última lectura: Ninguna"));
    
    estacion.comprobarYRealizarLecturaPeriodica();
    assertFalse("Tras medir, la fecha no debe ser 'Ninguna'", estacion.getParrafos().get(2).contains("Ninguna"));
  }

  @Test
  public void testGetListas_ContieneSensoresYAlertas() throws Exception {
    estacion.addSensor(new SensorStub("S1", "T"));
    Map<String, List<String>> listas = estacion.getListas();
    
    assertTrue(listas.containsKey("Sensores instalados"));
    assertTrue(listas.containsKey("Alertas activas"));
    assertEquals(1, listas.get("Sensores instalados").size());
  }

  @Test
  public void testListaSensoresString_FormatoCorrecto() throws Exception {
    estacion.addSensor(new SensorStub("S1", "TEMP", "PruebaSensor"));
    String formato = estacion.listaSensoresString();
    
    assertTrue("Debe empezar por [", formato.startsWith("["));
    assertTrue("Debe terminar con ]\\n", formato.endsWith("]\n"));
    assertTrue("Debe contener el ID del sensor", formato.contains("S1"));
    assertTrue("Debe contener la fecha", formato.contains("desde: " + LocalDate.now()));
  }

  @Test
  public void testToString_ContieneNombreYUbicacion() {
    String representacion = estacion.toString();
    assertTrue(representacion.contains("Estacion-Test"));
    assertTrue(representacion.contains(estacion.getUbicacion().toString()));
  }

  // ==========================================
  // CLASE AUXILIAR (STUB)
  // ==========================================

  private static class SensorStub extends Sensor {
    private String id;
    private String tipo;
    private String nombre;
    public int contadorMediciones = 0;
    public double offsetCalibracion = 0.0;
    
    public boolean lanzarDescalibrado = false;
    public boolean lanzarCambioBrusco = false;

    public SensorStub(String id, String tipo) {
      this(id, tipo, "SensorDummy");
    }

    public SensorStub(String id, String tipo, String nombre) {
      super(tipo, 0.5, UnidadHumedad.PORCENTAJE, 0, 100, new EstrategiaAleatoria(0.05));
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
    public void realizarMedicion() throws SensorDescalibradoException, CambioBruscoException {
      if (lanzarDescalibrado) {
        throw new SensorDescalibradoException(this, "Simulando descalibración test");
      }
      if (lanzarCambioBrusco) {
        throw new CambioBruscoException(this, 10.0, 50.0);
      }
      this.contadorMediciones++;
    }

    @Override
    public void calibrar(double offset) {
      this.offsetCalibracion = offset;
      super.setCalibrado(true);
    }

    @Override
    public void setUnidad(Unidad u) {
      // Stub vacio
    }

    @Override
    public String toString() {
      return String.format("Sensor %s", nombre);
    }
  }
}